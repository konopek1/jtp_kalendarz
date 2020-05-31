package com.example.kalendarz.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.common.EventListAdapter;
import com.example.kalendarz.exceptions.PermissionDeniedException;
import com.example.kalendarz.notifcations.NotifyReciver;
import com.example.kalendarz.Importer.CalendarApi;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

import static com.example.kalendarz.Importer.CalendarApi.CALENDAR_PERMISSION_CODE;

/**
 * Main activty responsible for displaying calendar and events
 */
public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RecyclerView mRecyclerView;
    private CalendarView mCalendarView;
    private EventListAdapter mAdapter;
    private long currentCalendarDate = (new Date()).getTime();
    private EventDAO eventDAO;
    private RealmResults<Event> events;
    private NotifyReciver notifyReciver;

    /**
     * Lambda Caalled when Event's in DB are updated,deleted,inserted
     */
    private final RealmChangeListener<RealmResults<Event>> realmChangeListener = events -> mAdapter.notifyDataSetChanged();

    /**
     * Lambda called when user change date on calendar view
     */
    private final CalendarView.OnDateChangeListener onDateChangeListener = (calendarView, year, month, day) -> {
        events = eventDAO.getEventsByDateSortedByDate(realm,year,month,day);
        events.addChangeListener(realmChangeListener);
        setCurrentCalendarDate(year,month,day);
        mAdapter.updateData(events);
    };

    /**
     * The constant DATE_EXTRAS.
     */
    public static final String DATE_EXTRAS = "com.example.activities.DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar();
        setUp();
        initCalendarView();
        events = getEventsByCalendarDate(mCalendarView.getDate());
        initRecyclerViewFromEvents();
        bindEvents();
        notifyReciver = new NotifyReciver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     * Check if application have needed permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALENDAR_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importEventsFromGoogleCalendar();
                } else {
                    Toast.makeText(getApplicationContext(), "Cant import data \n permission not granted.", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    /**
     * Get cursor over Google Calendar DB on client phone
     * @return Cursor crs
     */
    private Cursor getCursorForCalendarEvents() {
        Cursor crs = null;
        try {
            crs = CalendarApi.getCursorForEvents(this);
        } catch (PermissionDeniedException e) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_CODE);
            return crs;
        }
        return  crs;
    }

    private void importEventsFromGoogleCalendar() {
        Cursor crs = getCursorForCalendarEvents();
        creatEventFromCalendarProvider(crs);
    }

    /**
     * Traverse whole GoogleCalendarDB
     * @param crs
     */
    private void creatEventFromCalendarProvider(Cursor crs) {
        Realm realm = RealmProvider.getRealm();
        crs.moveToFirst();
        while(crs.moveToNext()){
            Event event = EventFromCalendarProviderProjection(crs);
            realm.beginTransaction();
            realm.insert(event);
            realm.commitTransaction();
        }
        crs.close();
    }

    /**
     * @param crs
     * @return Event
     * Create event based on GoogleEvent
     */
    @NotNull
    private Event EventFromCalendarProviderProjection(Cursor crs) {
        long startDate = crs.getLong(CalendarApi.EVENTS_DSTART_INDEX);
        long endDate = crs.getLong(CalendarApi.EVENTS_DEND_INDEX);
        String eventTitle =  crs.getString(CalendarApi.EVENTS_TITLE_INDEX);
        boolean isToDo = crs.getInt(CalendarApi.EVENTS_ISTODO_INDEX) == 1;
        return new Event(eventTitle,new Date(startDate),new Date(endDate),false,isToDo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    /**
     * @param item
     * @return
     * Handle context menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_to_today:
                changeCalendarDate((new Date()).getTime());
                break;
            case R.id.import_from_calendar:
                importEventsFromGoogleCalendar();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * @param date
     * Change calendar date
     */
    private void changeCalendarDate(long date) {
        mCalendarView.setDate(date);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        onDateChangeListener.onSelectedDayChange(mCalendarView,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Sets up.
     */
    public void setUp() {
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        realm = RealmProvider.getRealm();
        eventDAO = EventDAO.getInstance();
    }

    /**
     * Bind events.
     */
    public void bindEvents() {
        events.addChangeListener(realmChangeListener);
        mCalendarView.setOnDateChangeListener(onDateChangeListener);
    }

    /**
     * Init calendar view.
     */
    public void initCalendarView() {
        mCalendarView = findViewById(R.id.calendarView);
    }

    /**
     * init EventListAdapter with events and render RecyclerView
     */
    public void initRecyclerViewFromEvents() {
        mAdapter = new EventListAdapter(this, events);
        mRecyclerView = findViewById(R.id.eventRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    /**
     * Get Eveents for particular date
     *
     * @param timestamp the timestamp
     * @return events by calendar date
     */
    public RealmResults<Event> getEventsByCalendarDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return eventDAO.getEventsByDateSortedByDate(realm, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * On fab click.
     *
     * @param view the view
     */
    public void onFABClick(View view) {
        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra(DATE_EXTRAS,currentCalendarDate);
        startActivity(intent);
    }

    /**
     * Gets current calendar date.
     *
     * @return the current calendar date
     */
    public long getCurrentCalendarDate() {
        return currentCalendarDate;
    }

    /**
     * Sets current calendar date.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public void setCurrentCalendarDate(int year,int month,int day) {
        this.currentCalendarDate = DateFormatter.getDateFromYYMMDD(year, month, day).getTime();
    }
}

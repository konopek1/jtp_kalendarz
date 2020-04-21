package com.example.kalendarz.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RecyclerView mRecyclerView;
    private CalendarView mCalendarView;
    private EventListAdapter mAdapter;
    private long currentCalendarDate = (new Date()).getTime();
    private EventDAO eventDAO;
    private RealmResults<Event> events;
    private RealmChangeListener<RealmResults<Event>> realmChangeListener = events -> mAdapter.notifyDataSetChanged();
    private CalendarView.OnDateChangeListener onDateChangeListener = (calendarView, year, month, day) -> {
        events = eventDAO.getEventsByDateSortedByDate(realm,year,month,day);
        setCurrentCalendarDate(year,month,day);
        mAdapter.updateData(events);
        bindEvents();
    };

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_to_today:
                changeCalendarDate((new Date()).getTime());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void changeCalendarDate(long date) {
        mCalendarView.setDate(date);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        onDateChangeListener.onSelectedDayChange(mCalendarView,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }

    public void setUp() {
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        realm = RealmProvider.getRealm();
        eventDAO = EventDAO.getInstance();
    }

    public void bindEvents() {
        events.addChangeListener(realmChangeListener);
        mCalendarView.setOnDateChangeListener(onDateChangeListener);
    }

    public void initCalendarView() {
        mCalendarView = findViewById(R.id.calendarView);
    }

    public void initRecyclerViewFromEvents() {
        mAdapter = new EventListAdapter(this, events);
        mRecyclerView = findViewById(R.id.eventRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public RealmResults<Event> getEventsByCalendarDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return eventDAO.getEventsByDateSortedByDate(realm, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void onFABClick(View view) {
        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra(DATE_EXTRAS,currentCalendarDate);
        startActivity(intent);
    }

    public long getCurrentCalendarDate() {
        return currentCalendarDate;
    }

    public void setCurrentCalendarDate(int year,int month,int day) {
        this.currentCalendarDate = DateFormatter.getDateFromYYMMDD(year, month, day).getTime();
    }
}

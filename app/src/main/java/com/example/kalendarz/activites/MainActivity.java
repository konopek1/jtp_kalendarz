package com.example.kalendarz.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.common.EventListAdapter;
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
    private EventDAO eventDAO;
    private RealmResults<Event> events;
    private RealmChangeListener<RealmResults<Event>> realmChangeListener = events -> mAdapter.notifyDataSetChanged();
    private CalendarView.OnDateChangeListener onDateChangeListener = (calendarView, year, month, day) -> {
        events = eventDAO.getEventsByDate(realm,year,month,day);
        mAdapter.updateData(events);
        bindEvents();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        return eventDAO.getEventsByDate(realm, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void onFABClick(View view) {
        Intent intent = new Intent(this, AdderActivity.class);
        startActivity(intent);
    }

}

package com.example.kalendarz.activites;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.common.EventListAdapter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventListAdapter mAdapter;
    private RealmResults<Event> events;
    private RealmChangeListener<RealmResults<Event>> realmChangeListener = events -> mAdapter.notifyDataSetChanged();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());

        Realm realm = RealmProvider.getRealm();
        events = Event.getAllEvents(realm);
        events.addChangeListener(realmChangeListener);

        mAdapter = new EventListAdapter(this, events);
        mRecyclerView = findViewById(R.id.eventRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onFABClick(View view) {
        Intent intent = new Intent(this,AdderActivity.class);
        startActivity(intent);
    }


}

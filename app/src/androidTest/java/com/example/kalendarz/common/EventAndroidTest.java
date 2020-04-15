package com.example.kalendarz.common;

import androidx.test.runner.AndroidJUnit4;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EventAndroidTest {
    private Realm realm;
    private EventFabric eventFabric;


    @Before
    public void setUp() {
        Realm.init(getTargetContext());
        RealmConfiguration config = new RealmConfiguration.Builder().inMemory().name("test.realm").build();

        eventFabric = new EventFabric();

        realm = Realm.getInstance(config);
    }

    @After
    public void tearDown() throws Exception {
        realm.close();
    }

    @Test
    public void save() {
        Event event = eventFabric.produceEvent();

        event.save(realm);

        RealmQuery<Event> query = realm.where(Event.class);
        Event retriveEvent = query.findAll().last();

        assertThat(retriveEvent.getId(),equalTo(event.getId()));
    }

    @Test
    public void shouldReturnAllEvents() {
        final int eventSize = 10;
        Event event;

        realm.beginTransaction();
        for(int i =0; i <eventSize; i++) {
            event = eventFabric.produceEvent();
            realm.insert(event);
        }
        realm.commitTransaction();

        RealmResults<Event> retriveEvents =  Event.getAllEvents(realm);

        assertEquals(eventSize,retriveEvents.size());
    }
}


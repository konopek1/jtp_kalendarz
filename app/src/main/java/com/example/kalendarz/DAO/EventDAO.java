package com.example.kalendarz.DAO;

import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import java.util.Calendar;
import java.util.Date;

public class EventDAO {

    private static EventDAO instance;

    private EventDAO() {
    }

    public static EventDAO getInstance() {
        if(instance == null) {
            instance = new EventDAO();
            return instance;
        }
        return instance;
    }

    public  void  save(Realm realm,Event event) {
        realm.beginTransaction();
        realm.insert(event);
        realm.commitTransaction();
    }

    public  RealmResults<Event> getAllEvents(Realm realm) {
        RealmQuery<Event> query = realm.where(Event.class);
        return query.findAll();
    }

    public RealmResults<Event> getEventsByDate(Realm realm,int year,int month, int day) {
        Date startOfDay = DateFormatter.getDateFromYYMMDD(year, month, day);
        Date endOfDay = DateFormatter.getDateFromYYMMDD(year, month, day + 1);

        RealmQuery<Event> query = realm.where(Event.class);
        query.between("date",startOfDay,endOfDay);

        return query.findAll();
    }
}

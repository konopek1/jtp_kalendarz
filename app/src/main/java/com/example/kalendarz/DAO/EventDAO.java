package com.example.kalendarz.DAO;

import android.widget.Toast;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.DateFormatter;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

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
        try {
            realm.insert(event);
        }
        catch (RealmPrimaryKeyConstraintException e) {
            realm.cancelTransaction();
            return;
        }
        realm.commitTransaction();
    }

    public  RealmResults<Event> getAllEvents(Realm realm) {
        RealmQuery<Event> query = realm.where(Event.class);
        return query.findAll();
    }

    /**
     * @param realm
     * @param year
     * @param month
     * @param day
     * @return RealmResults<Event>
     * Zwraca Eventy posortowane Datą na przestrzeni jakiegoś czasu
     */
    public RealmResults<Event> getEventsByDateSortedByDate(Realm realm, int year, int month, int day) {
        Date startOfDay = DateFormatter.getDateFromYYMMDD(year, month, day);
        Date endOfDay = DateFormatter.getDateFromYYMMDD(year, month, day + 1);

        RealmQuery<Event> query = realm.where(Event.class);
        query
                .between("date",startOfDay,endOfDay).sort("date");

        return query.findAll();
    }
}

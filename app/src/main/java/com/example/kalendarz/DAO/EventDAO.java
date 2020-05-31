package com.example.kalendarz.DAO;

import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.DateFormatter;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import java.util.Date;

/**
 * The type Event dao.
 */
public class EventDAO {

    private static EventDAO instance;

    private EventDAO() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EventDAO getInstance() {
        if(instance == null) {
            instance = new EventDAO();
            return instance;
        }
        return instance;
    }

    /**
     * Save.
     *
     * @param realm the realm
     * @param event the event
     */
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

    /**
     * Gets all events.
     *
     * @param realm the realm
     * @return the all events
     */
    public  RealmResults<Event> getAllEvents(Realm realm) {
        RealmQuery<Event> query = realm.where(Event.class);
        return query.findAll();
    }

    /**
     * Gets events by date sorted by date.
     *
     * @param realm the realm
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return RealmResults Return events sorted by date
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

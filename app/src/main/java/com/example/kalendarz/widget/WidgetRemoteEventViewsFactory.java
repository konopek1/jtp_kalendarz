package com.example.kalendarz.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.common.EventListAdapter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.kalendarz.widget.WidgetProvider.EVENTS_CONTET;
import static com.example.kalendarz.widget.WidgetProvider.EVENTS_TIME;

/**
 * The type Widget remote event views factory.
 */
public class WidgetRemoteEventViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<String> eventsContent = new ArrayList<String>();
    private ArrayList<String> eventsTime = new ArrayList<String>();

    /**
     * Instantiates a new Widget remote event views factory.
     *
     * @param ctx    the ctx
     * @param intent the intent
     */
    public WidgetRemoteEventViewsFactory(Context ctx, Intent intent) {
        mContext = ctx;
    }

    @Override
    public void onCreate() {

    }

    /**
     * Function called when data changes
     */
    @Override
    public void onDataSetChanged() {
        eventsContent.clear();
        eventsTime.clear();
        Realm.init(mContext);
        Realm realm = RealmProvider.getRealm();

        Calendar c =Calendar.getInstance();
        c.setTime((new Date()));

        RealmResults<Event> events = (EventDAO.getInstance()).getEventsByDateSortedByDate(realm,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        for (Event event : events) {
            eventsContent.add(event.getContent());
            eventsTime.add(EventListAdapter.formatTimeRange(event.getDate(), event.getEndDate()));
        }

        realm.close();
    }

    /**
     * Function called when objet is destroyed
     */
    @Override
    public void onDestroy() {
        eventsTime.clear();
        eventsContent.clear();
    }

    @Override
    public int getCount() {
        return eventsTime.size();
    }

    /**
     * @param position
     * @return RemoteViews return list element mapped from event
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.event_row_widget);
        rv.setTextViewText(R.id.eventTimeRange, eventsTime.get(position));
        rv.setTextViewText(R.id.eventContent,eventsContent.get(position));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

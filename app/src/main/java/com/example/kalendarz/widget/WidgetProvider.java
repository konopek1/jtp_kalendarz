package com.example.kalendarz.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.activites.AdderActivity;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.common.EventListAdapter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmResults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.kalendarz.activites.MainActivity.DATE_EXTRAS;

/**
 * The type Widget provider.
 */
public class WidgetProvider extends AppWidgetProvider {
    /**
     * The constant EVENTS_CONTET.
     */
    public final static String EVENTS_CONTET = "WidgetProvider.EVENTS_CONTET";
    /**
     * The constant EVENTS_TIME.
     */
    public final static String EVENTS_TIME = "WidgetProvider.EVENTS_TIMES";

    /**
     * Called on widget update intent
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;


        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            PendingIntent updateIntent = createRefreshButtonIntent(context, appWidgetId);
            views.setOnClickPendingIntent(R.id.widgetRefreshButton,updateIntent);

            PendingIntent pendingIntent = createAddNewEventIntent(context);
            views.setOnClickPendingIntent(R.id.widgetAddNewEventButton, pendingIntent);

            Intent adapterIntent = createAdapterIntent(context, appWidgetId);
            views.setRemoteAdapter(R.id.widgetListView, adapterIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widgetListView);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Create intent to start AdderActivity with today date
     * @param context
     * @return
     */
    private PendingIntent createAddNewEventIntent(Context context) {
        Intent intent = new Intent(context, AdderActivity.class);
        intent.putExtra(DATE_EXTRAS, (new Date()).getTime());
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Create intent for starting adapter for event widget list
     * @param context
     * @param appWidgetId
     * @return
     */
    @NotNull
    private Intent createAdapterIntent(Context context, int appWidgetId) {
        Intent adapterIntent = new Intent(context, WidgetRemoteEventViewsService.class);
        adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        adapterIntent.setData(Uri.parse(adapterIntent.toUri(Intent.URI_INTENT_SCHEME)));
        return adapterIntent;
    }

    /**
     * Create Intent for refreshing which call onUpdate()
     * @param context
     * @param appWidgetId
     * @return
     */
    private PendingIntent createRefreshButtonIntent(Context context, int appWidgetId) {
        Intent intentUpdate = new Intent(context, WidgetProvider.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        return PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}

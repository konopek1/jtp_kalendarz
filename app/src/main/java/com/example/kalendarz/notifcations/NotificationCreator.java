package com.example.kalendarz.notifcations;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;

/**
 * Class for Notification Creator
 */
public class NotificationCreator {
    /**
     * The constant PRIMARY_CHANNEL_ID.
     */
    public static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    /**
     * The constant NOTIFICATION_ID_EXTRAS.
     */
    public static final String NOTIFICATION_ID_EXTRAS = "notification_id";
    /**
     * The constant NOTIFICATION_TITLE_EXTRAS.
     */
    public static final String NOTIFICATION_TITLE_EXTRAS = "title";
    /**
     * The constant NOTIFICATION_TEXT_EXTRAS.
     */
    public static final String NOTIFICATION_TEXT_EXTRAS = "text";
    /**
     * The constant notifyIntnetId.
     */
    public static final int notifyIntnetId = 0;


    /**
     * Create notification
     *
     * @param ctx   the ctx
     * @param id    the id
     * @param title the title
     * @param text  the text
     * @param when  the when
     */
    public static void createEventNotification(Context ctx, int id, String title, String text, long when) {
        PendingIntent notifyPendingIntent = createNotificationEventForBroadcastReciver(ctx, id, title, text);
        addToAlarmManager(ctx, notifyPendingIntent, when);
    }

    /**
     * @param ctx
     * @param id
     * @param title
     * @param text
     * @return
     */
    private static PendingIntent createNotificationEventForBroadcastReciver(Context ctx, int id, String title, String text) {
        Intent notifyIntent = new Intent(ctx, NotifyReciver.class);
        notifyIntent.putExtra(NOTIFICATION_TITLE_EXTRAS, title);
        notifyIntent.putExtra(NOTIFICATION_TEXT_EXTRAS, text);
        notifyIntent.putExtra(NOTIFICATION_ID_EXTRAS, id);

        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (ctx, notifyIntnetId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return notifyPendingIntent;
    }

    private static void addToAlarmManager(Context ctx, PendingIntent notifyPendingIntent, long triggerTime) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, notifyPendingIntent);
    }

}

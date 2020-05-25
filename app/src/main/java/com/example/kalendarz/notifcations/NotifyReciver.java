package com.example.kalendarz.notifcations;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.example.kalendarz.R;
import com.example.kalendarz.activites.MainActivity;
import com.example.kalendarz.notifcations.NotificationCreator;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.kalendarz.notifcations.NotificationCreator.PRIMARY_CHANNEL_ID;
import static com.example.kalendarz.notifcations.NotificationCreator.notifyIntnetId;

public class NotifyReciver extends BroadcastReceiver {

    private NotificationManager mNotifyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        setUpNotification(context);
        deliverNotification(intent.getExtras(), context);
    }

    /**
     * @param intentBundle
     * @param ctx
     * Wyswietlenie powiadomienia, funkcja ta jest wywoływana prez AlarmManagera
     */
    private void deliverNotification(Bundle intentBundle, Context ctx) {
        int id = intentBundle.getInt(NotificationCreator.NOTIFICATION_ID_EXTRAS);
        String title = intentBundle.getString(NotificationCreator.NOTIFICATION_TITLE_EXTRAS);
        String text = intentBundle.getString(NotificationCreator.NOTIFICATION_TEXT_EXTRAS);

        Intent i = new Intent(ctx, MainActivity.class);
        Notification n = getNotificationBuilder(ctx, title, text, i).build();
        mNotifyManager.notify(id, n);
    }

    private void setUpNotification(Context ctx) {
        mNotifyManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(mNotifyManager);
    }

    /**
     * @param notificationManager
     * Stworzenie Kanału Powiadomień, taki Kanał musi istnieć aby nasza aplikacja mogłą wysyłać powiadomienia
     */
    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Calendar Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationChannel.setDescription("Notification from calendar");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Zwraca podstwowy styl w jakim będzie wyświetlana aplikacja, wzorzec Fabryki
     * @param ctx
     * @param title
     * @param text
     * @param intent
     * @return
     */
    private static NotificationCompat.Builder getNotificationBuilder(Context ctx, String title, String text, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notifyIntnetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(ctx, PRIMARY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return notifyBuilder;
    }
}

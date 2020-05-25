package com.example.kalendarz.notifcations;

import android.content.Context;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Klasa reprezentująca powiadomienie
 */
public class Notify extends RealmObject {
    @PrimaryKey
    private int id;
    private long when;
    private String title;
    private String text;

    public Notify() {
    }

    /**
     * W momencie tworzenia powiadomienie zostaję również  dodane powiadomienia do AlarmMenegera
     * któy odpala je w odpowiednim czasie
     * @param ctx
     * @param when
     * @param title
     * @param text
     */
    public Notify(Context ctx, long when, String title, String text) {
        this.id = generateId(when);
        this.when = when;
        this.title = title;
        this.text = text;
        NotificationCreator.createEventNotification(ctx, id, title, text, when);
    }

    private int generateId(long when) {
        return (int) when + id;
    }
}

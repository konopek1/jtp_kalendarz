package com.example.kalendarz.notifcations;

import android.content.Context;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Notify object stored in DB
 */
public class Notify extends RealmObject {
    @PrimaryKey
    private int id;
    private long when;
    private String title;
    private String text;

    /**
     * Instantiates a new Notify.
     */
    public Notify() {
    }

    /**
     *When Event is created also Notification is created and send to AlaramManager
     *
     * @param ctx   the ctx
     * @param when  the when
     * @param title the title
     * @param text  the text
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

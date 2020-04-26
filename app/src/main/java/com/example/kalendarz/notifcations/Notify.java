package com.example.kalendarz.notifcations;

import android.content.Context;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notify extends RealmObject {
    @PrimaryKey
    private int id;
    private long when;
    private String title;
    private String text;

    public Notify() {
    }

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

package com.example.kalendarz.common;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Event extends RealmObject {
    @PrimaryKey
    private long id;

    private String content;

    private Date date;

    private Date endDate;

    private boolean isDone;

    private boolean useNotification;

    private boolean isToDo;

    public Event() {

    }

    public Event(String opis, Date date) {
        this.content = opis;
        this.date = date;
    }

    public Event(String opis, Date date, Date endDate, boolean useNotification,boolean isToDo) {
        this.content = opis;
        this.date = date;
        this.endDate = endDate;
        this.useNotification = useNotification;
        this.id = getId();
        this.isDone = false;
        this.isToDo = isToDo;
    }

    public long getId() {
        long startTimestamp = date.getTime();
        long endTimestamp = date.getTime();
        long opisHashed = content.hashCode();
        return startTimestamp + endTimestamp + opisHashed;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isUseNotification() {
        return useNotification;
    }

    public boolean isToDo() { return isToDo; }
}





package com.example.kalendarz.common;

import com.example.kalendarz.notifcations.Notify;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.Date;

/**
 * The type Event.
 */
public class Event extends RealmObject {
    @PrimaryKey
    private long id;

    private String content;

    private Date date;

    private Date endDate;

    private boolean isDone;

    private boolean useNotification;

    private Notify notification;

    private boolean isToDo;

    /**
     * Instantiates a new Event.
     */
    public Event() {

    }

    /**
     * Instantiates a new Event.
     *
     * @param opis the opis
     * @param date the date
     */
    public Event(String opis, Date date) {
        this.content = opis;
        this.date = date;
    }

    /**
     * Instantiates a new Event.
     *
     * @param opis            the opis
     * @param date            the date
     * @param endDate         the end date
     * @param useNotification the use notification
     * @param isToDo          the is to do
     */
    public Event(String opis, Date date, Date endDate, boolean useNotification, boolean isToDo) {
        this.content = opis;
        this.date = date;
        this.endDate = endDate;
        this.useNotification = useNotification;
        this.id = getId();
        this.isDone = false;
        this.isToDo = isToDo;
    }

    /**
     * Instantiates a new Event.
     *
     * @param opis         the opis
     * @param date         the date
     * @param endDate      the end date
     * @param isToDo       the is to do
     * @param notification the notification
     */
    public Event(String opis, Date date, Date endDate, boolean isToDo, Notify notification) {
        this.content = opis;
        this.date = date;
        this.endDate = endDate;
        this.useNotification = true;
        this.id = getId();
        this.isDone = false;
        this.isToDo = isToDo;
        this.notification = notification;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        long startTimestamp = date.getTime();
        long endTimestamp = date.getTime();
        long opisHashed = content.hashCode();
        return startTimestamp + endTimestamp + opisHashed;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Is done boolean.
     *
     * @return the boolean
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Is use notification boolean.
     *
     * @return the boolean
     */
    public boolean isUseNotification() {
        return useNotification;
    }

    /**
     * Is to do boolean.
     *
     * @return the boolean
     */
    public boolean isToDo() { return isToDo; }

    /**
     * Sets done.
     *
     * @param done the done
     */
    public void setDone(boolean done) { isDone = done; }
}





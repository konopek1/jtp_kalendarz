package com.example.kalendarz.Importer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import androidx.core.content.ContextCompat;
import com.example.kalendarz.exceptions.PermissionDeniedException;


/**
 * Klasa ta jest odpowiedzialna za Komunikację z Kalendarzem Googl'a
 */
public class CalendarApi {

    public static final int EVENTS_DSTART_INDEX = 0;
    public static final int EVENTS_TITLE_INDEX = 1;
    public static final int EVENTS_DEND_INDEX = 2;
    public static final int EVENTS_ISTODO_INDEX = 3;

    public static final int CALENDAR_PERMISSION_CODE = 0;


    /**
     * Metoda zwraca kursor po bazie daych Google Kalendarza
     * @param ctx
     * @return
     * @throws PermissionDeniedException
     */
    @SuppressLint("MissingPermission")
    public static Cursor getCursorForCalendars(Context ctx) throws PermissionDeniedException {
        if(!checkForPermission(ctx)) {
            throw new PermissionDeniedException("Calendar");
        }
        Cursor cur = null;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        cur = ctx.getContentResolver()
                        .query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");

        return cur;
    }

    @SuppressLint("MissingPermission")
    public static Cursor getCursorForEvents(Context ctx) throws PermissionDeniedException {
        if(!checkForPermission(ctx)) {

            throw new PermissionDeniedException("Calendar");
        }
        Cursor cur = null;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection =
                new String[]{
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.ALL_DAY
                };

        cur = ctx.getContentResolver()
                .query(CalendarContract.Events.CONTENT_URI,
                        projection,
                        CalendarContract.Events.DELETED + " = 0",
                        null,
                        CalendarContract.Events.DTSTART + " ASC");
        return cur;
    }

    private static boolean checkForPermission(Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

}

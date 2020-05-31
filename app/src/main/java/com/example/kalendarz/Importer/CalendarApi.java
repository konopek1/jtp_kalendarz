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

    /**
     * The constant EVENTS_DSTART_INDEX.
     */
    public static final int EVENTS_DSTART_INDEX = 0;
    /**
     * The constant EVENTS_TITLE_INDEX.
     */
    public static final int EVENTS_TITLE_INDEX = 1;
    /**
     * The constant EVENTS_DEND_INDEX.
     */
    public static final int EVENTS_DEND_INDEX = 2;
    /**
     * The constant EVENTS_ISTODO_INDEX.
     */
    public static final int EVENTS_ISTODO_INDEX = 3;

    /**
     * The constant CALENDAR_PERMISSION_CODE.
     */
    public static final int CALENDAR_PERMISSION_CODE = 0;


    /**
     * Returns cursor for events
     *
     * @param ctx the ctx
     * @return the cursor for events
     * @throws PermissionDeniedException the permission denied exception
     */
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

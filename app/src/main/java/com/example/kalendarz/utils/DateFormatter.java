package com.example.kalendarz.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper class for formating dates
 */
public class DateFormatter {

    private final static String DDMMYYY = "%s.%s.%s";
    private final static String HHMM = "%s:%s";

    /**
     * Format date ddmmyyyy string.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return the string
     */
    public static String formatDateDDMMYYYY(int year, int month, int day) {
        String monthStr = Integer.toString(month);
        String dayStr = Integer.toString(day);
        String yearStr = Integer.toString(year);
        if (month < 10) monthStr = "0" + monthStr;
        if (day < 10) dayStr = "0" + dayStr;
        return String.format(DDMMYYY, dayStr, monthStr, yearStr);
    }

    /**
     * Format date ddmmyyyy string.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDateDDMMYYYY(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatDateDDMMYYYY(c.get(Calendar.YEAR),c.get(Calendar.MONTH) + 1,c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Gets current date ddmmyyyy.
     *
     * @return the current date ddmmyyyy
     */
    public static String getCurrentDateDDMMYYYY() {
        final Calendar c = Calendar.getInstance();
        return formatDateDDMMYYYY(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Format time hhmm string.
     *
     * @param hours the hours
     * @param mins  the mins
     * @return the string
     */
    public static String formatTimeHHMM(int hours, int mins) {
        String hoursStr = Integer.toString(hours);
        String minsStr = Integer.toString(mins);
        if (hours < 10) hoursStr = "0" + hoursStr;
        if (mins < 10) minsStr = "0" + minsStr;
        return String.format(HHMM, hoursStr, minsStr);
    }

    /**
     * Gets current time hhmm.
     *
     * @return the current time hhmm
     */
    public static String getCurrentTimeHHMM() {
        final Calendar c = Calendar.getInstance();
        return formatTimeHHMM(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    /**
     * Gets date from yymmdd.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return the date from yymmdd
     */
    public static Date getDateFromYYMMDD(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day,0,0,0);

        return c.getTime();
    }
}

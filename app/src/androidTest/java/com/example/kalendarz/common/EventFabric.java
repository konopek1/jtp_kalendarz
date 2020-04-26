package com.example.kalendarz.common;

import java.util.Calendar;

public class EventFabric {
    private  int counter;
    private int year = 2020;
    private int month = 5;
    private int day;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public EventFabric() {
        counter = 0;
    }

    public Event produceEvent(){
        String content = "Event " + counter;
        day = counter % 29;
        Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        boolean useNotification = (counter % 2 == 0);
        counter++;
        return new Event(content, c.getTime(), c.getTime(), useNotification, false);
    }
}

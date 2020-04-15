package com.example.kalendarz.common;

import java.util.Date;

public class EventFabric {
    private static  int counter;

    public EventFabric() {
        counter = 0;
    }

    public Event produceEvent(){
        String content = "Event " + counter++;
        Date dateFrom = new Date(2020,5,10);
        Date dateTo = new Date(2020,5,11);
        boolean useNotification = (counter % 2 == 0);

        return new Event(content,dateFrom,dateTo,useNotification);
    }
}

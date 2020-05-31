package com.example.kalendarz.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * The type Widget remote event views service.
 */
public class WidgetRemoteEventViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteEventViewsFactory(getApplicationContext(),intent);
    }
}

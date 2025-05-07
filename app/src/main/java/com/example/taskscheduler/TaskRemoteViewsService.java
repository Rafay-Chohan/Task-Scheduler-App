package com.example.taskscheduler;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TaskRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TaskRemoteViewsFactory(getApplicationContext());
    }
}

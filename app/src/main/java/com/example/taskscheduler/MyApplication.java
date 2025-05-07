package com.example.taskscheduler;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WorkScheduler.scheduleHourlyTaskCheck(this);
    }
}

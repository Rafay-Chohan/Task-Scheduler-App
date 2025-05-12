package com.example.taskscheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;

public class DailyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Check for tasks and show notification
        SchedulerDB db = new SchedulerDB(context);
        db.open();
        db.checkUpcomingTasks();
        db.close();
        Log.d("Notification","works");
        // Reschedule the alarm for next day (important for Android 6+)
        AlarmUtils.setDailyNotificationAlarm(context);
    }
}

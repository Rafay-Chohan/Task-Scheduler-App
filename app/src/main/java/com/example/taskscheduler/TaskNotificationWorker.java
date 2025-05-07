package com.example.taskscheduler;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;


public class TaskNotificationWorker extends Worker {
    public TaskNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("Workermf", "Worker running at " + new Date());
        SchedulerDB db = new SchedulerDB(getApplicationContext());
        db.checkUpcomingTasks();
        return Result.success();
    }
}

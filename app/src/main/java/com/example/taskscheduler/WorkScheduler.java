package com.example.taskscheduler;


import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkScheduler {
    private static final String WORKER_TAG = "TASK_NOTIFICATION_WORKER";

    public static void scheduleHourlyTaskCheck(Context context) {
        // Create a periodic work request that runs every hour
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                TaskNotificationWorker.class,
                1, // Repeat interval
                TimeUnit.HOURS)
                .build();

        // Enqueue the work, keeping only one instance of this worker
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP, // Keep the existing worker if one exists
                workRequest);
    }

    public static void cancelScheduledTask(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORKER_TAG);
    }
}

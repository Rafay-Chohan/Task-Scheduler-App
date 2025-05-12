package com.example.taskscheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "task_reminder_channel";
    private static final int NOTIFICATION_ID = 100;
    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Reminders";
            String description = "Notifications for upcoming tasks";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400});
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setShowBadge(true); // Allow app icon badges
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showTaskNotification(String taskTitle, String timeLeft) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return; // Or handle the case where permission isn't granted
        }
        // Create intent for when notification is tapped
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragment", "tasks");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.btn_notifications)
                .setContentTitle("Task Due Soon: " + taskTitle)
                .setContentText("Due in " + timeLeft)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(taskTitle + " is due in " + timeLeft + ". Don't forget to complete it!"));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Use a unique ID for each notification
        int notificationId = (int) System.currentTimeMillis();
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

        }
        notificationManager.notify(notificationId, builder.build());
    }
}

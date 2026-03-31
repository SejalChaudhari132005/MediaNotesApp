package com.example.mediaapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class ReminderWorker extends Worker {
    private static final String CHANNEL_ID = "note_reminder_channel";

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Show the notification
        showNotification();

        // Schedule the next notification in 5 minutes
        scheduleNext();

        return Result.success();
    }

    private void scheduleNext() {
        OneTimeWorkRequest nextRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag("NoteReminder")
                .build();
        
        WorkManager.getInstance(getApplicationContext()).enqueue(nextRequest);
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Note Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Note Reminder")
                .setContentText("Review your saved notes today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}

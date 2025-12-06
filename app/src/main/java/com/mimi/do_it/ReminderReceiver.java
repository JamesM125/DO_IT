package com.mimi.do_it;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String task = intent.getStringExtra("task");
        int taskId = intent.getIntExtra("taskId", (int) System.currentTimeMillis());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "todoReminder")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Task Reminder")
                .setContentText(task)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(taskId, builder.build());
    }
}

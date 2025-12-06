package com.mimi.do_it;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderHelper {

    public static void scheduleReminder(Context context, int taskId, String taskText, long triggerAtMillis) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("task", taskText);
        intent.putExtra("taskId", taskId);

        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                taskId, // requestCode unique per task
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending);
        }
    }

    public static void cancelReminder(Context context, int taskId) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );
        if (pending != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (am != null) am.cancel(pending);
            pending.cancel();
        }
    }
}

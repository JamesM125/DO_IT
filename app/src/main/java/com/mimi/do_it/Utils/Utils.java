package com.mimi.do_it.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mimi.do_it.Model.ToDoModel;
import com.mimi.do_it.ReminderReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static long scheduleAfterInsert(DatabaseHandler db, Context context, ToDoModel task) {
        db.insertTask(task); // task already has task text, status, dueDateMillis
        long taskId = task.getId();

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("task", task.getTask());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getDueDateMillis(), pendingIntent);
        }

        return taskId;
    }

    public static String formatDateTime(long millis, Context context) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(millis));
    }
}

package com.mimi.do_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    long selectedDate = 0;  // Store selected date in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        // When the user selects a date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Convert the selected date into milliseconds
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.set(year, month, dayOfMonth, 0, 0, 0);
            c.set(java.util.Calendar.MILLISECOND, 0);

            selectedDate = c.getTimeInMillis();

            Toast.makeText(this, "Date Selected: " +
                    dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();

            // Create an intent to return the result to the calling activity
            Intent intent = new Intent();
            intent.putExtra("selected_date", selectedDate);
            setResult(RESULT_OK, intent);

            // Finish this activity and return to the previous one
            finish();
        });
    }
}

package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Notification_withDetailPage extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    TimePicker simpleTimePicker;
    CalendarView calendarView;

    DatePicker datePicker;
    String theme = "",message= "";
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_with_detail_page);

        setTitle("Уведомление");

        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");
        message = intent.getStringExtra("message");
        id = intent.getIntExtra("id",0);


        simpleTimePicker = findViewById(R.id.timePicker); // initiate a time picker
        simpleTimePicker.setIs24HourView(true); // set 24 hours mode for the time picker

        datePicker=findViewById(R.id.datePicker);
        /*calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        });*/

    }

    public void createNotification(View view) {

        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, simpleTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, simpleTimePicker.getMinute());
        calendar.set(Calendar.SECOND,0);

        Log.e("DATE2", String.valueOf(System.currentTimeMillis()));
        Log.e("DATE3", String.valueOf(calendar.getTimeInMillis()-System.currentTimeMillis()));

        Intent intent = new Intent(this, com.example.kursa4new.Notification.class);



        intent.putExtra("id", id);
        intent.putExtra("theme", theme);
        intent.putExtra("message", message);

        PendingIntent p1=PendingIntent.getBroadcast(getApplicationContext(),id, intent,0);
        AlarmManager a=(AlarmManager)getSystemService(ALARM_SERVICE);
        a.set(AlarmManager.RTC,calendar.getTimeInMillis(),p1);


        Toast toast = Toast.makeText(getApplicationContext(),
                "Уведомление создано", Toast.LENGTH_SHORT);
        toast.show();


        super.onBackPressed();

    }
}

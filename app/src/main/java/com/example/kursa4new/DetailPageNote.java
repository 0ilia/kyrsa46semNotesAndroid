package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.Sampler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class DetailPageNote extends AppCompatActivity {

    String theme, message;
    int id;
    EditText themeEditText, messageEditText;
    TextView resMess;

    TextView currentDateTime;
    Calendar dateAndTime=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_note);
        themeEditText = findViewById(R.id.theme_Note_EditText_ID);
        messageEditText = findViewById(R.id.message_Note_EditText_ID);

        resMess = findViewById(R.id.resultMessageForDetailPage);
        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");
        message = intent.getStringExtra("message");
        id = intent.getIntExtra("id", 0);

        // Log.e("XXXXXXXXXXXXXX",idString);
        //id = Integer.parseInt(idString);


        themeEditText.setText(theme);
        messageEditText.setText(message);


     /*   currentDateTime=(TextView)findViewById(R.id.currentDateTime);
        setInitialDateTime();*/



    }

/*
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }*/

    public void saveNotes(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("theme", themeEditText.getText().toString());
            object.put("message", messageEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/updateNote/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            resMess.setText(response.getString("update"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });
        requestQueue.add(jsonObjectRequest);

    }

    public void deleteNotes(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/deleteNote/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            resMess.setText(response.getString("delete"));
                            openPageAllNote();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });
        requestQueue.add(jsonObjectRequest);
    }
    public void openPageAllNote() {

        super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_detaipage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.back_in_allNotes_id:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createNotification(View view) {


        Log.e("DDDD", String.valueOf(dateAndTime.getTimeInMillis()));
        Log.e("DDDD1", String.valueOf(System.currentTimeMillis()));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setWhen(/*dateAndTime.getTimeInMillis()*/System.currentTimeMillis())
                .setTicker(themeEditText.getText().toString())
                .setSmallIcon(R.drawable.notificationnote)
                .setContentTitle(themeEditText.getText().toString())
                .setContentText(messageEditText.getText().toString());

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);

    }

  /*  public void setTime(View view) {
        new TimePickerDialog(DetailPageNote.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    public void setDate(View view) {
        new DatePickerDialog(DetailPageNote.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }*/
}

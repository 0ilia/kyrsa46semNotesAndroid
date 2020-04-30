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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class DetailPageNote extends AppCompatActivity {

    String theme = "", message = "", updatedAt = "", createdAt = "",login,function;
    int id = -1, idItem;
    EditText themeEditText, messageEditText;
    TextView resMess;

    Calendar dateAndTime = Calendar.getInstance();

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
        login = intent.getStringExtra("login");
        //    updatedAt = intent.getStringExtra("updatedAt");
        updatedAt = "";
        createdAt = intent.getStringExtra("createdAt");
        id = intent.getIntExtra("id", 0);

        idItem = intent.getIntExtra("idItem", 0);

        Log.e("XXXXXXXXXXXXXX", String.valueOf(updatedAt));
        Log.e("XXXXXXXXXXXXXX", String.valueOf(createdAt));

        //id = Integer.parseInt(idString);
        themeEditText.setText(theme);
        messageEditText.setText(message);
    }


    public void saveNotes(View view) {
//crate Note
        if (id == -1) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            try {
                //input your API parameters
                object.put("login", login);
                object.put("theme", themeEditText.getText().toString());
                object.put("message", messageEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            String url = getString(R.string.URL)+"/addNote/";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("SSSSS", response.toString());
                            try {
                                id = response.getInt("id");
                                updatedAt = response.getString("updatedAt");
                                createdAt = response.getString("createdAt");
                                theme = response.getString("theme");
                                message = response.getString("message");
                                function = "create";
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
        //update
        else {

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
            String url = getString(R.string.URL)+"/updateNote/" + id;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PUT, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                theme = themeEditText.getText().toString();
                                message = messageEditText.getText().toString();

                                updatedAt = response.getString("updatedAt");
                                Log.e("CCCC", updatedAt);
                                function = "update";
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
    }

    public void deleteNotes(View view) {
        if (id!=-1) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            String url = getString(R.string.URL)+"/deleteNote/" + id;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(DELETE, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            function = "delete";
                            openPageAllNote();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void openPageAllNote() {

        // super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra("function", function);
        intent.putExtra("idItem", idItem);
        setResult(1, intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_detaipage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenu = item.getItemId();
        switch (idMenu) {
            case R.id.back_in_allNotes_id:

                Intent intent = new Intent();
                intent.putExtra("function", function);
                intent.putExtra("idItem", idItem);
                intent.putExtra("theme", theme);
                intent.putExtra("message", message);
                intent.putExtra("updatedAt", updatedAt);
                intent.putExtra("createdAt", createdAt);
                intent.putExtra("id", id);
                setResult(1, intent);
                finish();
                //    super.onBackPressed();
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


}

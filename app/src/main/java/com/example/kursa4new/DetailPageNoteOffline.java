package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class DetailPageNoteOffline extends AppCompatActivity {
    String theme = "", message = "", updatedAt = "", createdAt = "", login, function;
    int id = -1, idItem;
    EditText themeEditText, messageEditText;
    TextView resMess;
    ContentValues cv = new ContentValues();
    SQLiteDatabase database;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_note_offline);

        database = dbHelper.getWritableDatabase();

        themeEditText = findViewById(R.id.theme_Note_EditText_IDOffline);
        messageEditText = findViewById(R.id.message_Note_EditText_IDOffline);


        resMess = findViewById(R.id.resultMessageForDetailPageOffline);
        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");
        message = intent.getStringExtra("message");
        login = intent.getStringExtra("login");
        //    updatedAt = intent.getStringExtra("updatedAt");
        updatedAt = "";
        createdAt = "";
        id = intent.getIntExtra("id", 0);

        idItem = intent.getIntExtra("idItem", 0);

        Log.e("XXXXXXXXXXXXXX", String.valueOf(id));
        Log.e("XXXXXXXXXXXXXX", String.valueOf(createdAt));

        themeEditText.setText(theme);
        messageEditText.setText(message);
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        backPage();
    }

    public void backPage() {
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
                backPage();
                return true;
            case R.id.saveNoteMenuId:
                saveNotes();
                return true;
            case R.id.deleteNoteMenuId:
                deleteNotes();
                return true;
            case R.id.notificationNoteMenuId:
                createNotification();
                return true;
            case R.id.saveNoteMenuId_asNew:
                saveNotes_asNew();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNotes_asNew() {
        if (id == -1) {
            creatNote();
        }else {
            if (!themeEditText.getText().toString().equals("") || !messageEditText.getText().toString().equals("")) {

                cv.put("theme", themeEditText.getText().toString());
                cv.put("message", messageEditText.getText().toString());
                cv.put("status", "NoAdd");
                updatedAt = String.valueOf(System.currentTimeMillis());

                cv.put("unix_timeUpdate", updatedAt);
                database.update(DBHelper.TABLE_NAME, cv, "id = ?",
                        new String[]{String.valueOf(id)});
                function = "update";
                theme = themeEditText.getText().toString();
                message = messageEditText.getText().toString();
            }
        }

    }

    public void saveNotes() {
//crate Note
        if (id == -1) {
            creatNote();
        }
        //update
        else {
            if (!themeEditText.getText().toString().equals("") || !messageEditText.getText().toString().equals("")) {

                cv.put("theme", themeEditText.getText().toString());
                cv.put("message", messageEditText.getText().toString());
                updatedAt = String.valueOf(System.currentTimeMillis());

                cv.put("unix_timeUpdate", updatedAt);
                database.update(DBHelper.TABLE_NAME, cv, "id = ?",
                        new String[]{String.valueOf(id)});
                function = "update";
                theme = themeEditText.getText().toString();
                message = messageEditText.getText().toString();
            }
        }
    }

    public void creatNote() {

        if (!themeEditText.getText().toString().equals("") || !messageEditText.getText().toString().equals("")) {
            cv.put(DBHelper._THEME, themeEditText.getText().toString());
            cv.put(DBHelper._MESSAGE, messageEditText.getText().toString());
            cv.put(DBHelper._UNIX_TIMECreate, System.currentTimeMillis());
            cv.put(DBHelper._UNIX_TIMEUpdate, System.currentTimeMillis());
            cv.put(DBHelper._STATUS, "NoAdd");
            database.insert(DBHelper.TABLE_NAME, null, cv);

            long count = DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_NAME);
            database.close();
            Log.e("Count<A", String.valueOf(count));

            id = (int) count;
            updatedAt = String.valueOf(System.currentTimeMillis());
            createdAt = String.valueOf(System.currentTimeMillis());
            theme = themeEditText.getText().toString();
            message = messageEditText.getText().toString();
            function = "create";

            backPage();
        }
    }

    public void deleteNotes() {
        if (id != -1) {
            database.delete(DBHelper.TABLE_NAME, DBHelper._ID + "  = '" + id + "'", null);
            function = "delete";
            openPageAllNote();
/*
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            String url = getString(R.string.URL) + "/deleteNote/" + id;
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
            requestQueue.add(jsonObjectRequest);*/
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


    public void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setWhen(/*dateAndTime.getTimeInMillis()*/System.currentTimeMillis())
                .setTicker(themeEditText.getText().toString())
                .setSmallIcon(R.drawable.notificationnote)
                .setContentTitle(themeEditText.getText().toString())
                .setContentText(messageEditText.getText().toString())
                .setColor(R.color.red)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageEditText.getText().toString()))
                .setLights(23, 300, 100); // To change Light Colors;

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(id, notification);

    }

}

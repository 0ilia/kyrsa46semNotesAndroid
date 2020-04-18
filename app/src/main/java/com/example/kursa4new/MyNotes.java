package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


public class MyNotes extends AppCompatActivity {

    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                while (true) {
                    notes.clear();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject object = new JSONObject();
                    // Enter the correct url for your api service site
                    String url = "http://10.0.2.2:3005/getAllNotes/" + login;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, object,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("CCCC", response.toString());
                                    try {
                                        JSONArray c = response.getJSONArray("notes");
                                        for (int i = 0; i < c.length(); i++) {
//populates the array, in your case, jsonarray size = 4
                                            JSONObject jsonObject = c.getJSONObject(i);

                                            id = jsonObject.getInt("id"); //gets category String
                                            theme = jsonObject.getString("theme"); //gets category String
                                            message = jsonObject.getString("message"); //gets category String
                                            //Log.d("CCCCID", String.valueOf(id));
                                                notes.add(new Note(theme, message, id));
                                            adapter.notifyDataSetChanged();
                                        }
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

                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notes.clear();
        mt = new MyTask();
        mt.execute();
    }

    String login;
    String theme, message;
    int id;
    List<Note> notes = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    MyTask mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);


        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();

        login = intent.getStringExtra("login");

        Log.d("CCCC", login);


        //  setInitialData();


        adapter = new RecyclerViewAdapter(this, notes);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_page_mynotes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addNotesMenu:
                addNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void addNote() {
        //recyclerView.setAdapter(null);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("login", login);
            object.put("theme", "");
            object.put("message", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/addNote/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("SSSSS", response.toString());
                        try {
                            id = response.getInt("id");
                            openDetailNote(id);
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

    public void openDetailNote(int id) {

        Intent intent = new Intent(this, DetailPageNote.class);

        // passing data to the book activity
        intent.putExtra("theme", "");
        intent.putExtra("message", "");
        intent.putExtra("id", id);
        // start the activity
        startActivity(intent);
    }

}

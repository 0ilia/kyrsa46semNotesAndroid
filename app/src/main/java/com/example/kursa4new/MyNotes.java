package com.example.kursa4new;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


public class MyNotes extends AppCompatActivity {
    class MyTaskGetAllNotes extends AsyncTask<Void, Void, Void> {
        String url;
        MyTaskGetAllNotes(String url){
            this.url=url;
        }
        @Override
        protected Void doInBackground(Void... params) {
            notes.clear();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            // Enter the correct url for your api service site
            //String url = "http://10.0.2.2:3005/getAllNotes/" + login;
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
            return null;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();


        adapter = new RecyclerViewAdapter(this, notes);
        recyclerView.setAdapter(adapter);
        mt = new MyTaskGetAllNotes("http://10.0.2.2:3005/getAllNotes/" + login);
        mt.execute();

    }

    String theme, message, login;
    int id;
    ArrayList<Note> notes = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter, filterAdapter;
    MyTaskGetAllNotes mt;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        recyclerView = findViewById(R.id.RecyclerViewId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        search = findViewById(R.id.edittextSearchId);


        search = findViewById(R.id.edittextSearchId);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }


    private void filter(String text) {
        ArrayList<Note> filterList = new ArrayList<>();
        for (Note item : notes) {
            if (item.getTheme().toLowerCase().contains(text.toLowerCase()) ||
                    item.getMessage().toLowerCase().contains(text.toLowerCase())
            ) {
                filterList.add(item);
            }
        }
        filterAdapter = new RecyclerViewAdapter(this, filterList);
        recyclerView.setAdapter(filterAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_page_mynotes, menu);
        return true;
    }

    public class CustomComparator implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.getTheme().compareTo(o2.getTheme());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addNotesMenu:
                addNote();
                return true;
            case R.id.refreshNoteMenuItemID:
                onResume();
                return true;
            case R.id.sortByDateCreate_MenuItemID:
                Collections.reverse(notes);
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.sortByDateUpdate_MenuItemID:
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                mt = new MyTaskGetAllNotes("http://10.0.2.2:3005/sortByDateUpdate/" + login);
                mt.execute();

                return  true;

            case R.id.SortByAlphabetA_ZID:
                Collections.sort(notes, new CustomComparator());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return  true;
            case R.id.SortByAlphabetZ_AID:

                Collections.sort(notes, new CustomComparator());
                Collections.reverse(notes);
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return  true;
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
        intent.putExtra("theme", "");
        intent.putExtra("message", "");
        intent.putExtra("id", id);
        startActivity(intent);
    }


}

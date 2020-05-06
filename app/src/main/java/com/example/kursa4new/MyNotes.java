package com.example.kursa4new;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


public class MyNotes extends AppCompatActivity {


    class MyTaskGetAllNotes extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            notes.clear();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            // Enter the correct url for your api service site
            String url = getString(R.string.URL) + "/getAllNotes/" + login;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String s = response.toString();
                            //  Log.d("CCCC", s);
                            saveToJSON(s);
                            try {
                                JSONArray c = response.getJSONArray("notes");
                                for (int i = 0; i < c.length(); i++) {
//populates the array, in your case, jsonarray size = 4
                                    JSONObject jsonObject = c.getJSONObject(i);

                                    id = jsonObject.getInt("id"); //gets category String
                                    theme = jsonObject.getString("theme"); //gets category String
                                    message = jsonObject.getString("message"); //gets category String
                                    updatedAt = jsonObject.getString("updatedAt"); //gets category String
                                    createdAt = jsonObject.getString("createdAt"); //gets category String
                                    //    Log.d("CCCCID", String.valueOf(unix_timeArray.get(i)));
                                    notes.add(new Note(theme, message, id, updatedAt, createdAt));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data.getStringExtra("function").equals("update")) {
                int updateIndex = data.getIntExtra("idItem", 0);
                int id = data.getIntExtra("id", 0);
                theme = data.getStringExtra("theme");
                message = data.getStringExtra("message");
                updatedAt = data.getStringExtra("updatedAt");

                createdAt = data.getStringExtra("createdAt");

                Log.e("BBBBB", String.valueOf(notes.size()));
                Log.e("BBBBB", String.valueOf(adapter.getItemCount()));
                Log.e("BBBBB", String.valueOf(updateIndex));


                notes.set(updateIndex, new Note(theme, message, id, updatedAt, createdAt));
                adapter.notifyItemChanged(updateIndex);
            /*adapter = new RecyclerViewAdapter(this, notes);
            recyclerView.setAdapter(adapter);*/

            } else if (data.getStringExtra("function").equals("delete")) {
                int removeIndex = data.getIntExtra("idItem", 0);
                notes.remove(removeIndex);
                //adapter.notifyItemRemoved(removeIndex);

                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);

                Log.e("DEL", String.valueOf(adapter.getItemCount()) + "-" + notes.size());
            } else if (data.getStringExtra("function").equals("create")) {

                int id = data.getIntExtra("id", 0);
                theme = data.getStringExtra("theme");
                message = data.getStringExtra("message");
                updatedAt = data.getStringExtra("updatedAt");

                createdAt = data.getStringExtra("createdAt");

                notes.add(0, new Note(theme, message, id, updatedAt, createdAt));
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("Error_onActivityResult", e.getMessage());
        }
//        Log.e("FFFF",data.getStringExtra("name"));
    }


    public void saveToJSON(String json) {
        //Save to external storage
/*
        Log.e("File", String.valueOf(Environment.getExternalStorageDirectory()));

        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/notes_"+R.string.project_id);
        // создаем каталог
        sdPath.mkdirs();

        File sdFile = new File(sdPath, "notes.json");
        if (!sdFile.exists()) {
            try {
                sdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(sdFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);

            bw.close();
            fw.close();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Сохраннено в json", Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
*/

        file = new File(getFilesDir(), "notes.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);

            bw.close();
            fw.close();

            /*Toast toast = Toast.makeText(getApplicationContext(),
                    "Сохраннено в json", Toast.LENGTH_SHORT);
            toast.show();*/
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    SQLiteDatabase database;
    ContentValues cv = new ContentValues();
    DBHelper dbHelper = new DBHelper(this);
    File file;
    String theme, message, login, updatedAt, createdAt, unix_time;
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

        database = dbHelper.getWritableDatabase();
        //load preferences

        //SharedPreferences  sPref = getPreferences(MODE_PRIVATE);
       /* SharedPreferences sPref = getSharedPreferences("com.example.kursa4new", Context.MODE_PRIVATE);
        String savedText = sPref.getString("SAVED_TEXT", "");
        // etText.setText(savedText);
        Toast.makeText(this, savedText, Toast.LENGTH_SHORT).show();

*/

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Name", "");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();*/
        /*if(!name.equalsIgnoreCase(""))
        {
            name = name + "  Sethi";  *//* Edit the value here*//*
        }*/


        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        setTitle(login);

        recyclerView = findViewById(R.id.RecyclerViewId);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this, notes);
        recyclerView.setAdapter(adapter);
        mt = new MyTaskGetAllNotes();
        mt.execute();

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
        if (!text.trim().equals("")) {
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
        } else {
            adapter = new RecyclerViewAdapter(this, notes);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_page_mynotes, menu);
        return true;
    }

    public class CustomComparatorSortByAlphabetA_Z implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.getTheme().toLowerCase().compareTo(o2.getTheme().toLowerCase());
        }
    }

    public class CustomComparatorSortByAlphabetZ_A implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o2.getTheme().toLowerCase().compareTo(o1.getTheme().toLowerCase());
        }
    }

    public class CustomComparatorSortDateUpdate implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o2.getDateUpdate().compareTo(o1.getDateUpdate());
        }
    }

    public class CustomComparatorSortByDateCreate implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o2.getDateCreate().compareTo(o1.getDateCreate());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idMenu = item.getItemId();
        switch (idMenu) {
            case R.id.addNotesMenu:
                addNote();
                return true;
            case R.id.sortByDateCreate_MenuItemID:
                Collections.sort(notes, new CustomComparatorSortByDateCreate());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.sortByDateUpdate_MenuItemID:
                Collections.sort(notes, new CustomComparatorSortDateUpdate());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;

            case R.id.SortByAlphabetA_ZID:
                Collections.sort(notes, new CustomComparatorSortByAlphabetA_Z());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.SortByAlphabetZ_AID:
                Collections.sort(notes, new CustomComparatorSortByAlphabetZ_A());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;

            case R.id.saveToJSONMenuItem_ID:
                mt = new MyTaskGetAllNotes();
                mt.execute();
                return true;
            case R.id.exitToAppMenuItem_ID:
                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("login");
                editor.remove("password");
                editor.remove("checkInternet");
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.synchronizationMenuItem_ID:
                synchronizationlocaldb();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void synchronizationlocaldb() {

        Cursor key = database.rawQuery("SELECT * from " + DBHelper.TABLE_NAME + " where " + DBHelper._STATUS + " = 'NoAdd' ;", null);
        if (key.moveToFirst()) {
            do {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject object = new JSONObject();
                try {
                    //input your API parameters
                    object.put("login", login);
                    object.put("theme", key.getString(1));
                    object.put("message", key.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Enter the correct url for your api service site
                String url = getString(R.string.URL) + "/addNote/";
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
                                    notes.add(0, new Note(theme, message, id, updatedAt, createdAt));
                                    adapter.notifyDataSetChanged();

                                    cv.put("status", "Add");
                                    database.update(DBHelper.TABLE_NAME, cv, "status = ?",
                                            new String[]{"NoAdd"});

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
            while (key.moveToNext());
            key.close();
        }
    }

    void addNote() {

        Intent intent = new Intent(this, DetailPageNote.class);
        intent.putExtra("idItem", adapter.getItemCount() - 1);
        intent.putExtra("login", login);
        intent.putExtra("id", -1);
        startActivityForResult(intent, 1);


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


}

package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.android.volley.Request.Method.GET;

public class MyNotesOffline extends AppCompatActivity {

    class MyTaskGetAllNotesOffline extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Cursor key = database.rawQuery("SELECT * from " + DBHelper.TABLE_NAME + " order by " + DBHelper._UNIX_TIMECreate + " desc ;", null);
            //   String keyValue = "";
            if (key.moveToFirst()) {
                do {
                    Log.e("key.getString(0)", String.valueOf(key.getInt(0)));
                    // keyValue = key.getString(0);
                    // break;
                    notes.add(new Note(key.getString(1), key.getString(1), key.getInt(0), key.getString(4), key.getString(3)));
                    adapter.notifyDataSetChanged();
                }
                while (key.moveToNext());
                key.close();
            }
            return null;
        }
    }

    MyTaskGetAllNotesOffline mt;

    String theme, message, login, updatedAt, createdAt;
    int id;
    ArrayList<Note> notes = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter, filterAdapter;
    EditText search;
    ContentValues cv = new ContentValues();
    SQLiteDatabase database;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_work);
        setTitle("Работа в оффлайне");




        recyclerView = findViewById(R.id.RecyclerViewIdOffline);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this, notes);
        recyclerView.setAdapter(adapter);

        database = dbHelper.getWritableDatabase();
        //Get All Notes Local db
        mt = new MyTaskGetAllNotesOffline();
        mt.execute();

        search = findViewById(R.id.edittextSearchIdOffline);

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
        inflater.inflate(R.menu.menu_mynotes_offline, menu);
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

                notes.add(0,new Note(theme, message, id, updatedAt, createdAt));
                adapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            Log.e("Error_onActivityResult", e.getMessage());
        }
//        Log.e("FFFF",data.getStringExtra("name"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idMenu = item.getItemId();
        switch (idMenu) {
            case R.id.addNotesMenu_offline:
                addNote();
                return true;
            case R.id.sortByDateCreate_MenuItemID_offline:
                Collections.sort(notes, new CustomComparatorSortByDateCreate());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.sortByDateUpdate_MenuItemID_offline:
                Collections.sort(notes, new CustomComparatorSortDateUpdate());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;

            case R.id.SortByAlphabetA_ZID_offline:
                Collections.sort(notes, new CustomComparatorSortByAlphabetA_Z());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.SortByAlphabetZ_AID_offline:
                Collections.sort(notes, new CustomComparatorSortByAlphabetZ_A());
                adapter = new RecyclerViewAdapter(this, notes);
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.gotoMain:
                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("checkInternet");
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    void addNote() {

        Intent intent = new Intent(this, DetailPageNoteOffline.class);
        intent.putExtra("idItem", adapter.getItemCount() - 1);
        intent.putExtra("login", login);
        intent.putExtra("id", -1);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("checkInternet");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
         super.onBackPressed();
        /*Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);*/
    }




}

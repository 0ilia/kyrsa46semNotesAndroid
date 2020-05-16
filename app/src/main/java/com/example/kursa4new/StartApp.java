package com.example.kursa4new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

public class StartApp extends AppCompatActivity {
    ImageView im;
    SharedPreferences preferences;
    boolean register = false;

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // разрешение было предоставлено
                    // выполните здесь необходимые операции для включения функциональности приложения, связанной с запрашиваемым разрешением
                } else {
                    // разрешение не было предоставлено
                    // выполните здесь необходимые операции для выключения функциональности приложения, связанной с запрашиваемым разрешением
                }

                return;
            }
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        try {
            getSupportActionBar().hide();
            im = findViewById(R.id.imageView);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_for_start_app);
            im.startAnimation(anim);



            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);


                    preferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (!preferences.getString("login", "").equals("") &&
                    !preferences.getString("password", "").equals("")) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject object = new JSONObject();
                String url = getString(R.string.URL) + "/loginUser/" + preferences.getString("login", "") + "/" + preferences.getString("password", "");
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    register = response.getBoolean("register");

                                    openMyNotes(register, preferences.getString("login", ""), (preferences.getString("password", "")));
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

            } else {
                openMyNotes(false, "", "");
            }


        } catch (Exception e) {
            Log.e("StartActivityError", e.getMessage());
        }


    }

    public void openMyNotes(boolean register, String login, String password) {
        if (register) {


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.putString("checkInternet", "true");
            editor.apply();


            Intent intent = new Intent(this, MyNotes.class);
            intent.putExtra("login", login);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

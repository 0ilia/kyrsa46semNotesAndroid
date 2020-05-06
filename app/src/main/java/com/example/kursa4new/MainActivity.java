package com.example.kursa4new;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText login, password;

    TextView errorMessage;
    String messageError = "";
    boolean register = false;
    SharedPreferences preferences;
    Button offlineButtonPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        offlineButtonPage = findViewById(R.id.offlineButtonId_auth);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("checkInternet", "").equals("false")) {
            offlineButtonPage.performClick();
        } else if (!preferences.getString("login", "").equals("") &&
                !preferences.getString("password", "").equals("")) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject object = new JSONObject();
            String url = getString(R.string.URL) + "/loginUser/" + preferences.getString("login", "") + "/" + preferences.getString("password", "");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                messageError = response.getString("messageError");
                                register = response.getBoolean("register");
                                errorMessage.setText(messageError);

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

        }
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



        errorMessage = findViewById(R.id.errorMessageLoginForm);
        login = findViewById(R.id.loginPlainTextId_auth);
        password = findViewById(R.id.passwordPlainTextId_auth);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());//звёздочки

    }

   /* @Override
    protected void onResume() {
        super.onResume();

        if(!preferences.getString("login", "").equals("") &&
                !preferences.getString("password", "").equals("")){

            Intent intent = new Intent(this, MyNotes.class);
            intent.putExtra("login", preferences.getString("login", ""));
            startActivity(intent);

        }
    }*/

    public void loginButtonClick_auth(View view) {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
      /*  try {
            //input your API parameters
            object.put("login",login.getText().toString());
            object.put( "password",password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        // Enter the correct url for your api service site
        String url = getString(R.string.URL) + "/loginUser/" + login.getText().toString() + "/" + password.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CCCC", response.toString());
                        try {

                            messageError = response.getString("messageError");
                            register = response.getBoolean("register");
                            errorMessage.setText(messageError);

                            openMyNotes(register, login.getText().toString(), password.getText().toString());
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
        }
    }


    public void registerTextViewClick_auth(View view) {

        Intent intent = new Intent(this, ActivityRegisterForm.class);
        startActivity(intent);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idMenu = item.getItemId();
        switch (idMenu) {
            case R.id.aboutMe_MenuId:
                dialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       /*  alertdialog.setTitle("alertDialog");
         alertdialog.setMessage("Проверка");*/

        builder.setTitle("О разработчике")
                //.setMessage("Мартинкевич Илья")
                .setMessage(getString(R.string.myName) + "\n" + getString(R.string.about))
                .setCancelable(true)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      /*  Toast toast = Toast.makeText(getApplicationContext(),
                                "Нажата  кнопка :ДА", Toast.LENGTH_SHORT);
                        toast.show();*/
                    }
                });
                /*.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ничего не выбранно ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });*/
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void gotoOffline_avtivityClick_auth(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("checkInternet", "false");
        editor.apply();

        Intent intent = new Intent(this, MyNotesOffline.class);
        startActivity(intent);
    }
}

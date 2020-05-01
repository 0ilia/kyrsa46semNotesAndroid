package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

import static com.android.volley.Request.Method.POST;

public class ActivityRegisterForm extends AppCompatActivity {

    EditText login, password, email, confirmPass;
    TextView errorMessage,linkVk;

    String messageError = "";
    boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
/*
         linkVk =  findViewById(R.id.about_TextVieew_Id);
        linkVk.setMovementMethod(LinkMovementMethod.getInstance());*/

Log.e("URL", getString(R.string.URL));
        errorMessage = findViewById(R.id.errorMessageAuthForm);

        login = findViewById(R.id.loginPlainTextId_reg);
        email = findViewById(R.id.emailPlainTextId_reg);
        password = findViewById(R.id.passwordPlainTextId_reg);
        confirmPass = findViewById(R.id.confirmPasswordPlainTextId_reg);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());
        confirmPass.setTransformationMethod(new LockerPasswordTransformationMethod());

//rxjava

    }


    public void registerButtonClick_reg(View view) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
                object.put("login", login.getText().toString());
            object.put("email", email.getText().toString());
            object.put("password", password.getText().toString());
            object.put("confirmPassword", confirmPass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url =getString(R.string.URL)+"/addUser/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            messageError = response.getString("messageError");
                            register = response.getBoolean("register");

                            errorMessage.setText(messageError);
                            openMyNotes(register,login.getText().toString(),password.getText().toString());

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


    public void openMyNotes(boolean register,String login,String password) {
        if (register) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login",login);
            editor.putString("password",password);
            editor.apply();

            Intent intent = new Intent(this, MyNotes.class);
            intent.putExtra("login", this.login.getText().toString());
            startActivity(intent);
        }
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


    public  void  dialog(){

        AlertDialog.Builder builder =new AlertDialog.Builder(ActivityRegisterForm.this);
       /*  alertdialog.setTitle("alertDialog");
         alertdialog.setMessage("Проверка");*/

        builder.setTitle("О разработчике")
                //.setMessage("Мартинкевич Илья")
                .setMessage(getString(R.string.about))
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

}

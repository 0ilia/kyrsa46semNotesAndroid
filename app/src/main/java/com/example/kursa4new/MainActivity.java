package com.example.kursa4new;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {
    EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        login = findViewById(R.id.loginPlainTextId_auth);
        password = findViewById(R.id.passwordPlainTextId_auth);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());//звёздочки


        login.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (login.getText().toString().length() <= 3) {
                    login.setError("Логин должен содержать минимум 4 символа");
                } else {
                    login.setError(null);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length() <= 4) {
                    password.setError("Пароль должен содержать минимум 5 символов");
                } else {
                    password.setError(null);
                }
            }
        });
    }

    public void loginButtonClick_auth(View view) {

        if (login.getText().toString().length() > 3) {
            login.setError(null);
            if (password.getText().toString().length() > 4) {
                password.setError(null);


                //Проверка  есть ли пользователь в системе

                ///add user + hash password
                String passwordhash = BCrypt.hashpw(password.getText().toString(), BCrypt.gensalt());

                //  if ((BCrypt.checkpw(keyChat.getText().toString(), keyValue/*хеш*/))  проверка на соотаветствие
                ConnectMySqlAuth connectMySqlAuth = new ConnectMySqlAuth();


                StringDataMysql stringDataMysql = new StringDataMysql();

                connectMySqlAuth.findUser = "SELECT " + stringDataMysql.login + " , " + stringDataMysql.password + " FROM  " + stringDataMysql.tablenameUsers + " WHERE "
                        + stringDataMysql.login + " = '" + login.getText().toString() + "';";

               /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    if (!connectMySqlAuth.passHash.equals("")) {
                        break;
                    }
                }
                if (!connectMySqlAuth.name.equals("") || !connectMySqlAuth.passHash.equals("")) {
                    if ((BCrypt.checkpw(password.getText().toString(), connectMySqlAuth.passHash))) {
//Входим
                        Intent intent = new Intent(this, MyNotes.class);
                        startActivity(intent);
//Сохранить в SharePre
                    } else {
                        password.setError("Логин или пароль неверный");
                        login.setError("Логин или пароль неверный");
                    }
                } else {
                    password.setError("Логин или пароль неверный");
                    login.setError("Логин или пароль неверный");
                }
/*
                if (connectMySqlRegistration.countUser == 1) {//если логин уже есть
                    login.setError("Логин уже существует");
                } else {
                    connectMySqlRegistration.stringInserAddUsers = "INSERT INTO " + stringDataMysql.tablenameUsers + "" +
                            " (" + stringDataMysql.login + "," + stringDataMysql.email + "," + stringDataMysql.password + ")" +
                            " VALUES ('" + login.getText().toString() + "','" + email.getText().toString() + "','" + passwordhash + "');";
                }
                */

            } else {
                password.setError("Пароль должен содержать минимум 5 символов");

            }
        } else {
            login.setError("Логин должен содержать минимум 4 символа");
        }
    }


    public void registerTextViewClick_auth(View view) {
        Intent intent = new Intent(this, ActivityRegisterForm.class);
        startActivity(intent);
    }
}

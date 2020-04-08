package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mindrot.jbcrypt.BCrypt;

public class ActivityRegisterForm extends AppCompatActivity {

    TextView reg;
    EditText login, password;
    Button auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);






        login = findViewById(R.id.loginPlainTextId);
        password = findViewById(R.id.passwordPlainTextId);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());

        reg = findViewById(R.id.registerButtonId);
        auth = findViewById(R.id.loginButtonId);

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


    public void registerTextViewClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerButtonClick(View view) {

        if (login.getText().toString().length() > 3) {
            login.setError(null);
            if (password.getText().toString().length() > 4) {
                password.setError(null);

                //Проверка  есть ли пользователь в системе

                ///add user + hash password
                String passwordhash = BCrypt.hashpw(password.getText().toString(), BCrypt.gensalt(4));

                //  if ((BCrypt.checkpw(keyChat.getText().toString(), keyValue/*хеш*/))  проверка на соотаветствие
                ConnectMySql connectMySql = new ConnectMySql();

                connectMySql.findOneeUser = "SELECT login  FROM  " + connectMySql.tablenameUsers + " WHERE "
                        + connectMySql.login + " = '" + login.getText().toString() + "';";

                connectMySql.stringInserAddUsers = "INSERT INTO " + connectMySql.tablenameUsers + "" +
                        " (" + connectMySql.login + "," + connectMySql.password + ")" +
                        " VALUES ('" + login.getText().toString() + "','" + passwordhash + "');";


                if (connectMySql.countUser == 1) {//если логин уже есть

                    login.setError("Логин уже существует");
                }
            } else {
                password.setError("Пароль должен содержать минимум 5 символов");

            }
        } else {
            login.setError("Логин должен содержать минимум 4 символа");
        }
    }
}

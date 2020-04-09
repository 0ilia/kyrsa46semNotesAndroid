package com.example.kursa4new;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {
    EditText login, password;

 /*   @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("fieldLogin",login.getText().toString());
        savedInstanceState.putString("fieldPassword",password.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        login.setText( savedInstanceState.getString("fieldLogin"));
        password.setText( savedInstanceState.getString("fieldPassword"));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getSupportActionBar().hide();  скрыть actionbar;


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

                //авторизация

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

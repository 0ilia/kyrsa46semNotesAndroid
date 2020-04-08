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
    TextView reg;
    EditText login, password;
    Button  auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getSupportActionBar().hide();  скрыть actionbar;

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

    public void loginButtonClick(View view) {

    }

    public void registerTextViewClick(View view) {

        Intent intent = new Intent(this, ActivityRegisterForm.class);
        startActivity(intent);


    }
}

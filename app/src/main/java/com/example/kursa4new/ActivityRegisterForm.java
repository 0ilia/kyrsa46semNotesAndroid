package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

public class ActivityRegisterForm extends AppCompatActivity {

    EditText login, password,email,confirmPass;



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("fieldLogin",login.getText().toString());
        savedInstanceState.putString("fieldPassword",login.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);






        login = findViewById(R.id.loginPlainTextId_reg);
        email = findViewById(R.id.emailPlainTextId_reg);
        password = findViewById(R.id.passwordPlainTextId_reg);
        confirmPass = findViewById(R.id.confirmPasswordPlainTextId_reg);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());
        confirmPass.setTransformationMethod(new LockerPasswordTransformationMethod());


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




    public void registerButtonClick_reg(View view) {

        if (login.getText().toString().length() > 3) {
            login.setError(null);
            if (password.getText().toString().length() > 4) {
                password.setError(null);



                //Проверка  есть ли пользователь в системе

                ///add user + hash password
                String passwordhash = BCrypt.hashpw(password.getText().toString(), BCrypt.gensalt(4));

                //  if ((BCrypt.checkpw(keyChat.getText().toString(), keyValue/*хеш*/))  проверка на соотаветствие
                ConnectMySqlRegistration connectMySqlRegistration = new ConnectMySqlRegistration();





                StringDataMysql stringDataMysql = new  StringDataMysql();

                connectMySqlRegistration.findOneeUser = "SELECT login  FROM  " + stringDataMysql.tablenameUsers + " WHERE "
                        + stringDataMysql.login + " = '" + login.getText().toString() + "';";




                if (connectMySqlRegistration.countUser == 1) {//если логин уже есть
                    login.setError("Логин уже существует");



                }else {


                    connectMySqlRegistration.stringInserAddUsers = "INSERT INTO " + stringDataMysql.tablenameUsers + "" +
                            " (" + stringDataMysql.login + "," + stringDataMysql.password + ")" +
                            " VALUES ('" + login.getText().toString() + "','" + passwordhash + "');";
                }

                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/




            } else {
                password.setError("Пароль должен содержать минимум 5 символов");

            }
        } else {
            login.setError("Логин должен содержать минимум 4 символа");
        }
    }

    public void loginTextViewClick_reg(View view) {
        super.onBackPressed();
    }


}

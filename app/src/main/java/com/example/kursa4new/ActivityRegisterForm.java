package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    TextView errorMessage;
    String regex;
    boolean emailBool = false;


    String messageError ="";
    boolean register = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);


        errorMessage = findViewById(R.id.errorMessage);
        regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


        login = findViewById(R.id.loginPlainTextId_reg);
        email = findViewById(R.id.emailPlainTextId_reg);
        password = findViewById(R.id.passwordPlainTextId_reg);
        confirmPass = findViewById(R.id.confirmPasswordPlainTextId_reg);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());
        confirmPass.setTransformationMethod(new LockerPasswordTransformationMethod());

//rxjava

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

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                emailBool = Pattern.matches(regex, email.getText().toString());

                if (!emailBool) {
                    email.setError("Не валидный email");
                } else {
                    email.setError(null);
                }

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
                    if (password.getText().toString().equals(confirmPass.getText().toString())) {
                        confirmPass.setError(null);
                    }
                }
            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!password.getText().toString().equals(confirmPass.getText().toString())) {
                    confirmPass.setError("Пароли не совпадают");
                } else {
                    confirmPass.setError(null);
                }
            }
        });


    }


    public void registerButtonClick_reg(View view) {

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("login",login.getText().toString());
                            object.put("email",email.getText().toString());
                            object.put( "password",password.getText().toString());
                            object.put( "confirmPassword",confirmPass.getText().toString());
                            object.put( "cookie","d");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "http://10.0.2.2:3005/users/";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("CCCC",response.toString());
                                        try {

                                             messageError = response.getString("messageError");
                                             register = response.getBoolean("register");

                                                errorMessage.setText(messageError);


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


/*
        if(register){
            Intent intent = new Intent(this, MyNotes.class);
            startActivity(intent);
        }*/
    }



}

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


    String messageError = "";
    boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);


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
            object.put("cookie", "d");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/addUser/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            messageError = response.getString("messageError");
                            register = response.getBoolean("register");

                            errorMessage.setText(messageError);
                            openMyNotes(register);

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


    public void openMyNotes(boolean register) {
        if (register) {
            Intent intent = new Intent(this, MyNotes.class);
            startActivity(intent);
        }
    }

}

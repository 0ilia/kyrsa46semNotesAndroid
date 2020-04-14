package com.example.kursa4new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

    TextView errorMessage;
    String messageError ="";
    boolean register = false;
    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        errorMessage = findViewById(R.id.errorMessageLoginForm);

        login = findViewById(R.id.loginPlainTextId_auth);
        password = findViewById(R.id.passwordPlainTextId_auth);
        password.setTransformationMethod(new LockerPasswordTransformationMethod());//звёздочки




    }

    public void loginButtonClick_auth(View view) {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("login",login.getText().toString());
            object.put( "password",password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/loginUser/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CCCC",response.toString());
                        try {

                            messageError = response.getString("messageError");
                            register = response.getBoolean("register");

                            errorMessage.setText(messageError);

                            openMyNotes(register ,login.getText().toString());
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
public  void openMyNotes(boolean register,String login){
     if(register){




         Intent intent = new Intent(this, MyNotes.class);
         startActivity(intent);
     }
}


    public void registerTextViewClick_auth(View view) {




        Intent intent = new Intent(this, ActivityRegisterForm.class);
        startActivity(intent);
    }
}

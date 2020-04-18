package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class DetailPageNote extends AppCompatActivity {

    String theme, message;
    int id;
    EditText themeEditText, messageEditText;
    TextView resMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_note);
        themeEditText = findViewById(R.id.theme_Note_EditText_ID);
        messageEditText = findViewById(R.id.message_Note_EditText_ID);

        resMess = findViewById(R.id.resultMessageForDetailPage);
        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");
        message = intent.getStringExtra("message");
        id = intent.getIntExtra("id", 0);

        // Log.e("XXXXXXXXXXXXXX",idString);
        //id = Integer.parseInt(idString);


        themeEditText.setText(theme);
        messageEditText.setText(message);

    }

    public void saveNotes(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
       /* try {
            //input your API parameters
            object.put("id", id);
            object.put("theme", themeEditText.getText().toString());
            object.put("message", messageEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/updateNote/"+themeEditText.getText().toString()+"/"+
                messageEditText.getText().toString()+"/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            resMess.setText(response.getString("update"));
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

    public void deleteNotes(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:3005/deleteNote/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {

                            resMess.setText(response.getString("delete"));
                            openPageAllNote();
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

    public void openPageAllNote() {
        super.onBackPressed();

    }
}

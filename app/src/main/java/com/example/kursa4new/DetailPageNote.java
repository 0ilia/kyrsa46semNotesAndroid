package com.example.kursa4new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class DetailPageNote extends AppCompatActivity {

    String theme, message;
    int id;
    EditText themeEditText, messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_note);
        themeEditText = findViewById(R.id.theme_Note_EditText_ID);
        messageEditText = findViewById(R.id.message_Note_EditText_ID);


        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");
        message = intent.getStringExtra("message");
        id = intent.getIntExtra("id",0);

       // Log.e("XXXXXXXXXXXXXX",idString);
        //id = Integer.parseInt(idString);


        themeEditText.setText(theme);
        messageEditText.setText(message);

    }
}

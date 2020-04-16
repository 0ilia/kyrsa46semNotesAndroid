package com.example.kursa4new;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.POST;

public class DialogAddNote extends AppCompatDialogFragment {
    EditText theme, message;
    String login;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null );

        theme = view.findViewById(R.id.theme_dialog_id);
        message = view.findViewById(R.id.message_dialog_id);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add_note, null))
       .setTitle("Добавление заметки")

        // Add action buttons
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int id) {


                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("login", login);
                            object.put("theme", theme.getText().toString());
                            object.put("message", message.getText().toString());
                            Log.d("CCCC",  theme.getText().toString());
                            Log.d("CCCC",  message.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "http://10.0.2.2:3005/addNote/";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("CCCC", response.toString());
                                       /* try {

                                            messageError = response.getString("messageError");
                                            register = response.getBoolean("register");

                                            errorMessage.setText(messageError);

                                            openMyNotes(register ,login.getText().toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }*/
                                    }

                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        Log.d("CCCC", String.valueOf(theme));

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogAddNote.this.getDialog().cancel();
                    }
                });



        return builder.create();
    }



}

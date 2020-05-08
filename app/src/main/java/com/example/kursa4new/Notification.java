package com.example.kursa4new;

import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class Notification  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create the content intent for the notification, which launches this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        int id = intent.getIntExtra("id", 0);
        String theme = intent.getStringExtra("theme");
        String message = intent.getStringExtra("message");

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, id, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentPendingIntent)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.COLOR_DEFAULT)

        .setWhen(/*dateAndTime.getTimeInMillis()*/System.currentTimeMillis())
                .setTicker(theme)
                .setSmallIcon(R.drawable.notificationnote)
                .setContentTitle(theme)
                .setContentText(message)
                /*  .setContentIntent(resultPendingIntent)*/
                .setColor(R.color.red)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLights(23, 300, 100); // To change Light Colors;
        //Deliver the notification

        notificationManager.notify(id, builder.build());



    }
}

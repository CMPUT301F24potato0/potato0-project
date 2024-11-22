package com.example.eventlottery;


import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.credentials.Action;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Objects;

public class PushNotificationService extends FirebaseMessagingService{

    // https://medium.com/@Codeible/android-notifications-with-firebase-cloud-messaging-914623716dea
    FirebaseFirestore db;
    /**
     * @param remoteMessage Remote message that has been received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TESTING

        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

//        String SignUP = remoteMessage.getData().get("SignUP");
//        String eventID = "";
//        if(Objects.equals(SignUP, "true")) {
//            eventID = remoteMessage.getData().get("eventID");
//        }



        String topic = remoteMessage.getFrom();

//        String topic = remoteMessage.getFrom().substring(8).replace("_"," ");

//        String check = "signup";
        Log.d("Recieved notification", title);
        Log.d("Recieved notification", text);
        Log.d("Topic",topic);
//        if(eventID != ""){
//            Log.d("eventId",eventID);
//        }



        // TESTING

        final String channel_id = "notification_popup";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Boolean ismuted = ProfileFragment.getIsmute();
            Log.e("Ismuted", String.valueOf(ismuted));

            NotificationChannel channel = new NotificationChannel(
                    channel_id,
                    "Heads Up Notification", // We can put event name here
                    NotificationManager.IMPORTANCE_HIGH);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);

            NotificationCompat.Builder notification;


            notification = new NotificationCompat.Builder(
                    this, channel_id)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }



            if(!ismuted) {
                Log.e("Ismuted??", ""+ false);
                // https://www.youtube.com/watch?v=Hj8mjY4znEo&ab_channel=TechnicalSkillz

                // TEST THIS MORE
                // SHOULD BE ABLE TO OPEN NOTIFICATION FRAGMENT

                // CAlls activity when notification pressed
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("redirect","NotificationsFragment");
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                notificationIntent.putExtra("eventID", eventID);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                notification.setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1, notification.build());

//                NotificationManagerCompat.from(this).notify(1, notification.build());
            }

            super.onMessageReceived(remoteMessage);
        }
    }
}
package com.example.eventlottery.Notifications;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eventlottery.MainActivity;
import com.example.eventlottery.Entrant.ProfileFragment;
import com.example.eventlottery.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This class is the PushNotificationService
 * This class is used to receive notifications from the Firebase
 * This overrides the onMessageReceived method to receive notifications from the Firebase
 */
public class PushNotificationService extends FirebaseMessagingService{

    // https://medium.com/@Codeible/android-notifications-with-firebase-cloud-messaging-914623716dea
    FirebaseFirestore db;
    /**
     * Here we are overriding the onMessageReceived method to receive notifications from the Firebase
     * This sends the notification to the user
     * @param remoteMessage Remote message that has been received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();



        String topic = remoteMessage.getFrom();


        Log.d("Recieved notification", title);
        Log.d("Recieved notification", text);
        Log.d("Topic",topic);


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
                    .setSmallIcon(R.drawable.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round))
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setAutoCancel(true);


            if(!ismuted) {
                Log.e("Ismuted??", ""+ false);
                // https://www.youtube.com/watch?v=Hj8mjY4znEo&ab_channel=TechnicalSkillz


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
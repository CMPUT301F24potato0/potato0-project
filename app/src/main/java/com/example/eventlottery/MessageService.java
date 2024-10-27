package com.example.eventlottery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class MessageService extends FirebaseMessagingService {

    private NotificationManager notificationManager;


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("Token","updated");
        updateNewToken(token);
        // here we need to update DB when new token is updated
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
//        Map<String, String> data = message.getData();

        //optional vibration

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");

        Intent resultIntent = new Intent(this, MainActivity.class); // redirect to mainActivity

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this,1, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setPriority(Notification.PRIORITY_MAX);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = "Notification";
            NotificationChannel channel = new NotificationChannel(
                    channelID, "Coding", NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.canBypassDnd();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                channel.canBubble();
            }

            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);

        }
        notificationManager.notify(100,builder.build());

    }
    private void updateNewToken(String token){
        // update new refreshed token to db
    }
}

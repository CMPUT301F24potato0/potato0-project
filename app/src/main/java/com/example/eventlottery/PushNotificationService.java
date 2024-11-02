package com.example.eventlottery;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.credentials.Action;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    /**
     * @param remoteMessage Remote message that has been received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TESTING
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

        Log.d("Recieved notification", title);
        Log.d("Recieved notification", text);
        // TESTING

        final String channel_id = "notification_popup";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channel_id,
                    "Heads Up Notification", // We can put event name here
                    NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);

//
            Intent action1 = new Intent(this,MyBroadcastReceiver.class);
            action1.setAction("first");
            PendingIntent pendingaction1 = PendingIntent.getBroadcast(this,1,action1,PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder
                    (R.mipmap.ic_notifications, "Text",pendingaction1).build();

            Intent action2 = new Intent(this,MyBroadcastReceiver.class);
            action2.setAction("second");
            PendingIntent pendingaction2 = PendingIntent.getBroadcast(this,1,action2,PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Action action2_2 = new NotificationCompat.Action.Builder
                    (R.mipmap.ic_notifications, "Text2",pendingaction2).build();


            NotificationCompat.Builder notification = new NotificationCompat.Builder(
                    this, channel_id)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .addAction(action)
                    .addAction(action2_2)
                    .setAutoCancel(true);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            NotificationManagerCompat.from(this).notify(1,notification.build());
        }

        super.onMessageReceived(remoteMessage);
    }
}

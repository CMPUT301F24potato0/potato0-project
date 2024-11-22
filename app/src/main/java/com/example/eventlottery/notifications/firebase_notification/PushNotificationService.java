package com.example.eventlottery.notifications.firebase_notification;


import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This class is the push notification service from firebase
 * Not being used right now
 */
public class PushNotificationService extends FirebaseMessagingService {

    // https://medium.com/@Codeible/android-notifications-with-firebase-cloud-messaging-914623716dea
    FirebaseFirestore db;
    /**
     * @param remoteMessage Remote message that has been received.
     */
    public static final String TAG  = "PushNotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "Inside the onMessageReceived");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}

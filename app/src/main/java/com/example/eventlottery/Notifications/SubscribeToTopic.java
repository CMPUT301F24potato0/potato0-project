package com.example.eventlottery.Notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class Subscribes the user to a topic
 * This is required for sending notifications
 */
public class SubscribeToTopic {

    //https://firebase.google.com/docs/cloud-messaging/android/topic-messaging


    private final String topic;
    private final Context context;

    /**
     * This is the constructor for SubscribeToTopic
     * @param topic The topic to subscribe to
     * @param context The context
     */
    public SubscribeToTopic(String topic, Context context){
        this.topic = topic;
        this.context = context;
    }

    /**
     * This function subscribes to the topic
     */
    public void subscribe(){

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("Notification subscription", msg);
                        Log.d("Topic",topic);
                    }
                });
    }

}

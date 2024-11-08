package com.example.eventlottery;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class is the SubscribeToTopic
 * Not being used
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
    SubscribeToTopic(String topic, Context context){
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

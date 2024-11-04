package com.example.eventlottery;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class UnsubscribeFromTopic {

    private final String topic;
    private final Context context;

    UnsubscribeFromTopic(String topic, Context context){
        this.topic = topic;
        this.context = context;
    }

    public void unsubscribe(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Unsubscribed";
                        if (!task.isSuccessful()) {
                            msg = "Unsubscribe failed";
                        }
                        Log.d("Notification subscription", msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}

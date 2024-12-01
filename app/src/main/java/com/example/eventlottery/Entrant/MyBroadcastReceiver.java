package com.example.eventlottery.Entrant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

/**
 * This is the MyBroadcastReceiver class
 * This class handles the actions from notifications
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    /**
     * On receive
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Notification","Button");
        String action = intent.getAction();
        if(action.equals("first")){
            action1(context);
        }
        else if(action.equals("second")){
            action2(context);
        }
        NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();

    }
    public void action1(Context context){ //Cancel
        Toast.makeText(context ,"Cancel",Toast.LENGTH_LONG).show();
    }
    public void action2(Context context){ // Sign up
        Toast.makeText(context ,"Signed up",Toast.LENGTH_LONG).show();

    }
}


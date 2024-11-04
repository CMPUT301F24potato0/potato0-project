package com.example.eventlottery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Notification","Button");
        String action = intent.getAction();
//        String action_btn_2 = intent.getStringExtra("second");
        if(action.equals("first")){
            action1(context);
        }
        else if(action.equals("second")){
            action2(context);
        }
        NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();

    }
    public void action1(Context context){
        Toast.makeText(context ,"Action 1",Toast.LENGTH_LONG).show();
    }
    public void action2(Context context){
        Toast.makeText(context ,"Action 2",Toast.LENGTH_LONG).show();
    }
}


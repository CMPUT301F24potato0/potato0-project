package com.example.eventlottery;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendNotification {

    private String title;
    private String body;
    private String topic;
    private Context context;

    public  SendNotification(Context context){
        this.context = context;
    }
    public void setTitlesetBody(String title, String body){
        this.title = title;
        this.body = body;

    }
    public void popup(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.notification_pop_up);
        Button cancel = (Button) dialog.findViewById(R.id.Cancel_id);
        Button send = (Button) dialog.findViewById(R.id.send_id);
        EditText title = (EditText) dialog.findViewById(R.id.title);
        EditText body = (EditText) dialog.findViewById(R.id.body);


        this.topic = "testTopic";

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitlesetBody(title.getText().toString(),body.getText().toString());
                NotificationCreate();
                Toast.makeText(context ,"Sent",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });



        dialog.show();

    }


//    public SendNotification(String title, String body, String topic, Context context) {
//        this.title = title;
//        this.body = body;
//        this.topic = topic;
//        this.context = context;
//    }

    public void NotificationCreate (){

        FcmNotificationSender fcmNotificationSender = new FcmNotificationSender(
                title,
                body,
                context,
                topic
        );
        fcmNotificationSender.SendNotifications();

    }
}

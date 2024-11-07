package com.example.eventlottery;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class SendNotification implements Serializable {

    private String title;
    private String body;
    private String topic;
    private Context context;
    private String eventID;
    private Boolean SignUP;
    private ArrayList<String> title_text;


    public  SendNotification(Context context, String eventID, Boolean SignUP){
        this.context = context;
        this.eventID = eventID;
        this.SignUP = SignUP;
    }

    public ArrayList<String> getArray(){
        this.title_text.add(this.title);
        this.title_text.add(this.body);
        return title_text;
    }
//    public void setArrayList(){
//        this.title_text.add(this.title);
//        this.title_text.add(this.body);
//    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setBody(String body){
        this.body = body;
    }


//    public void setTitlesetBody(String title, String body){
//        this.title = title;
//        this.body = body;
//
//    }
//    public ArrayList<String> popup(){
//    public void popup(){
//        ArrayList<String> title_text = new ArrayList<>();
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.notification_pop_up);
//        Button cancel = (Button) dialog.findViewById(R.id.Cancel_id);
//        Button send = (Button) dialog.findViewById(R.id.send_id);
//        EditText title = (EditText) dialog.findViewById(R.id.title);
//        EditText body = (EditText) dialog.findViewById(R.id.body);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                setTitlesetBody(title.getText().toString(),body.getText().toString());
////                NotificationCreate();
//                title_text.add(title.getText().toString());
//                title_text.add(body.getText().toString());
//
//                Toast.makeText(context ,"Sent",Toast.LENGTH_LONG).show();
//
//                dialog.dismiss();
//
//            }
//        });
//
//        dialog.show();
//
//
//    }



    public void NotificationCreate (String title, String body, String topic){

        FcmNotificationSender fcmNotificationSender = new FcmNotificationSender(
                title,
                body,
                context,
                topic,
                eventID,
                SignUP
        );
        fcmNotificationSender.SendNotifications();

    }
}

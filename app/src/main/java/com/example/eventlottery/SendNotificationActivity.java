package com.example.eventlottery;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class SendNotificationActivity extends AppCompatActivity  {

    private Button send;
    private EditText title;
    private EditText message;


    private String title_text;
    private String body_text;
    private EventModel event;
    private ArrayList<UsersList> usersLists;
    private String eventId;
    private SendNotification sendNotification;
    private boolean click;
    private int temp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            event = (EventModel) extra.getSerializable("event");
            if(event != null){
                usersLists = event.getWaitingList();
                eventId = event.getEventID();
            }
            temp = (int) extra.getInt("bool");
            if(temp == 0){
                click = false;
            }
            else{
                click = true;
            }
            flag = extra.getString("flag");
        }

        sendNotification = new SendNotification(getApplicationContext(),eventId,click, db);
        send = findViewById(R.id.send_id);
        title = findViewById(R.id.title_id);
        message = findViewById(R.id.message_id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_text = title.getText().toString();
                body_text = message.getText().toString();
                send();
                finish();
            }
        });
    }

    public void send(){
        for(int i = 0; i < usersLists.size(); i++){
            sendNotification.NotificationCreate(title_text, body_text, usersLists.get(i).getiD(), flag);
        }
    }
}
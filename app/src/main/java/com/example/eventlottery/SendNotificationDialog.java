package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class SendNotificationDialog extends DialogFragment {
    private EditText title;
    private EditText message;
//    private Button send;
    private EventModel event;
    private String flag;
    private ArrayList<UsersList> usersLists;
    private String eventID;
    private SendNotification sendNotification;
    private Boolean sent;
    private FirebaseFirestore db;
    private String title_text;
    private String body_text;
    private ChosenListActivity chosenListActivity;

    public SendNotificationDialog(EventModel event, String flag, Boolean sent, FirebaseFirestore db, ChosenListActivity chosenListActivity){
        this.event = event;
        this.flag = flag;
        switch(flag) {
            case "Waitlist":    usersLists = event.getWaitingList();
                break;
            case "Chosen":      usersLists = event.getInvitedList();
                break;
            case "Cancelled":   usersLists = event.getCancelledList();
                break;
            case "Enrolled":    usersLists = event.getEnrolledList();
                break;
            case "Invited":     usersLists = event.getInvitedList();
                break;
        }
        this.eventID = event.getEventID();
        this.sent = sent;
        this.db = db;
        this.chosenListActivity = chosenListActivity;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_send_notification, null);

        title = view.findViewById(R.id.title_id);
        message = view.findViewById(R.id.message_id);
//        send = view.findViewById(R.id.send_id);
//        send.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Send Notification")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send", (dialog, which) -> {
                    title_text = title.getText().toString();
                    body_text = message.getText().toString();
                    sendNotification = new SendNotification(getContext(),eventID, sent, db);
                    send();
                    sent = true;
                    db.collection("events").document(event.getEventID()).set(event);
                    chosenListActivity.sendNotification();
                })
                .create();

    }
    public void send(){
        for(int i = 0; i < usersLists.size(); i++){
            sendNotification.NotificationCreate(title_text, body_text, usersLists.get(i).getiD(), flag);
        }
    }
}

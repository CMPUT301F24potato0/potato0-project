package com.example.eventlottery.Notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.Organizer.ChosenListActivity;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.R;
import com.example.eventlottery.Models.RemoteUserRef;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * This class is the SendNotificationDialog
 * This was taken after Maxim implemented SendNotificationActivity
 */
public class SendNotificationDialog extends DialogFragment {
    private EditText title;
    private EditText message;
    private EventModel event;
    private String flag;
    private ArrayList<RemoteUserRef> remoteUserRefs;
    private String eventID;
    private SendNotification sendNotification;
    private Boolean sent;
    private FirebaseFirestore db;
    private String title_text;
    private String body_text;

    private ChosenListActivity chosenListActivity = null;
    /**
     * Constructor for SendNotificationDialog
     * @param event The event
     * @param flag The flag indicating which list the organizer is sending a notification from
     * @param sent Boolean flag for sending notifications
     * @param db The database
     */
    public SendNotificationDialog(EventModel event, String flag, Boolean sent, FirebaseFirestore db){
        this.event = event;
        this.flag = flag;
        switch(flag) {
            case "Waitlist":    remoteUserRefs = event.getWaitingList();
                break;
            case "Chosen":      remoteUserRefs = event.getChosenList();
                break;
            case "Cancelled":   remoteUserRefs = event.getCancelledList();
                break;
            case "Enrolled":    remoteUserRefs = event.getEnrolledList();
                break;
            case "Invited":     remoteUserRefs = event.getInvitedList();
                break;
        }
        this.eventID = event.getEventID();
        this.sent = sent;
        this.db = db;
    }

    /**
     * Constructor for SendNotificationDialog
     * @param event The event
     * @param flag The flag indicating which list the organizer is sending a notification from
     * @param sent Boolean flag for sending notifications
     * @param db The database
     * @param chosenListActivity A reference to chosenListActivity
     */
    public SendNotificationDialog(EventModel event, String flag, Boolean sent, FirebaseFirestore db, ChosenListActivity chosenListActivity){
        this(event, flag, sent, db);
        this.chosenListActivity = chosenListActivity;
    }

    /**
     * Empty constructor
     */
    public SendNotificationDialog(){
        super();
    }

    /**
     * on create
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog after being created
     */

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_send_notification, null);
        title = view.findViewById(R.id.title_id);
        message = view.findViewById(R.id.message_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    if (flag.equals("Chosen")) {
                        Toast.makeText(chosenListActivity, "Invitations have not been sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Send", (dialog, which) -> {
                    title_text = title.getText().toString();
                    body_text = message.getText().toString();
                    sendNotification = new SendNotification(getContext(),eventID, sent, db);
                    send();
                    sent = true;
                    db.collection("events").document(event.getEventID()).set(event);
                    if (flag.equals("Chosen")) {
                        chosenListActivity.sendNotification();
                    }
                })
                .create();

    }

    /**
     * This function sends the notification
     */
    public void send(){
        for(int i = 0; i < remoteUserRefs.size(); i++){
            String topic = event.getEventID() + "_" + remoteUserRefs.get(i).getiD();
            sendNotification.NotificationCreate(title_text, body_text, remoteUserRefs.get(i).getiD(), flag, topic);
        }
    }
}

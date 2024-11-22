package com.example.eventlottery;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the SendNotification
 */
public class SendNotification implements Serializable {

    private String title;
    private String body;
    private Context context;
    private String eventID;
    private Boolean SignUP;
    private String topic;
    private ArrayList<String> title_text;
    private FirebaseFirestore db;
    private CurrentUser tempCurUser;


    /**
     * Constructor for SendNotification
     * @param context The context
     * @param eventID The event's ID
     * @param SignUP Boolean flag for sign up
     * @param db The database
     */
    public  SendNotification(Context context, String eventID, Boolean SignUP, FirebaseFirestore db){
        this.context = context;
        this.eventID = eventID;
        this.SignUP = SignUP;
        this.db = db;
        this.topic = topic;
    }

    /**
     * This function gets the array of the title and body
     * @return title_text
     */
    public ArrayList<String> getArray(){
        this.title_text.add(this.title);
        this.title_text.add(this.body);
        return title_text;
    }

    /**
     * This function sets the title and body
     * @param title The title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * This function sets the body
     * @param body The body
     */
    public void setBody(String body){
        this.body = body;
    }

    /**
     * This function sends the notification
     * @param title The title
     * @param body The body
     * @param id The id
     * @param flag The flag
     */
    public void NotificationCreate (String title, String body, String id, String flag, String topic){

        Task<DocumentSnapshot> docRef = db.collection("users").document(id).get();
        docRef.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tempCurUser = documentSnapshot.toObject(CurrentUser.class);
                    HashMap<String,String> notification = new HashMap<String,String>();
                    notification.put("title",title);
                    notification.put("body",body);
                    notification.put("eventID",eventID);
                    notification.put("flag",flag);
                    tempCurUser.addNotifications(notification);
                    db.collection("users").document(id).set(tempCurUser);
                }
            }
        });
        Tasks.whenAllComplete(docRef);



        // ****************************************************************************************
        FcmNotificationSender fcmNotificationSender = new FcmNotificationSender(
                title,
                body,
                context,
                topic,
                eventID,
                false

        );
        fcmNotificationSender.SendNotifications();
    }
}

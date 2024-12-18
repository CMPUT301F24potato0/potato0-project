package com.example.eventlottery.Models;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreKt;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This Class stores the current user's information
 * This class represent a User on the app, it contains all the information of the user, it name,
 * phone, email, profile picture and if the user has a facility where they can make events,
 * it also check if the user has notification on/off
 * This class provides setter and getter for the attributes of this class
 */
public class UserModel implements Serializable {
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private boolean isAdmin;
    private String facilityID;
    private String iD;
    private String userPhoto;
    private boolean isMuted = false;
    private ArrayList<String> topics;
    private ArrayList<HashMap<String, String>> notifications;

    /**
     * Constructor that updates the local variables
     * @param fName First Name
     * @param lName Last Name
     * @param email Email Address
     * @param isAdmin Admin Status
     * @param phone Phone number
     * @param iD Android ID
     */
    public UserModel(String fName, String lName,
                     String email, String phone,
                     boolean isAdmin, String facilityID,
                     String iD, boolean isMuted,
                     ArrayList<String> topics) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.facilityID = facilityID;
        this.phone = phone;
        this.iD = iD;
        this.isMuted = isMuted;
        this.topics = topics;
    }

    /**
     * Constructor for UserModel
     * @param fName First name
     * @param lName Last name
     * @param email Email
     * @param phone Phone number
     * @param isAdmin Boolean flag if the user is an admin
     * @param facilityID The user's facility ID
     * @param iD The user's unique device ID
     * @param isMuted Boolean flag for muted notifications
     * @param topics Topics the user is subscribed to
     * @param notifications Notifications sent to the user
     */
    public UserModel(String fName, String lName,
                     String email, String phone,
                     boolean isAdmin, String facilityID,
                     String iD, boolean isMuted,
                     ArrayList<String> topics, ArrayList<HashMap<String, String>> notifications) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.facilityID = facilityID;
        this.phone = phone;
        this.iD = iD;
        this.isMuted = isMuted;
        this.topics = topics;
        this.notifications = notifications;
    }

    /**
     * Empty constructor
     */
    public UserModel() {
        this.fName = "";
        this.lName = "";
        this.email = "";
        this.isAdmin = false;
        this.facilityID = "";
        this.phone = "";
        this.iD = "";
        this.isMuted = false;
        this.topics = new ArrayList<String>();
    }

    /**
     * Getting First name
     * @return First name
     */
    public String getfName(){
        return fName;
    }
    /**
     * Getting Last name
     * @return Last name
     */
    public String getlName(){
        return lName;
    }
    /**
     * Getting phone number
     * @return phone number as String
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Getting Email address
     * @return Email address
     */
    public String getEmail(){
        return email;
    }
    /**
     * Getting Admin status
     * @return Admin status
     */
    public boolean getIsAdmin(){
        return isAdmin;
    }
    /**
     * Getting Android ID
     * @return Android's ID
     */
    public String getiD() {
        return iD;
    }
    /**
     * Setting First name
     */
    public void setfName(String fName) {
        this.fName = fName;
    }
    /**
     * Setting Last name
     */
    public void setlName(String lName) {
        this.lName = lName;
    }
    /**
     * Setting email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Setting phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Setting Admin Status
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Getting Facility ID
     * @return Facility ID
     */
    public String getFacilityID() {
        return facilityID;
    }

    /**
     * Setting Facility ID
     * @param facilityID Facility ID
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * Getting admin
     * @return admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Setting admin
     * @param admin admin boolean
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Getting Android ID
     * @param iD Android ID
     */
    public void setiD(String iD) {
        this.iD = iD;
    }

    /**
     * Getting user photo
     * @return user photo
     */
    public String getUserPhoto() {
        return userPhoto;
    }

    /**
     * Setting user photo
     * @param userPhoto user photo
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * Getting muted
     * @return muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Setting muted
     * @param muted muted boolean
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    /**
     * Getting topics
     * @return topics
     */
    public ArrayList<String> getTopics() {
        return topics;
    }

    /**
     * Setting topics
     * @param topics topics
     */
    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    /**
     * Adding topics
     * @param topic topic
     */
    public void addTopics(String topic) {
        for (String t : topics) {
            if (t.equals(topic)) {
                return;
            }
        }
        this.topics.add(topic);
    }

    /**
     * Removing topics
     * @param topic topic
     */
    public void removeTopics(String topic){
        for (String t : topics) {
            if (t.equals(topic)) {
                this.topics.remove(topic);
                return;
            }
        }
    }

    /**
     * Getting notifications
     * @return notifications
     */
    public ArrayList<HashMap<String, String>> getNotifications() {
        return notifications;
    }

    /**
     * Setting notifications
     * @param notifications notifications
     */
    public void setNotifications(ArrayList<HashMap<String, String>> notifications) {
        this.notifications = notifications;
    }

    /**
     * Adding notifications
     * @param notification hash map that contains the notification
     */

    public void addNotifications(HashMap<String, String> notification) {
        this.notifications.add(notification);
    }

    /**
     * Removing notifications
     * @param notification notification to be removed
     */
    public void removeNotifications(HashMap<String, String> notification){
        Boolean notFinished = Boolean.TRUE;
        while (notFinished) {
            notFinished = notifications.remove(notification);
        }
//        for (HashMap<String, String> n : notifications) {
//            if (n.equals(notification)) {
//                this.notifications.remove(notification);
//            }
//        }
    }

    /**
     * Deletes the user from firebase, taking care to also delete associated facilities and events,
     * as well as removing the user from events they joined
     */
    public void delete(FirebaseFirestore db) {
        db.collection("facilities").document(getiD()).get().addOnCompleteListener((Task<DocumentSnapshot> facilityTask) -> {
            FacilityModel facility = facilityTask.getResult().toObject(FacilityModel.class);
            removeFromEvents(db);
            if (facility == null) return;
            facility.delete(db);

        });
        db.collection("photos").document(getiD()).delete();
        db.collection("users").document(getiD()).delete();

    }

    /**
     * A helper function that removes the user from all events they joined
     * @param db
     */
    private void removeFromEvents(FirebaseFirestore db) {
        db.collection("events").whereArrayContains("entrantIDs", getiD()).get().addOnCompleteListener((Task<QuerySnapshot> eventTask) -> {
            if (eventTask.isSuccessful()) {
                for (QueryDocumentSnapshot eventDocSnapshot : eventTask.getResult()) {
                    EventModel event = eventDocSnapshot.toObject(EventModel.class);
                    RemoteUserRef tempRemoteUserRef = new RemoteUserRef(getiD(), getfName() + " " + getlName());
                    List<ArrayList<RemoteUserRef>> eventArrayLists = Arrays.asList(
                            event.getWaitingList(),
                            event.getChosenList(),
                            event.getCancelledList(),
                            event.getEnrolledList(),
                            event.getInvitedList()
                    );
                    for (ArrayList<RemoteUserRef> currentArrayList : eventArrayLists) {
                        currentArrayList.remove(tempRemoteUserRef);  // removes a different RemoteUserRef object but with the same unique ID
                    }
                    event.deregisterUserID(tempRemoteUserRef);  // removes the RemoteUserRef with the same unique ID
                    db.collection("events").document(event.getEventID()).set(event);
                }
            }
            else {
                Log.e("FireStore Task Error", "Task of retrieving events the entrant joined has failed");
            }
        });
    }
}
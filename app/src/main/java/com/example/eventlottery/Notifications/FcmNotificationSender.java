package com.example.eventlottery.Notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventlottery.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This is not being used
 */
public class FcmNotificationSender {

    //https://www.youtube.com/watch?v=o_PikvavsYY&ab_channel=CodingwithMukund

    //    private final String userFcmToken;

    private final String title;

    private final String body;

    private final Context context;

    private final String topic;

    private final String eventID;

    private final Boolean SignUp;

    private final String postUrl = "https://fcm.googleapis.com/v1/projects/eventlottery-32085/messages:send";

    /**
     * Constructor for FcmNotificationSender
     * @param title The title of the notification
     * @param body The message body of the notification
     * @param context The context
     * @param topic The topic
     * @param eventID The event's ID
     * @param SignUp Boolean flag for sign up
     */
    public FcmNotificationSender (String title, String body, Context context, String topic, String eventID, Boolean SignUp){

        this.title = title;
        this.body = body;
        this.context = context;
        this.topic = topic;
        this.eventID = eventID;
        this.SignUp = SignUp;
        Log.d("Title",title);
        Log.d("Body",body);
    }

    /**
     * Function for sending notifications
     */
    public void SendNotifications(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try{
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();


            notificationObject.put("title", title);
            notificationObject.put("body", body);

            messageObject.put("topic", topic);

            messageObject.put("topic", topic);

            messageObject.put("notification", notificationObject);

            mainObj.put("message", messageObject);



            mainObj.put("message", messageObject);




            Log.d("Notificaiton Sender: ", "First");
            Log.d("Notification sent:", mainObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, postUrl, mainObj, response -> {
                // code run got response
            }, volleyError -> {
                // code run error
                volleyError.getCause();
                Log.d("volleyError", volleyError.getMessage());
            }){

                @NonNull
                @Override
                public Map<String, String> getHeaders(){

                    AccessToken accessToken = new AccessToken();
                    String accessKey = accessToken.getAccessToken();
                    Map<String, String> header = new HashMap<>();
                    header.put("Content-Type","application/json; UTF-8");
                    header.put("Authorization","Bearer " + accessKey);

                    Log.e("Notificaiton Sender: ", accessKey);

                    return header;
                }
            };
            requestQueue.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }


}

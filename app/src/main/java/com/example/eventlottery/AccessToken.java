package com.example.eventlottery;

import android.util.Log;

import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {

    private static final String firebaseMessagingScope =
            "https://www.googleapis.com/auth/firebase.messaging";
    public String getAccessToken(){
        try{
//

            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"eventlottery\",\n" +
                    "  \"private_key_id\": \"6c9c82318795e37ccbb3abe30db85237b54bd617\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-16nlh@eventlottery.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"113768406071652300690\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-16nlh%40eventlottery.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();

        }catch(IOException e){
            Log.e("error","" + e.getMessage());
            return null;

        }
    }
}

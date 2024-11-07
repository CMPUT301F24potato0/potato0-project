package com.example.eventlottery;

import android.util.Log;

import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * TODO Describe this class
 */
public class AccessToken {

    private static final String firebaseMessagingScope =
            "https://www.googleapis.com/auth/firebase.messaging";

    /**
     * TODO Describe this method
     * @return TODO Describe the return value
     */
    public String getAccessToken(){
        try{
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"eventlottery\",\n" +
                    "  \"private_key_id\": \"6d360c1dbdcd6a8789c2af3ab9467b775c6ff000\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbmP6zUoAJ/olD\\nj882jGkpujQl7K4S7qqQGawAAw1fEmDtBPXGTAz6M9ewsPWIyIKq72Zzdqju8C+W\\nZBO0VCoJGOYnR9E8DXg/QS+tB6pALwOV6C/yfFXfk4Iod+yg0g3VznSpqyxXZtg1\\niaLDG9SmxBkm9dJ1+UlHijfq3pcp+cSOZoMnoQMJUVJIvWjQgEC8CDE2ucuVdG3u\\ndwsWT6zYyxblvKr8uGK3TXr1uMu2jgGFw8W23TE0O4GLcpO91hwDXG7SO95iRSKb\\nOLXB68PwcTFGfIqgJ/GwQlh5dS7B/Q5vkk5JhGwz5R35D/34x7uC3QxUYKOqCzJE\\nyx6Mmo0ZAgMBAAECggEABld6gUMkCwQ8tcW6btRs0HGrGj6Z+HvEwa6VYMMlfBH+\\nMSFDuSTOKwAKhirFBYgSl0/xle3CREoCzoQR4rnDCtYUD1bK5tuWFcUc4dY1o+JA\\nQnX3rYNQZq/jtq2InFdjW/fLSanVDiAX2m31KQAASOaMleNs0NHrupNq5J/U4ZxU\\nFzuEBus56VznA/so95XTJgUHwt9ESpmOopItQ1wEeB7NY+2VR8gBnvOfwCFusnH7\\nK6g2TGtwC6RDHmh9zJXfhPpuOrwENnIDj/hle7L74EQczC5v87uR2xH52+XWfbiF\\nLik3ebwmEhKNuvuHWR/cewjbEZx1zdqfKGGlOB9h5wKBgQDLW+KlgYJocpUBHbW7\\nWuJz8vzRQtavHTn9wBRZWmojepUGqIAQcqQ1sU0EEldY/6ia4vQYanPwfOCivIXq\\nmx2beuI4j/ftkazEataJpEPU2DjUKH8ukebFRr3+yKWTOjSmHyld4lU1Q6iVjzxL\\nYtns9ZzWciWynYTBPjwTneirQwKBgQDD4BT481o5MlJvMlDz5eKLECkFKZ9RKA2L\\nCHXSH+iBQZsAdf8BjWVrd7jXrCY5us75AoAOiQ4+74Hcs+CTENR1VYEo344AeCG3\\naJkVeoxEY1TjsAdBuLb2eB68vpfZN6ixlgnXq9Uw4RdgCrWBPeX8WrrPPdT8Hf6T\\nn7FTKTcKcwKBgHB9F1Yt2ZElML85S/mjQHibBLZMnqynIhj3U0pJ9URC3oScWJrA\\nj40T0mJaMu8MHJvwxs1qtU9mBGyAlCM+QpZq1xAqsx464rKT1rC0qMDoCidxb0i3\\ndEvT4e052D0kiVMLEbrZJAEet/ZjERsyRxIImwUUhz3SXEj4wAqp4rY5AoGBAKxU\\nar84gymNP397WkOq/mxw1FYpb3Scc4Xd9KLg1dNan/+A0NFR0GgDlgmR+LOTymLE\\nM1wWcwUl/S/qxes9xF5S0ubeAHYjOy69BOojvmKwrdAAX/IPjFE+D3Tpjnlagb10\\nDsbD9DYj40fpRBUoGz6xqGeKs+b4kLDar81FlppHAoGAREc3nbWT6UgMdSKtKTWU\\nh/NOEAewm22mXWVIVeH7tecH38iIMkPrpO/qeSSNPfzKW5pVYgsHsWfuQ8lKlK5c\\n8D7Vc4bVP2CCasoyeI5DRlMJRSqyeulo5JAMsyi0ldg5/Q3voit7W0wuEFYlTmF1\\nTIHaAv9Zxt4pfPdDR5ANeNM=\\n-----END PRIVATE KEY-----\\n\",\n" +
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

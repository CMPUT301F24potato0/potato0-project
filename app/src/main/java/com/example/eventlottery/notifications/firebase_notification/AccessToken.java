package com.example.eventlottery.notifications.firebase_notification;

import android.util.Log;

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
//

            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"eventlottery\",\n" +
                    "  \"private_key_id\": \"6c9c82318795e37ccbb3abe30db85237b54bd617\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC4H+1Zh7vmmcny\\nAdYygoOQnRVJvetVqJYpAMY6Q1YkxJyFncGPleMXhV50NHF34bU/mV/WscduJwnf\\n6VCxLh3NuMZL2NiEe/tu/p1MRbXWcp3hSLehgAgEoVqaPQC5YH65sSXkOM7rBLhc\\neWTeHnCUl5ihOu8Iz3hJfZoWZLYUMXdP6wPvi9JS3FcDdtHMRpxgPW5PrZ3MkMZY\\nxHndqBS7NQy9kkXx9pgg1wDbawoaZGI/bKBEFrkJp7TdjOrDw5ZdTEuUHbsJAGg2\\nJ3hzcZ0BL6JRt1rg6FYe2ZEeHHoK+MxIofOqTlT3cYd1u0go3nH96q1SVAKn/rYr\\nIkt2Z3xpAgMBAAECggEACMipI5DIo4cpraxtwgoc6ay8w8BWNkdtJnadflERDn78\\nspkkj0uIwmn/6A5KbXwsj+SqwEzJ5VTicTapvVIkZNiQSTUGm0do5tIpweY/dwQP\\nyRwVepRnjYT/FAWd/7n9QbGUS690G50liG/YIoqUN1qGEwcDOUnghK0DyVazowHm\\nXs1qDm8qqVDQDwoyK/YVU/9vsBGW9+cez6DPfui/yHFpu5Vbfnx5GJJVCHDiQCCk\\n8Taqg6N86m9dc2ECD4XKaELb+jN+ykgOjvxCcN/JkagPTy+GEzb/6TzoYVwasfMC\\nwJ2aWuece7rHH+rBixOMPkGBr4l0Y7X0BYN+UplmCQKBgQDlS0PBtz8iBbP/Q/GA\\n0OJl0ze/SPyX4dSV5SGVt9dDim6xm26daHQc2c6gIn/+x4o2c6cjkDpSogG4PJsT\\nos0pTHuTHWR0j9yigvnEGnHKYO/utFGdHXbA4+eZzINtUTt3XBIQ4Xz3E9BxYpaR\\n24sHdWrwDzutCzyoCXaHmLJDNQKBgQDNkd/PES7qzn2CTBbNDy+KRGnPAAlUEpqP\\nSkbOY/FuTckVqGqPiQ/h/j8lvvF0yPlHsCC5eVtkfKsyE6m1WbfVgBHBV8Zq+CYz\\naqWqbdw1lVkg3VGovdQoL8iGV6iFSl1u1DbiSuNZRqR/B6YguEEr6RkGVkja0ZV+\\nOpo3EImm5QKBgHRKJcUbgONqbQ2rkdI5HnlyFDXpeGI3YK/dE5lxMbXJgbaGFCW1\\nEh6HwKmqlavL26ceJMh+n3XFQIYGBsiAqvilF49XOtl7FPU+5Bm3J7rySv2Pakz6\\n4n2VmKKB/K32oug4Q9lX4x8UZ4T58U9sPyi7Pk8eCrAKoOrAUeMXaGF5AoGALRPW\\n0w4+joi3JvJOcarpztUPqq+kXAY0VEWCX1G1KG3wMUqcC1uP2wuohztmsSg82YZ6\\ntpExBEj+NV3wce75i9yoiBAaV2yQ2+d/6m5qwmk8gSNm+6ycsC9CNotUTd4vvNTW\\nMCshzXrbKWvg9v3QdLOJpEUZdrpMoMBy/q4msS0CgYAlCU03mggn027/nDghiDj3\\nmoZIRJgb62e/O6T+to6S6S+mE5P9OZvYb25b165UR2edbzfeVm/0GC3Qir8/L54T\\n6RNJCvVlFIxYf2I/Lq2ip/PwrFL395TQIbmQ9MLSEgB3WNTko72xhSK3JfGL49l9\\nPzdR4hCMMlxwTT7ueSX5Qg==\\n-----END PRIVATE KEY-----\\n\",\n" +
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

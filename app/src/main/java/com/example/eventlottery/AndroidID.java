package com.example.eventlottery;

import android.content.Context;
import android.provider.Settings;

public class AndroidID {
    public static final String ANDROID_ID_KEY = "android_id";
    private static String androidID = "";
    public String getAndroidId(Context context) {
        if (androidID.isEmpty()) {
            androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidID;
    }
}
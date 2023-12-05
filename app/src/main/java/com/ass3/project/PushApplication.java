package com.ass3.project;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

public class PushApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "e6b73f2f-f949-4a0d-938b-a05b00b5db2f";

    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //https://documentation.onesignal.com/v7.0/docs/android-native-sdk

        //846935e2-8dd3-4fb5-9f3f-3177ff443ff8





    }


}

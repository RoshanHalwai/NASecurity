package com.kirtanlabs.nammaapartmentssecurity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.BETA_ENV;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.DEV_ENV;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_NAME;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/15/2018
 */
public class NammaApartmentsSecurity extends Application {

    public static String BUILD_VARIANT;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseOptions firebaseOptions;

        BUILD_VARIANT = getBuildVariant();

        if (BUILD_VARIANT.equals(BETA_ENV)) {
            //TODO:Change the database URL when we deploy to new society.
            firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                    .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                    .setDatabaseUrl("https://nammaapartments-beta-airforcecolony.firebaseio.com/")
                    .setStorageBucket("nammaapartments-beta.appspot.com")
                    .setGcmSenderId("896005326129")
                    .setProjectId("nammaapartments-beta")
                    .build();
        } else {
            //TODO:Change the database URL when we deploy to new society.
            firebaseOptions = new FirebaseOptions.Builder()
                    .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                    .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                    .setDatabaseUrl("https://nammaapartments-dev-airforcecolony.firebaseio.com/")
                    .setStorageBucket("nammaapartments-development.appspot.com")
                    .setGcmSenderId("703896080530")
                    .setProjectId("nammaapartments-development")
                    .build();
        }

        initializeApp(this, firebaseOptions, BUILD_VARIANT);
    }

    private String getBuildVariant() {
        return getApplicationContext().getPackageName().equals(PACKAGE_NAME) ? DEV_ENV : BETA_ENV;
    }

    private void initializeApp(final Context context, final FirebaseOptions firebaseOptions, final String buildVariant) {
        try {
            FirebaseApp.initializeApp(context, firebaseOptions, buildVariant);
        } catch (Exception e) {
            Log.d("Nammma Apartments", e.getLocalizedMessage());
        }
    }

}

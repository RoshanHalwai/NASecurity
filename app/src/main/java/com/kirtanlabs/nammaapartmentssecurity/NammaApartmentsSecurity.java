package com.kirtanlabs.nammaapartmentssecurity;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.BETA_ENV;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.DEV_ENV;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/15/2018
 */
public class NammaApartmentsSecurity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseOptions BETA_ENV_OPTIONS = new FirebaseOptions.Builder()
                .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                .setDatabaseUrl("https://nammaapartments-beta.firebaseio.com/")
                .build();

        final FirebaseOptions DEV_ENV_OPTIONS = new FirebaseOptions.Builder()
                .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                .setDatabaseUrl("https://nammaapartments-development.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(this, BETA_ENV_OPTIONS, BETA_ENV);
        FirebaseApp.initializeApp(this, DEV_ENV_OPTIONS, DEV_ENV);
    }

}

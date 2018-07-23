package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /*this method will be called
    when the token is generated*/
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        /* Here we are generating token id for Security Guard so that User can send push notification to guard*/
        String token = FirebaseInstanceId.getInstance().getToken();

        /*For now we are displaying the token in the log
        copy it as this method is called only when the new token is generated
        and usually new token is only generated when the app is reinstalled or the data is cleared */
        Log.d("MyRefreshedToken", token);
    }
}

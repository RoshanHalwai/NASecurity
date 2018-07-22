package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency.RecentEmergencyDetails;

import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String message = remoteMessageData.get(Constants.MESSAGE);
        String emergencyType = remoteMessageData.get(Constants.EMERGENCY_TYPE);
        String ownerName = remoteMessageData.get(Constants.OWNER_NAME);
        String mobileNumber = remoteMessageData.get(Constants.MOBILE_NUMBER);
        String apartmentName = remoteMessageData.get(Constants.APARTMENT_NAME);
        String flatNumber = remoteMessageData.get(Constants.FLAT_NUMBER);

        Intent intent = new Intent(this, RecentEmergencyDetails.class);
        intent.putExtra(Constants.EMERGENCY_TYPE, emergencyType);
        intent.putExtra(Constants.OWNER_NAME, ownerName);
        intent.putExtra(Constants.MOBILE_NUMBER, mobileNumber);
        intent.putExtra(Constants.APARTMENT_NAME, apartmentName);
        intent.putExtra(Constants.FLAT_NUMBER, flatNumber);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.RECENT_EMERGENCY_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.medical_emergency)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                .setPriority(PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*To support Android Oreo Devices and higher*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id), "Namma Apartments Security Channel", NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }

        int notificationID = (int) System.currentTimeMillis();
        Objects.requireNonNull(notificationManager).notify(notificationID, notification);
    }
}

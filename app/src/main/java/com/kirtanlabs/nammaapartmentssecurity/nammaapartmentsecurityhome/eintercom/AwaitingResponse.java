package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;

import java.io.File;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.ACCEPTED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.IGNORED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.REJECTED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.getBitmapFromFile;

public class AwaitingResponse extends BaseActivity {

    private LinearLayout layoutAwaitingResponse;
    private LinearLayout layoutUserResponse;
    private android.support.v7.widget.CardView layoutCallUser;
    private TextView textUserResponse;
    private ImageView imageResponseStatus;
    private Intent callIntent;
    private String notificationUID, userUID, visitorType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_awaiting_response;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.e_intercom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getStringExtra("NotificationUID") != null) {
            notificationUID = getIntent().getStringExtra("NotificationUID");
            userUID = getIntent().getStringExtra("SentUserUID");
            visitorType = getIntent().getStringExtra("EIntercomType");
        }

        /*Get Id for UI Views*/
        layoutAwaitingResponse = findViewById(R.id.layoutAwaitingResponse);
        layoutUserResponse = findViewById(R.id.layoutUserResponse);
        layoutCallUser = findViewById(R.id.layoutCallUser);
        de.hdodenhof.circleimageview.CircleImageView imageVisitorPic = findViewById(R.id.imageVisitorPic);
        imageResponseStatus = findViewById(R.id.imageResponseStatus);
        TextView textReference = findViewById(R.id.textReference);
        TextView textNotificationSent = findViewById(R.id.textNotificationSent);
        TextView textCallUser = findViewById(R.id.textCallUser);
        textUserResponse = findViewById(R.id.textUserResponse);

        /*Set Fonts for UI Views*/
        textReference.setTypeface(setLatoRegularFont(this));
        textNotificationSent.setTypeface(setLatoRegularFont(this));
        textCallUser.setTypeface(setLatoRegularFont(this));
        textUserResponse.setTypeface(setLatoBoldFont(this));

        textReference.setText(getIntent().getStringExtra("Reference"));
        switch (visitorType) {
            case GUEST:
                File guestImage = new File(getIntent().getStringExtra("VisitorImageFilePath"));
                Bitmap dailyServiceProfilePic = getBitmapFromFile(AwaitingResponse.this, guestImage);
                imageVisitorPic.setImageBitmap(dailyServiceProfilePic);
                break;
            case CAB:
                imageVisitorPic.setImageDrawable(getResources().getDrawable(R.drawable.taxi));
                break;
            default:
                imageVisitorPic.setImageDrawable(getResources().getDrawable(R.drawable.delivery_man));

        }

        textCallUser.setOnClickListener(v -> {
            callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getIntent().getStringExtra("UserMobileNumber")));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                startActivity(callIntent);
            }
        });

        /*Keep track of user response*/
        getUserResponse(visitorType);

        /*Action to be performed when no response received - Timeout 45 Seconds*/
        Handler h = new Handler(Looper.getMainLooper());
        long delayInMilliseconds = 45000;
        h.postDelayed(() -> setNotificationToIgnored(userUID, visitorType, notificationUID), delayInMilliseconds);
    }

    /**
     * Sets the status of the notification to IGNORED since the notification timed out
     *
     * @param currentUserID   - userUID of the the LoggedIn user
     * @param visitorType     - can be either one of these Guest/Cab/Package
     * @param notificationUID - unique UID to identify each gate notifications
     */
    private void setNotificationToIgnored(final String currentUserID, final String visitorType, final String notificationUID) {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(currentUserID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference currentUserDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber());

                /*Here we are setting the notification status so that Guard know that this notification has been ignored by the user*/
                DatabaseReference currentUserNotificationReference = currentUserDataReference
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(currentUserID)
                        .child(EINTERCOM_TYPE_MAP.get(visitorType))
                        .child(notificationUID);

                currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).setValue(IGNORED);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startActivity(callIntent);
        }
    }

    private void getUserResponse(String visitorType) {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(userUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber())
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(userUID)
                        .child(EINTERCOM_TYPE_MAP.get(visitorType));
                userDataReference.child(notificationUID).child(FIREBASE_CHILD_STATUS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            hideProgressIndicator();
                            switch (Objects.requireNonNull(dataSnapshot.getValue(String.class))) {
                                case ACCEPTED:
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.request_accepted_na));
                                    textUserResponse.setText(getString(R.string.accepted_request));
                                    break;
                                case REJECTED:
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));
                                    textUserResponse.setText(getString(R.string.rejected_request));
                                    break;
                                default:
                                    //TODO: Add new Image for Ignored case
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));
                                    textUserResponse.setText(getString(R.string.ignored_request));
                                    layoutCallUser.setVisibility(View.VISIBLE);
                            }
                            layoutAwaitingResponse.setVisibility(View.GONE);
                            layoutUserResponse.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

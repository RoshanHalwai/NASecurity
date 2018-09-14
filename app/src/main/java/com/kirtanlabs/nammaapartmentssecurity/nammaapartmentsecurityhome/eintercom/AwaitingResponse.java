package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;

public class AwaitingResponse extends BaseActivity {

    private TextView textUserResponse;

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
        textUserResponse = findViewById(R.id.textUserResponse);

        if (getIntent() != null && getIntent().getStringExtra("NotificationUID") != null) {
            String NotificationUID = getIntent().getStringExtra("NotificationUID");
            String SentUserUID = getIntent().getStringExtra("SentUserUID");
            String visitorType = getIntent().getStringExtra("EIntercomType");

            DatabaseReference databaseReference = Constants.PRIVATE_USERS_REFERENCE.child(SentUserUID);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserFlatDetails userFlatDetails = dataSnapshot.getValue(NammaApartmentUser.class).getFlatDetails();
                    DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                            .child(userFlatDetails.getCity())
                            .child(userFlatDetails.getSocietyName())
                            .child(userFlatDetails.getApartmentName())
                            .child(userFlatDetails.getFlatNumber())
                            .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                            .child(SentUserUID)
                            .child(EINTERCOM_TYPE_MAP.get(visitorType));
                    userDataReference.child(NotificationUID).child(FIREBASE_CHILD_STATUS).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                hideProgressIndicator();
                                textUserResponse.setText(dataSnapshot.getValue(String.class));
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
}

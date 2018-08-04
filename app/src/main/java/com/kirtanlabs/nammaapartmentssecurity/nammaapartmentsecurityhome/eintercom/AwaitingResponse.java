package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;

public class AwaitingResponse extends BaseActivity {

    TextView textUserResponse;

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
            String visitorType = getIntent().getStringExtra("VisitorType");

            DatabaseReference databaseReference = Constants.PRIVATE_USERS_REFERENCE.child(SentUserUID);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserFlatDetails userFlatDetails = dataSnapshot.getValue(NammaApartmentUser.class).getFlatDetails();
                    DatabaseReference userDataReference = FirebaseDatabase.getInstance().getReference().child("userData")
                            .child(Constants.FIREBASE_CHILD_PRIVATE)
                            .child(userFlatDetails.getCity())
                            .child(userFlatDetails.getSocietyName())
                            .child(userFlatDetails.getApartmentName())
                            .child(userFlatDetails.getFlatNumber())
                            .child("gateNotifications")
                            .child(SentUserUID)
                            .child(EINTERCOM_TYPE_MAP.get(visitorType));
                    userDataReference.child(NotificationUID).child("status").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                hideProgressIndicator();
                                textUserResponse.setText(dataSnapshot.getValue().toString());
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

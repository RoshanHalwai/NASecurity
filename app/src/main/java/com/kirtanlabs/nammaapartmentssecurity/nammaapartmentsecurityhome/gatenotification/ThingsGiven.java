package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class ThingsGiven extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
    private int givenThingsTo;
    private String mobileNumber;
    private String serviceType;
    private String visitorOrDailyServiceUid;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_things_given;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Given Things to Guest and Given Things to Daily Services
         *we set the title based on the user click on Gate Notification screen*/
        if (getIntent().getIntExtra(Constants.GIVEN_THINGS_TO, 0) == R.string.things_given_to_guest) {
            givenThingsTo = R.string.things_given_to_guest;
        } else {
            givenThingsTo = R.string.things_given_to_daily_services;
        }
        return givenThingsTo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonVerifyThings = findViewById(R.id.buttonVerifyThings);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyThings.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonVerifyThings.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        mobileNumber = editMobileNumber.getText().toString().trim();
        if (isValidPhone(mobileNumber)) {
            /*We need Progress Indicator in this screen*/
            showProgressIndicator();
            checkIsThingsGivenInFireBase();
        } else {
            editMobileNumber.setError(getText(R.string.number_10digit_validation));
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to check whether Resident has given things to visitor or daily service or not.
     */
    private void checkIsThingsGivenInFireBase() {
        DatabaseReference mobileNumberReference;
        if (givenThingsTo == R.string.things_given_to_guest) {
            mobileNumberReference = Constants.PREAPPROVED_VISITORS_MOBILE_REFERENCE.child(mobileNumber);
            mobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        visitorOrDailyServiceUid = (String) dataSnapshot.getValue();
                        assert visitorOrDailyServiceUid != null;
                        DatabaseReference visitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                .child(visitorOrDailyServiceUid)
                                .child(Constants.FIREBASE_CHILD_HANDED_THINGS);
                        visitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    residentHasGivenThings();
                                } else {
                                    openValidationStatusDialog(Constants.FAILED, getString(R.string.resident_has_not_given_things_to_visitor));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.invalid_visitor));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            mobileNumberReference = Constants.PRIVATE_DAILYSERVICES_REFERENCE.child(mobileNumber);
            mobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        visitorOrDailyServiceUid = (String) dataSnapshot.getValue();

                        DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE;
                        assert visitorOrDailyServiceUid != null;
                        dailyServiceReference.child(Constants.FIREBASE_CHILD_DAILYSERVICETYPE)
                                .child(visitorOrDailyServiceUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                serviceType = (String) dataSnapshot.getValue();

                                assert serviceType != null;
                                dailyServiceReference.child(serviceType)
                                        .child(visitorOrDailyServiceUid)
                                        .child(Constants.FIREBASE_CHILD_HANDED_THINGS)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    residentHasGivenThings();
                                                } else {
                                                    openValidationStatusDialog(Constants.FAILED, getString(R.string.resident_has_not_given_things_to_daily_service));
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
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.invalid_daily_service));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * This method is invoked when visitor has given something to visitor or daily service.
     */
    private void residentHasGivenThings() {
        Intent intent = new Intent(ThingsGiven.this, ResidentHasGivenThings.class);
        intent.putExtra(Constants.SCREEN_TITLE, givenThingsTo);
        intent.putExtra(Constants.GUEST_UID, visitorOrDailyServiceUid);
        if (givenThingsTo == R.string.things_given_to_daily_services) {
            intent.putExtra(Constants.SERVICE_TYPE, serviceType);
        }
        startActivity(intent);
        finish();
    }
}

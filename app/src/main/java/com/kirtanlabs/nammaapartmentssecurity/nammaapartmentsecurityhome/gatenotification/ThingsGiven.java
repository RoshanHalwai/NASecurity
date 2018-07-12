package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;

public class ThingsGiven extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
    private DatabaseReference visitorReference;
    private int givenThingsTo;
    private String mobileNumber;
    private String serviceType;
    private String visitorOrDailyServiceUid;
    private AlertDialog dialog;

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
        switch (v.getId()) {
            case R.id.buttonVerifyThings:
                mobileNumber = editMobileNumber.getText().toString().trim();
                if (isValidPhone(mobileNumber)) {
                    /*We need Progress Indicator in this screen*/
                    showProgressIndicator();
                    checkIsThingsGivenInFireBase();
                } else {
                    editMobileNumber.setError(getText(R.string.number_10digit_validation));
                }
                break;
            case R.id.buttonOk:
                dialog.cancel();
                visitorReference.child(Constants.FIREBASE_CHILD_STATUS).setValue(getString(R.string.left));
                Intent intent = new Intent(ThingsGiven.this, NammaApartmentSecurityHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to check whether Resident has given things to visitor / daily service or not.
     */
    private void checkIsThingsGivenInFireBase() {
        // Database Reference of Visitor and Daily Service Mobile number
        DatabaseReference mobileNumberReference;

        if (givenThingsTo == R.string.things_given_to_guest) {

            // Retrieving Visitors UID (mapped with Mobile number) from preApprovedVisitorsMobileNumber in Firebase.
            mobileNumberReference = Constants.PREAPPROVED_VISITORS_MOBILE_REFERENCE.child(mobileNumber);
            mobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        visitorOrDailyServiceUid = (String) dataSnapshot.getValue();
                        assert visitorOrDailyServiceUid != null;
                        visitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                .child(visitorOrDailyServiceUid);
                        visitorReference.child(Constants.FIREBASE_CHILD_HANDED_THINGS)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            givenThingsToVisitorDialog();
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
            // Retrieving Daily Service UID (mapped with Mobile number) from dailyServices->all->private in Firebase.
            mobileNumberReference = Constants.PRIVATE_DAILYSERVICES_REFERENCE.child(mobileNumber);
            mobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        visitorOrDailyServiceUid = (String) dataSnapshot.getValue();

                        // Getting the Daily Service Type and its corresponding details
                        DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE;
                        assert visitorOrDailyServiceUid != null;
                        dailyServiceReference.child(Constants.FIREBASE_CHILD_DAILYSERVICETYPE)
                                .child(visitorOrDailyServiceUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                serviceType = (String) dataSnapshot.getValue();
                                givenThingsToDailyService();
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
     * This method is invoked when Resident has given something to daily service.
     */
    private void givenThingsToDailyService() {
        Intent intent = new Intent(ThingsGiven.this, GivenThingsToDailyService.class);
        intent.putExtra(Constants.FIREBASE_CHILD_DAILYSERVICE_UID, visitorOrDailyServiceUid);
        intent.putExtra(Constants.SERVICE_TYPE, serviceType);
        startActivity(intent);
        finish();
    }

    /**
     * This method is invoked when Resident has given something to visitor.
     */
    private void givenThingsToVisitorDialog() {
        View givenThingsToVisitorDialog = View.inflate(this, R.layout.layout_things_given_dialog, null);

        /*Getting Id's for all the views*/
        TextView textHasGivenThings = givenThingsToVisitorDialog.findViewById(R.id.textHasGivenThings);
        Button buttonOk = givenThingsToVisitorDialog.findViewById(R.id.buttonOk);

        /*Setting font for all the views*/
        textHasGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        buttonOk.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonOk.setOnClickListener(this);

        AlertDialog.Builder alertGivenThingsDialog = new AlertDialog.Builder(this);
        alertGivenThingsDialog.setView(givenThingsToVisitorDialog);
        dialog = alertGivenThingsDialog.create();

        new Dialog(this);
        dialog.show();
    }
}

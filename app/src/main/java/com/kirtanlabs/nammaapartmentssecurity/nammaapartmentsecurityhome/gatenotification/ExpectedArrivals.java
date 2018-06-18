package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;


import java.text.DateFormatSymbols;
import java.util.Calendar;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;

public class ExpectedArrivals extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editCabNumberAndResidentMobileNumber;
    private int arrivalType;
    private String cabDriverUid;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expected_arrivals;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Expected Cab and Package Arrivals
         *we set the title based on the user click on Gate Notification screen*/
        if (getIntent().getIntExtra(Constants.ARRIVAL_TYPE, 0) == R.string.expected_cab_arrivals) {
            arrivalType = R.string.expected_cab_arrivals;
        } else {
            arrivalType = R.string.expected_package_arrivals;
        }
        return arrivalType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textCabNumberAndResidentMobileNumber = findViewById(R.id.textCabNumberAndResidentMobileNumber);
        editCabNumberAndResidentMobileNumber = findViewById(R.id.editCabNumberAndResidentMobileNumber);
        Button buttonVerifyCabNumberAndPackageVendor = findViewById(R.id.buttonVerifyCabNumberAndPackageVendor);

        /*Setting font for all the views*/
        textCabNumberAndResidentMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        editCabNumberAndResidentMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyCabNumberAndPackageVendor.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same layout for Expected Cab and Package Arrivals we need to
         *change some Views Text in Expected Package Arrivals*/
        if (arrivalType == R.string.expected_package_arrivals) {
            changeViewsText(textCabNumberAndResidentMobileNumber, buttonVerifyCabNumberAndPackageVendor);
            editCabNumberAndResidentMobileNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        /*Setting onClickListener for view*/
        buttonVerifyCabNumberAndPackageVendor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        /*We need Progress Indicator in this screen*/
        showProgressIndicator();
        String cabNumberOrResidentMobileNumber = editCabNumberAndResidentMobileNumber.getText().toString().trim();
        if (editCabNumberAndResidentMobileNumber.length() > EDIT_TEXT_MIN_LENGTH) {
            checkDetailsInFirebase(cabNumberOrResidentMobileNumber);
        } else {
            editCabNumberAndResidentMobileNumber.setError(getString(R.string.field_cant_be_empty));
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * We update the TextView textCabNumberAndResidentMobileNumber and Button buttonVerifyCabNumber Text in Expected Package Arrivals
     *
     * @param textCabNumberAndResidentMobileNumber - to update text in Expected Package Arrivals Screen
     * @param buttonVerifyCabNumber                - to update text in Expected Package Arrivals Screen
     */
    private void changeViewsText(TextView textCabNumberAndResidentMobileNumber, Button buttonVerifyCabNumber) {
        textCabNumberAndResidentMobileNumber.setText(getResources().getString(R.string.resident_mobile_number));
        buttonVerifyCabNumber.setText(getResources().getString(R.string.verify_package_vendor));
    }

    /**
     * This method is used to check whether User has booked this cab or not
     * and package vendor is valid or not
     *
     * @param cabNumberOrResidentMobileNumber - that need to check in firebase whether it is valid or not.
     */
    private void checkDetailsInFirebase(String cabNumberOrResidentMobileNumber) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_CABS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_ALL)
                .child(cabNumberOrResidentMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    cabDriverUid = (String) dataSnapshot.getValue();
                    isExpectedArrivalReachedOnTime(cabDriverUid);
                } else {
                    openValidationStatusDialog(Constants.FAILED, getString(R.string.dont_allow_cab_to_enter));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * This is used check whether expected Arrival reached into society in expected time or not.
     *
     * @param cabDriverUid - to check arrival time of Expected Arrival.
     */
    private void isExpectedArrivalReachedOnTime(String cabDriverUid) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_CABS)
                .child(Constants.FIREBASE_CHILD_PUBLIC)
                .child(cabDriverUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String validFor = (String) dataSnapshot.child("validFor").getValue();
                String expectedDateAndTime = (String) dataSnapshot.child("dateAndTimeOfArrival").getValue();
                String[] separatedDateAndTime = TextUtils.split(expectedDateAndTime, "\t\t ");
                String expectedDate = separatedDateAndTime[0];
                String expectedTime = separatedDateAndTime[1];

                String[] validHours = TextUtils.split(validFor, " ");
                int hoursValidFor = Integer.parseInt(validHours[0]);
                String[] expectedHoursAndMinutes = TextUtils.split(expectedTime, ":");
                int expectedHour = Integer.parseInt(expectedHoursAndMinutes[0]);
                int totalValidHours = expectedHour + hoursValidFor;

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                String currentDate = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)].substring(0, 3) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);

                if (expectedDate.equals(currentDate) && currentHour < totalValidHours) {
                    openExpectedArrivalList();
                } else if (expectedDate.equals(currentDate) && currentHour == totalValidHours && currentMinute <= Integer.parseInt(expectedHoursAndMinutes[1])) {
                    openExpectedArrivalList();
                } else {
                    openValidationStatusDialog(Constants.FAILED, getString(R.string.dont_allow_cab_to_enter));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * This method is used to open Expected Arrivals List Activity.
     */
    private void openExpectedArrivalList() {
        Intent intentCabArrival = new Intent(ExpectedArrivals.this, ExpectedArrivalsList.class);
        intentCabArrival.putExtra(Constants.SCREEN_TITLE, arrivalType);
        intentCabArrival.putExtra(Constants.EXPECTED_ARRIVAL_UID, cabDriverUid);
        startActivity(intentCabArrival);
    }
}
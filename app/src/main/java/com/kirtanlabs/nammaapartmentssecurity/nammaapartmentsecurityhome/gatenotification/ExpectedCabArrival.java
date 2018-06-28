package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Calendar;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;

public class ExpectedCabArrival extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editCabNumber;
    private String cabDriverUid;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expected_cab_arrivals;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.expected_cab_arrivals;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textCabNumber = findViewById(R.id.textCabNumber);
        editCabNumber = findViewById(R.id.editCabNumber);
        Button buttonVerifyCabNumberAndPackageVendor = findViewById(R.id.buttonVerifyCabNumberAndPackageVendor);

        /*Setting font for all the views*/
        textCabNumber.setTypeface(Constants.setLatoBoldFont(this));
        editCabNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyCabNumberAndPackageVendor.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonVerifyCabNumberAndPackageVendor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        String cabNumber = editCabNumber.getText().toString().trim();
        if (editCabNumber.length() > EDIT_TEXT_MIN_LENGTH) {
            /*We need Progress Indicator in this screen*/
            showProgressIndicator();
            checkDetailsInFirebase(cabNumber);
        } else {
            editCabNumber.setError(getString(R.string.field_cant_be_empty));
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether User has booked this cab or not
     *
     * @param cabNumber - that need to check in firebase whether it is valid or not.
     */
    private void checkDetailsInFirebase(String cabNumber) {
        // Getting Cab Driver UID (Mapped with Cab Number) in Firebase.
        DatabaseReference cabNumberReference = Constants.ALL_CABS_REFERENCE
                .child(cabNumber);
        cabNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        // Retrieving Cab Driver details from (Cab->Public->CabDriverUID) in Firebase
        DatabaseReference cabReference = Constants.PUBLIC_CABS_REFERENCE.child(cabDriverUid);
        cabReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_STATUS).getValue();
                String validFor = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_VALIDFOR).getValue();
                String expectedDateAndTime = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL).getValue();
                String[] separatedDateAndTime = TextUtils.split(expectedDateAndTime, "\t\t ");
                String expectedDate = separatedDateAndTime[0];
                String expectedTime = separatedDateAndTime[1];

                String[] validHours = TextUtils.split(validFor, " ");
                int hoursValidFor = Integer.parseInt(validHours[0]);
                String[] expectedHoursAndMinutes = TextUtils.split(expectedTime, ":");
                int expectedHour = Integer.parseInt(expectedHoursAndMinutes[0]);
                int expectedMinutes = Integer.parseInt(expectedHoursAndMinutes[1]);
                int totalValidHours = expectedHour + hoursValidFor;

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                String currentDate = getCurrentDate();

                assert status != null;
                if (status.equals(getString(R.string.left))) {
                    openValidationStatusDialog(Constants.FAILED, getString(R.string.cab_left_society));
                } else {
                    if (expectedDate.equals(currentDate) && (currentHour < totalValidHours || (currentHour == totalValidHours && currentMinute <= expectedMinutes))) {
                        openExpectedArrivalList();
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.dont_allow_cab_to_enter));
                    }
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
        Intent intentCabArrival = new Intent(ExpectedCabArrival.this, ExpectedArrivalsList.class);
        intentCabArrival.putExtra(Constants.SCREEN_TITLE, R.string.expected_cab_arrivals);
        intentCabArrival.putExtra(Constants.EXPECTED_ARRIVAL_UID, cabDriverUid);
        startActivity(intentCabArrival);
    }
}
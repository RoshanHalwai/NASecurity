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
        Intent intentCabArrival = new Intent(ExpectedCabArrival.this, ExpectedArrivalsList.class);
        intentCabArrival.putExtra(Constants.SCREEN_TITLE, R.string.expected_cab_arrivals);
        intentCabArrival.putExtra(Constants.EXPECTED_ARRIVAL_UID, cabDriverUid);
        startActivity(intentCabArrival);
        finish();
    }
}
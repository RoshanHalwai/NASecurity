package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.vehiclevalidation.VehicleDetails;

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_VEHICLES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_NUMBER_FIELD_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EXPECTED_ARRIVAL_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FAILED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

public class VehicleValidation extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editVehicleStateCode, editVehicleRtoNumber, editVehicleSerialNumberOne,
            editVehicleSerialNumberTwo;
    private TextView textErrorVehicleNumber;
    private String vehicleUid;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_vehicle_validation;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.vehicle_validation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textVehicleNumber = findViewById(R.id.textVehicleNumber);
        textErrorVehicleNumber = findViewById(R.id.textErrorVehicleNumber);
        editVehicleStateCode = findViewById(R.id.editVehicleStateCode);
        editVehicleRtoNumber = findViewById(R.id.editVehicleRtoNumber);
        editVehicleSerialNumberOne = findViewById(R.id.editVehicleSerialNumberOne);
        editVehicleSerialNumberTwo = findViewById(R.id.editVehicleSerialNumberTwo);
        Button buttonVerifyVehicleNumber = findViewById(R.id.buttonVerifyVehicleNumber);

        /*Setting font for all the views*/
        textVehicleNumber.setTypeface(setLatoBoldFont(this));
        textErrorVehicleNumber.setTypeface(setLatoRegularFont(this));
        editVehicleStateCode.setTypeface(setLatoRegularFont(this));
        editVehicleRtoNumber.setTypeface(setLatoRegularFont(this));
        editVehicleSerialNumberOne.setTypeface(setLatoRegularFont(this));
        editVehicleSerialNumberTwo.setTypeface(setLatoRegularFont(this));
        buttonVerifyVehicleNumber.setTypeface(setLatoLightFont(this));

        /*Setting events for Cab Number edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyVehicleNumber.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (isAllFieldsFilled(new EditText[]{editVehicleStateCode, editVehicleRtoNumber, editVehicleSerialNumberOne, editVehicleSerialNumberTwo})) {
            String vehicleNumber = editVehicleStateCode.getText().toString().trim() + HYPHEN + editVehicleRtoNumber.getText().toString().trim()
                    + HYPHEN + editVehicleSerialNumberOne.getText().toString().trim() + HYPHEN + editVehicleSerialNumberTwo.getText().toString().trim();
            //We need Progress Indicator in this screen
            showProgressIndicator();
            checkDetailsInFirebase(vehicleNumber);
            textErrorVehicleNumber.setVisibility(View.GONE);
        } else {
            textErrorVehicleNumber.setVisibility(View.VISIBLE);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Once user enters details in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editVehicleStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editVehicleRtoNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editVehicleRtoNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editVehicleStateCode.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editVehicleSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editVehicleSerialNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editVehicleRtoNumber.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editVehicleSerialNumberTwo.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editVehicleSerialNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editVehicleSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * This method is used to check whether User has booked this cab or not
     *
     * @param vehicleNumber - that need to check in firebase whether it is valid or not.
     */
    private void checkDetailsInFirebase(String vehicleNumber) {
        // Getting Cab Driver UID (Mapped with Cab Number) in Firebase.
        DatabaseReference cabNumberReference = ALL_CABS_REFERENCE
                .child(vehicleNumber);
        cabNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    vehicleUid = dataSnapshot.getValue(String.class);
                    openExpectedArrivalList();
                } else {
                    /*Checking if vehicle number is added by User in vehicle list or not*/
                    checkDetailsInVehicleList(vehicleNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * This method is used to check whether User has entered his/her Vehicle Details in firebase or not
     *
     * @param vehicleNumber - that need to check in firebase whether it is valid or not.
     */
    private void checkDetailsInVehicleList(String vehicleNumber) {
        DatabaseReference vehicleNumberReference = ALL_VEHICLES_REFERENCE.
                child(vehicleNumber);
        vehicleNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vehicleUid = dataSnapshot.getValue(String.class);
                    Intent intent = new Intent(VehicleValidation.this, VehicleDetails.class);
                    intent.putExtra(Constants.VEHICLE_UID, vehicleUid);
                    startActivity(intent);
                    finish();
                } else {
                    openValidationStatusDialog(FAILED, getString(R.string.dont_allow_vehicle_to_enter));
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
        // Retrieving Status of Cab driver from(Cab->Public->CabDriverUid) in Firebase.
        DatabaseReference expectedArrivalStatusReference = PRIVATE_CABS_REFERENCE
                .child(vehicleUid);
        expectedArrivalStatusReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = Objects.requireNonNull(dataSnapshot.child(FIREBASE_CHILD_STATUS).getValue(String.class));
                if (!status.equals(getString(R.string.left))) {
                    Intent intentCabArrival = new Intent(VehicleValidation.this, ExpectedArrivalsList.class);
                    intentCabArrival.putExtra(SCREEN_TITLE, R.string.vehicle_validation);
                    intentCabArrival.putExtra(EXPECTED_ARRIVAL_UID, vehicleUid);
                    startActivity(intentCabArrival);
                    finish();
                } else {
                    openValidationStatusDialog(FAILED, getString(R.string.dont_allow_vehicle_to_enter));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
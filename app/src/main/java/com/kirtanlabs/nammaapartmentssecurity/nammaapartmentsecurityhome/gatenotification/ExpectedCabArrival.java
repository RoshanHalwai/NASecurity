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

import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_NUMBER_FIELD_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.HYPHEN;

public class ExpectedCabArrival extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editCabStateCode;
    private EditText editCabRtoNumber;
    private EditText editCabSerialNumberOne;
    private EditText editCabSerialNumberTwo;
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
        editCabStateCode = findViewById(R.id.editCabStateCode);
        editCabRtoNumber = findViewById(R.id.editCabRtoNumber);
        editCabSerialNumberOne = findViewById(R.id.editCabSerialNumberOne);
        editCabSerialNumberTwo = findViewById(R.id.editCabSerialNumberTwo);

        Button buttonVerifyCabNumberAndPackageVendor = findViewById(R.id.buttonVerifyCabNumberAndPackageVendor);

        /*Setting font for all the views*/
        textCabNumber.setTypeface(Constants.setLatoBoldFont(this));
        editCabStateCode.setTypeface(Constants.setLatoRegularFont(this));
        editCabRtoNumber.setTypeface(Constants.setLatoRegularFont(this));
        editCabSerialNumberOne.setTypeface(Constants.setLatoRegularFont(this));
        editCabSerialNumberTwo.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyCabNumberAndPackageVendor.setTypeface(Constants.setLatoLightFont(this));

        /*Setting events for Cab Number edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyCabNumberAndPackageVendor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (isAllFieldsFilled(new EditText[]{editCabStateCode, editCabRtoNumber, editCabSerialNumberOne, editCabSerialNumberTwo})) {
            String cabNumber = editCabStateCode.getText().toString().trim() + HYPHEN + editCabRtoNumber.getText().toString().trim()
                    + HYPHEN + editCabSerialNumberOne.getText().toString().trim() + HYPHEN + editCabSerialNumberTwo.getText().toString().trim();
            //We need Progress Indicator in this screen
            showProgressIndicator();
            checkDetailsInFirebase(cabNumber);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Once user enters details in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editCabStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabRtoNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editCabRtoNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabStateCode.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editCabSerialNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabRtoNumber.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabSerialNumberTwo.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editCabSerialNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabSerialNumberOne.requestFocus();
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
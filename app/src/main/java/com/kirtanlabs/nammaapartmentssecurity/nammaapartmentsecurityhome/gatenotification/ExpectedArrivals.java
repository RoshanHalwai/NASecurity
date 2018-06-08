package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class ExpectedArrivals extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editCabNumberAndResidentMobileNumber;
    private int arrivalType;

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
        if (arrivalType == R.string.expected_package_arrivals)
            changeViewsText(textCabNumberAndResidentMobileNumber, buttonVerifyCabNumberAndPackageVendor);

        /*Setting onClickListener for view*/
        buttonVerifyCabNumberAndPackageVendor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        openExpectedArrivalsValidationStatus();
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
     * This method is invoked when user will click on Verify Cab driver or Verify Package vendor
     */
    private void openExpectedArrivalsValidationStatus() {
        boolean validationStatus = isValidCabDriverOrPackageVendor();
        Intent intent = new Intent(ExpectedArrivals.this, ExpectedArrivalsValidationStatus.class);
        intent.putExtra(Constants.SCREEN_TITLE, arrivalType);
        intent.putExtra(Constants.VALIDATION_STATUS, validationStatus);
        startActivity(intent);
        finish();
    }

    /**
     * This method is used to check whether User has booked this cab or not
     * and package vendor is valid or not
     *
     * @return it will return boolean value whether cab number is valid or not and package vendor is valid or not
     */
    private boolean isValidCabDriverOrPackageVendor() {
        String cabNumberOrResidentMobileNumber = editCabNumberAndResidentMobileNumber.getText().toString().trim();
        if (arrivalType == R.string.expected_cab_arrivals) {
            // TODO : To Change Cab number here
            return cabNumberOrResidentMobileNumber.equalsIgnoreCase("KA 04 G 1234");
        } else {
            // TODO : To Change Mobile number here
            return cabNumberOrResidentMobileNumber.equals("7895185103");
        }
    }
}

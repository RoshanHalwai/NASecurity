package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;

public class Expected_Arrivals extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private View expectedArrivalsValidationDialog;
    private AlertDialog dialog;
    private EditText editCabNumberAndResidentMobileNumber;
    private boolean validationStatus;
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
        switch (v.getId()) {
            case R.id.buttonVerifyCabNumberAndPackageVendor:
                validationStatus = isValidCabDriverOrPackageVendor();
                openExpectedArrivalsValidationDialog(validationStatus);
                break;

            case R.id.buttonAllowArrivalsAndEIntercom:
                dialog.cancel();
                if (validationStatus) {
                    Intent intentExpectedArrivals = new Intent(Expected_Arrivals.this, NammaApartmentSecurityHome.class);
                    intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentExpectedArrivals);
                } else {
                    Intent intentExpectedArrivals = new Intent(Expected_Arrivals.this, EIntercom.class);
                    intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentExpectedArrivals);
                    finish();
                }
                break;
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
     * @return it will return boolean value whether cab number is valid or not and package vendor is valid or not
     */
    private boolean isValidCabDriverOrPackageVendor() {
        boolean check;
        String cabNumberOrResidentMobileNumber = editCabNumberAndResidentMobileNumber.getText().toString().trim();
        if (arrivalType == R.string.expected_cab_arrivals) {
            // TODO : To Change Cab number here
            check = cabNumberOrResidentMobileNumber.equalsIgnoreCase("KA 04 G 1234");
        } else {
            // TODO : To Change Mobile number here
            check = cabNumberOrResidentMobileNumber.equals("7895185103");
        }
        return check;
    }

    /**
     * This method is invoked when user will click on Verify Cab driver or Verify Package vendor
     *
     * @param validationStatus- validation status of Cab driver or Package vendor
     */
    private void openExpectedArrivalsValidationDialog(boolean validationStatus) {
        expectedArrivalsValidationDialog = View.inflate(this, R.layout.layout_expected_arrivals_validation_dialog, null);

        /*Getting Id's for all the views*/
        LinearLayout layoutValidationSuccessful = expectedArrivalsValidationDialog.findViewById(R.id.layoutValidationSuccessful);
        LinearLayout layoutValidationFailed = expectedArrivalsValidationDialog.findViewById(R.id.layoutValidationFailed);
        TextView textBookedBy = expectedArrivalsValidationDialog.findViewById(R.id.textBookedBy);
        TextView textFlatNumber = expectedArrivalsValidationDialog.findViewById(R.id.textFlatNumber);
        TextView textDateToVisit = expectedArrivalsValidationDialog.findViewById(R.id.textDateToVisit);
        TextView textTimeToVisit = expectedArrivalsValidationDialog.findViewById(R.id.textTimeToVisit);
        TextView textBookedByValue = expectedArrivalsValidationDialog.findViewById(R.id.textBookedByValue);
        TextView textFlatNumberValue = expectedArrivalsValidationDialog.findViewById(R.id.textFlatNumberValue);
        TextView textDateToVisitValue = expectedArrivalsValidationDialog.findViewById(R.id.textDateToVisitValue);
        TextView textTimeToVisitValue = expectedArrivalsValidationDialog.findViewById(R.id.textTimeToVisitValue);
        TextView textDontAllowToEnter = expectedArrivalsValidationDialog.findViewById(R.id.textDontAllowToEnter);
        Button buttonAllowExpectedArrivalsAndEIntercom = expectedArrivalsValidationDialog.findViewById(R.id.buttonAllowArrivalsAndEIntercom);

        /*Setting fonts to the views*/
        textBookedBy.setTypeface(Constants.setLatoRegularFont(this));
        textFlatNumber.setTypeface(Constants.setLatoRegularFont(this));
        textDateToVisit.setTypeface(Constants.setLatoRegularFont(this));
        textTimeToVisit.setTypeface(Constants.setLatoRegularFont(this));
        textBookedByValue.setTypeface(Constants.setLatoBoldFont(this));
        textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(this));
        textDateToVisitValue.setTypeface(Constants.setLatoBoldFont(this));
        textTimeToVisitValue.setTypeface(Constants.setLatoBoldFont(this));
        textDontAllowToEnter.setTypeface(Constants.setLatoBoldFont(this));
        buttonAllowExpectedArrivalsAndEIntercom.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same Dialog for Expected Cab and Package Arrivals we need to
         * change some Views Title*/
        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);

            String flatNumberTitle = getResources().getString(R.string.flat_number);
            flatNumberTitle = flatNumberTitle + ":";
            textFlatNumber.setText(flatNumberTitle);

            if (arrivalType == R.string.expected_package_arrivals) {
                changeExpectedArrivalsDialogViewsTitle(textBookedBy, buttonAllowExpectedArrivalsAndEIntercom);
            }
        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            if (arrivalType == R.string.expected_package_arrivals) {
                String dontAllowTo = getResources().getString(R.string.dont_allow_cab_to_enter);
                dontAllowTo = dontAllowTo.replace("Cab", "Package Vendor");
                textDontAllowToEnter.setText(dontAllowTo);
            }
            buttonAllowExpectedArrivalsAndEIntercom.setText(getResources().getText(R.string.e_intercom));
        }

        /*This method is used to create openExpectedArrivalsValidationDialog */
        createExpectedArrivalsValidationDialog();

        /*Setting onClickListener for view*/
        buttonAllowExpectedArrivalsAndEIntercom.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a expected arrivals validation dialog
     */
    private void createExpectedArrivalsValidationDialog() {
        AlertDialog.Builder alertVisitorValidationDialog = new AlertDialog.Builder(this);
        alertVisitorValidationDialog.setView(expectedArrivalsValidationDialog);
        dialog = alertVisitorValidationDialog.create();

        new Dialog(this);
        dialog.show();
    }

    /**
     * We update the textBookedBy Title and  Button AllowExpectedArrivalsAndEIntercom Text when this dialog is called in
     * Expected Package Arrivals screen
     *
     * @param textBookedBy                            - to update title in Expected Package Arrivals Screen
     * @param buttonAllowExpectedArrivalsAndEIntercom - to update text in Expected Package Arrivals Screen
     */
    private void changeExpectedArrivalsDialogViewsTitle(TextView textBookedBy, Button buttonAllowExpectedArrivalsAndEIntercom) {
        String nameTitle = getResources().getString(R.string.booked_by);
        nameTitle = nameTitle.replace("Booked", "Ordered");
        textBookedBy.setText(nameTitle);

        String allowTo = getResources().getString(R.string.allow_cab_driver);
        allowTo = allowTo.replace("Cab Driver", "Package Vendor");
        buttonAllowExpectedArrivalsAndEIntercom.setText(allowTo);
    }
}

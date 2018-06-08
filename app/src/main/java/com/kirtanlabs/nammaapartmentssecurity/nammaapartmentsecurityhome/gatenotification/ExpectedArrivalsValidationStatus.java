package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;

public class ExpectedArrivalsValidationStatus extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private LinearLayout layoutValidationSuccessful;
    private LinearLayout layoutValidationFailed;
    private TextView textBookedBy;
    private TextView textDontAllowToEnter;
    private TextView textFlatNumber;
    private Button buttonAllowExpectedArrivalsAndEIntercom;
    private boolean validationStatus;
    private int validationStatusOf;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expected_arrivals_validation_status;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Cab Driver and Package Vendor Validation Status,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.expected_cab_arrivals) {
            validationStatusOf = R.string.cab_driver_validation_status;
        } else {
            validationStatusOf = R.string.package_vendor_validation_status;
        }
        return validationStatusOf;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        layoutValidationSuccessful = findViewById(R.id.layoutValidationSuccessful);
        layoutValidationFailed = findViewById(R.id.layoutValidationFailed);
        textBookedBy = findViewById(R.id.textBookedBy);
        textFlatNumber = findViewById(R.id.textFlatNumber);
        TextView textDateToVisit = findViewById(R.id.textDateToVisit);
        TextView textTimeToVisit = findViewById(R.id.textTimeToVisit);
        TextView textBookedByValue = findViewById(R.id.textBookedByValue);
        TextView textFlatNumberValue = findViewById(R.id.textFlatNumberValue);
        TextView textDateToVisitValue = findViewById(R.id.textDateToVisitValue);
        TextView textTimeToVisitValue = findViewById(R.id.textTimeToVisitValue);
        textDontAllowToEnter = findViewById(R.id.textDontAllowToEnter);
        buttonAllowExpectedArrivalsAndEIntercom = findViewById(R.id.buttonAllowArrivalsAndEIntercom);

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

        /*Method is used to display layout according to the validation type of the Cab driver and Package Vendor*/
        showValidationStatus();

        /*Setting onClickListener for view*/
        buttonAllowExpectedArrivalsAndEIntercom.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (validationStatus) {
            Intent intentExpectedArrivals = new Intent(ExpectedArrivalsValidationStatus.this, NammaApartmentSecurityHome.class);
            intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentExpectedArrivals);
        } else {
            Intent intentExpectedArrivals = new Intent(ExpectedArrivalsValidationStatus.this, EIntercom.class);
            intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentExpectedArrivals.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentExpectedArrivals);
            finish();
        }
    }

    /**
     * This method is invoked to display Validation Status of Cab Driver and Package Vendor
     */
    private void showValidationStatus() {
        validationStatus = getIntent().getBooleanExtra(Constants.VALIDATION_STATUS, false);

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);

            String flatNumberTitle = getResources().getString(R.string.flat_number);
            flatNumberTitle = flatNumberTitle + ":";
            textFlatNumber.setText(flatNumberTitle);

            if (validationStatusOf == R.string.package_vendor_validation_status) {
                /*Since we are using same layout for Cab Driver and Package Vendor Validation Status Screen
                 *we need to change some Views Title*/
                changeViewsTitle(textBookedBy, buttonAllowExpectedArrivalsAndEIntercom);
            }
        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            if (validationStatusOf == R.string.package_vendor_validation_status) {
                String dontAllowTo = getResources().getString(R.string.dont_allow_cab_to_enter);
                dontAllowTo = dontAllowTo.replace("Cab", "Package Vendor");
                textDontAllowToEnter.setText(dontAllowTo);
            }
            buttonAllowExpectedArrivalsAndEIntercom.setText(getResources().getText(R.string.e_intercom));
        }
    }

    /**
     * We update the textBookedBy Title and  Button AllowExpectedArrivalsAndEIntercom Text in
     * Package Vendor Validation Status Screen
     *
     * @param textBookedBy                            - to update title in Package Vendor Validation Status Screen
     * @param buttonAllowExpectedArrivalsAndEIntercom - to update text in Package Vendor Validation Status Screen
     */
    private void changeViewsTitle(TextView textBookedBy, Button buttonAllowExpectedArrivalsAndEIntercom) {
        String nameTitle = getResources().getString(R.string.booked_by);
        nameTitle = nameTitle.replace("Booked", "Ordered");
        textBookedBy.setText(nameTitle);

        String allowTo = getResources().getString(R.string.allow_cab_driver);
        allowTo = allowTo.replace("Cab Driver", "Package Vendor");
        buttonAllowExpectedArrivalsAndEIntercom.setText(allowTo);
    }
}

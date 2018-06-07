package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

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
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;

public class VisitorsAndDailyServicesValidation extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
    private View visitorValidationDialog;
    private AlertDialog dialog;
    private boolean validationStatus;
    private int validationType;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitors_and_daily_services_validation;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Visitors Validation and Daily Services validation,
         *we set the title based on the user click on Namma Apartments Security Home screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.visitors_validation) {
            validationType = R.string.visitors_validation;
        } else {
            validationType = R.string.daily_services_validation;
        }
        return validationType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonVerifyVisitorOrDailyServices = findViewById(R.id.buttonVerifyVisitorOrDailyServices);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyVisitorOrDailyServices.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same layout for Visitors and Daily services Validation we need to
         * set text for buttonVerifyVisitorOrDailyServices to either Verify Visitor or Verify Daily Services*/
        buttonVerifyVisitorOrDailyServices.setText(getVerifyVisitorOrDailyServicesText());

        /*Setting onClickListener for view*/
        buttonVerifyVisitorOrDailyServices.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyVisitorOrDailyServices:
                String mobileNumber = editMobileNumber.getText().toString().trim();
                validationStatus = isValidMobileNumber(mobileNumber);
                openVisitorValidationDialog(validationStatus);
                break;
            case R.id.buttonAllowVisitorsAndEIntercom:
                dialog.cancel();
                if (validationStatus) {
                    finish();
                } else {
                    startActivity(new Intent(VisitorsAndDailyServicesValidation.this, EIntercom.class));
                    finish();
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private int getVerifyVisitorOrDailyServicesText() {
        if (validationType == R.string.visitors_validation) {
            return R.string.verify_visitor;
        } else {
            return R.string.verify_daily_services;
        }
    }

    /**
     * This method is invoked when user will click on Verify Visitors
     *
     * @param validationStatus - validation status of visitor
     */
    private void openVisitorValidationDialog(boolean validationStatus) {
        visitorValidationDialog = View.inflate(this, R.layout.layout_visitor_validation_dialog, null);

        /*Getting Id's for all the views*/
        LinearLayout layoutValidationSuccessful = visitorValidationDialog.findViewById(R.id.layoutValidationSuccessful);
        LinearLayout layoutValidationFailed = visitorValidationDialog.findViewById(R.id.layoutValidationFailed);
        TextView textVisitorOrDailyServiceName = visitorValidationDialog.findViewById(R.id.textVisitorOrDailyServiceName);
        TextView textFlatToVisit = visitorValidationDialog.findViewById(R.id.textFlatToVisit);
        TextView textInvitedBy = visitorValidationDialog.findViewById(R.id.textInvitedBy);
        TextView textVisitorOrDailyServiceNameValue = visitorValidationDialog.findViewById(R.id.textVisitorOrDailyServiceNameValue);
        TextView textFlatToVisitValue = visitorValidationDialog.findViewById(R.id.textFlatToVisitValue);
        TextView textInvitedByValue = visitorValidationDialog.findViewById(R.id.textInvitedByValue);
        TextView textInvalidVisitorOrDailyService = visitorValidationDialog.findViewById(R.id.textInvalidVisitorOrDailyServices);
        Button buttonAllowVisitorsAndEIntercom = visitorValidationDialog.findViewById(R.id.buttonAllowVisitorsAndEIntercom);

        /*Setting fonts to the views*/
        textVisitorOrDailyServiceName.setTypeface(Constants.setLatoRegularFont(this));
        textFlatToVisit.setTypeface(Constants.setLatoRegularFont(this));
        textInvitedBy.setTypeface(Constants.setLatoRegularFont(this));
        textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedByValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvalidVisitorOrDailyService.setTypeface(Constants.setLatoBoldFont(this));
        buttonAllowVisitorsAndEIntercom.setTypeface(Constants.setLatoLightFont(this));

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);

            /*Since we are using same Dialog for Visitors and Daily services Validation we need to
             * we need to change some Views Title*/
            if (validationType == R.string.daily_services_validation) {
                changeViewsTitle(textVisitorOrDailyServiceName, buttonAllowVisitorsAndEIntercom);
                textInvitedBy.setVisibility(View.GONE);
                textInvitedByValue.setVisibility(View.GONE);
            }
        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            if (validationType == R.string.daily_services_validation) {
                String invalidText = getResources().getString(R.string.invalid_visitor);
                invalidText = invalidText.replace("Visitor", "Daily Service");
                textInvalidVisitorOrDailyService.setText(invalidText);
            }
            buttonAllowVisitorsAndEIntercom.setText(getResources().getText(R.string.e_intercom));
        }

        /*This method is used to create openVisitorValidationDialog */
        createVisitorValidationDialog();

        /*Setting onClickListener for view*/
        buttonAllowVisitorsAndEIntercom.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a Visitor Validation dialog.
     */
    private void createVisitorValidationDialog() {
        AlertDialog.Builder alertVisitorValidationDialog = new AlertDialog.Builder(this);
        alertVisitorValidationDialog.setView(visitorValidationDialog);
        dialog = alertVisitorValidationDialog.create();

        new Dialog(this);
        dialog.show();
    }

    /**
     * We update the VisitorOrDailyServiceName Title and  Button AllowVisitorsAndEIntercom Text when this dialog is called in
     * Daily Services Validation screen
     *
     * @param textVisitorOrDailyServiceName   - to update title in Daily Services Validation Screen
     * @param buttonAllowVisitorsAndEIntercom - to update text in Daily Services Validation Screen
     */
    private void changeViewsTitle(TextView textVisitorOrDailyServiceName, Button buttonAllowVisitorsAndEIntercom) {
        String nameTitle = getResources().getString(R.string.visitor_name);
        nameTitle = nameTitle.substring(8);
        textVisitorOrDailyServiceName.setText(nameTitle);

        String allowTo = getResources().getString(R.string.allow_visitor);
        allowTo = allowTo.replace("Visitor", "Daily Service");
        buttonAllowVisitorsAndEIntercom.setText(allowTo);
    }
}

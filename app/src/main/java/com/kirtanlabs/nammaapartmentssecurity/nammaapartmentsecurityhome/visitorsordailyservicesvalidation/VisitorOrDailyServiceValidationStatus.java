package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;

public class VisitorOrDailyServiceValidationStatus extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private LinearLayout layoutValidationSuccessful;
    private LinearLayout layoutValidationFailed;
    private Button buttonAllowVisitorsAndEIntercom;
    private TextView textVisitorOrDailyServiceName;
    private TextView textInvalidVisitorOrDailyService;
    private TextView textInvitedBy;
    private TextView textInvitedByValue;
    private boolean validationStatus;
    private int validationStatusOf;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitor_or_daily_service_validation_status;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Visitors and Daily Services Validation Status,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.visitors_validation) {
            validationStatusOf = R.string.visitor_validation_status;
        } else {
            validationStatusOf = R.string.daily_service_validation_status;
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
        textVisitorOrDailyServiceName = findViewById(R.id.textVisitorOrDailyServiceName);
        TextView textFlatToVisit = findViewById(R.id.textFlatToVisit);
        textInvitedBy = findViewById(R.id.textInvitedBy);
        TextView textVisitorOrDailyServiceNameValue = findViewById(R.id.textVisitorOrDailyServiceNameValue);
        TextView textFlatToVisitValue = findViewById(R.id.textFlatToVisitValue);
        textInvitedByValue = findViewById(R.id.textInvitedByValue);
        textInvalidVisitorOrDailyService = findViewById(R.id.textInvalidVisitorOrDailyServices);
        buttonAllowVisitorsAndEIntercom = findViewById(R.id.buttonAllowVisitorsAndEIntercom);

        /*Setting fonts to the views*/
        textVisitorOrDailyServiceName.setTypeface(Constants.setLatoRegularFont(this));
        textFlatToVisit.setTypeface(Constants.setLatoRegularFont(this));
        textInvitedBy.setTypeface(Constants.setLatoRegularFont(this));
        textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedByValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvalidVisitorOrDailyService.setTypeface(Constants.setLatoBoldFont(this));
        buttonAllowVisitorsAndEIntercom.setTypeface(Constants.setLatoLightFont(this));

        /*Method is used to display layout according to the validation type of the Visitor and Daily Services*/
        showValidationStatus();

        /*Setting onClickListener for view*/
        buttonAllowVisitorsAndEIntercom.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (validationStatus) {
            finish();
        } else {
            startActivity(new Intent(VisitorOrDailyServiceValidationStatus.this, EIntercom.class));
            finish();
        }
    }

    /**
     * This method is invoked to display Validation Status of Visitors and Daily Services
     */
    private void showValidationStatus() {
        validationStatus = getIntent().getBooleanExtra(Constants.VALIDATION_STATUS, false);

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);
            if (validationStatusOf == R.string.daily_service_validation_status) {
                changeViewsTitle(textVisitorOrDailyServiceName, buttonAllowVisitorsAndEIntercom);
                textInvitedBy.setVisibility(View.GONE);
                textInvitedByValue.setVisibility(View.GONE);
            }
        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            if (validationStatusOf == R.string.daily_service_validation_status) {
                String invalidText = getResources().getString(R.string.invalid_visitor);
                invalidText = invalidText.replace("Visitor", "Daily Service");
                textInvalidVisitorOrDailyService.setText(invalidText);
            }
            buttonAllowVisitorsAndEIntercom.setText(getResources().getText(R.string.e_intercom));
        }
    }

    /**
     * We update the VisitorOrDailyServiceName Title and  Button AllowVisitorsAndEIntercom Text when in
     * Daily Services Validation Status screen
     *
     * @param textVisitorOrDailyServiceName   - to update title in Daily Services Validation Status Screen
     * @param buttonAllowVisitorsAndEIntercom - to update text in Daily Services Validation Status Screen
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

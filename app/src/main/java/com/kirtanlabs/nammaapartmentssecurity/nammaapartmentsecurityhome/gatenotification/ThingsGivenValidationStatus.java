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

public class ThingsGivenValidationStatus extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private LinearLayout layoutValidationSuccessful;
    private LinearLayout layoutValidationFailed;
    private TextView textHasGivenThings;
    private TextView textNotGivenThings;
    private Button buttonOk;
    private int validationStatusOf;
    private boolean validationStatus;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_things_given_validation_status;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Visitors Validation and Daily Services validation Status,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.things_given_to_guest) {
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
        textHasGivenThings = findViewById(R.id.textHasGivenThings);
        textNotGivenThings = findViewById(R.id.textNotGivenThings);
        buttonOk = findViewById(R.id.buttonOk);

        /*Setting fonts to the views*/
        textHasGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        textNotGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        buttonOk.setTypeface(Constants.setLatoLightFont(this));

        /*Method is used to display layout according to the validation type of the visitor and daily services*/
        showValidationStatus();

        /*Setting onClickListener for view*/
        buttonOk.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (validationStatus) {
            Intent intentThingsGiven = new Intent(ThingsGivenValidationStatus.this, NammaApartmentSecurityHome.class);
            intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentThingsGiven);
        } else {
            Intent intentThingsGiven = new Intent(ThingsGivenValidationStatus.this, EIntercom.class);
            intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentThingsGiven);
            finish();
        }
    }

    /**
     * This method is invoked to display Validation Status of Visitor and Daily Services whether user has given thing to them or not
     */
    private void showValidationStatus() {
        validationStatus = getIntent().getBooleanExtra(Constants.VALIDATION_STATUS, false);

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);
            if (validationStatusOf == R.string.daily_service_validation_status) {
                String GivenThingsTo = getResources().getString(R.string.resident_has_given_things);
                GivenThingsTo = GivenThingsTo.replace("Visitor", "Daily Service");
                textHasGivenThings.setText(GivenThingsTo);
            }

        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            buttonOk.setText(R.string.e_intercom);
            if (validationStatusOf == R.string.daily_service_validation_status) {
                String notGivenThingsTo = getResources().getString(R.string.resident_has_not_given_things);
                notGivenThingsTo = notGivenThingsTo.replace("Visitor", "Daily Service");
                textNotGivenThings.setText(notGivenThingsTo);
            }
        }
    }
}

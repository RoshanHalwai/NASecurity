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

public class Things_Given extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private View givenThingsDialog;
    private AlertDialog dialog;
    private EditText editMobileNumber;
    private int givenThingsTo;
    private boolean validationStatus;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_things_given;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Given Things to Guest and Given Things to Daily Services
         *we set the title based on the user click on Gate Notification screen*/
        if (getIntent().getIntExtra(Constants.GIVEN_THINGS_TO, 0) == R.string.things_given_to_guest) {
            givenThingsTo = R.string.things_given_to_guest;
        } else {
            givenThingsTo = R.string.things_given_to_daily_services;
        }
        return givenThingsTo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonVerifyThings = findViewById(R.id.buttonVerifyThings);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyThings.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonVerifyThings.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyThings:
                String mobileNumber = editMobileNumber.getText().toString().trim();
                validationStatus = isValidMobileNumber(mobileNumber);
                openVerifyThingsDialog(validationStatus);
                break;
            case R.id.buttonOk:
                dialog.cancel();
                if (validationStatus) {
                    Intent intentThingsGiven = new Intent(Things_Given.this, NammaApartmentSecurityHome.class);
                    intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentThingsGiven);
                } else {
                    Intent intentThingsGiven = new Intent(Things_Given.this, EIntercom.class);
                    intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentThingsGiven);
                    finish();
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user will click on Verify things
     *
     * @param validationStatus - validation status of Visitor and Daily service
     */
    private void openVerifyThingsDialog(boolean validationStatus) {
        givenThingsDialog = View.inflate(this, R.layout.layout_things_given_dialog, null);
        LinearLayout layoutValidationSuccessful = givenThingsDialog.findViewById(R.id.layoutValidationSuccessful);
        LinearLayout layoutValidationFailed = givenThingsDialog.findViewById(R.id.layoutValidationFailed);
        TextView textHasGivenThings = givenThingsDialog.findViewById(R.id.textHasGivenThings);
        TextView textNotGivenThings = givenThingsDialog.findViewById(R.id.textNotGivenThings);
        Button buttonOk = givenThingsDialog.findViewById(R.id.buttonOk);

        /*Setting fonts to the views*/
        textHasGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        textNotGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        buttonOk.setTypeface(Constants.setLatoLightFont(this));

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);
            if (givenThingsTo == R.string.things_given_to_daily_services) {
                String GivenThingsTo = getResources().getString(R.string.resident_has_given_things);
                GivenThingsTo = GivenThingsTo.replace("Visitor", "Daily Service");
                textHasGivenThings.setText(GivenThingsTo);
            }

        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
            buttonOk.setText(R.string.e_intercom);
            if (givenThingsTo == R.string.things_given_to_daily_services) {
                String notGivenThingsTo = getResources().getString(R.string.resident_has_not_given_things);
                notGivenThingsTo = notGivenThingsTo.replace("Visitor", "Daily Service");
                textNotGivenThings.setText(notGivenThingsTo);
            }
        }

        /*This method is used to create createVerifyThingsDialog*/
        createVerifyThingsDialog();

        /*Setting onClickListener for view*/
        buttonOk.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a create Verify Things dialog
     */
    private void createVerifyThingsDialog() {
        AlertDialog.Builder alertVerifyThings = new AlertDialog.Builder(this);
        alertVerifyThings.setView(givenThingsDialog);

        dialog = alertVerifyThings.create();
        new Dialog(this);
        dialog.show();
    }
}

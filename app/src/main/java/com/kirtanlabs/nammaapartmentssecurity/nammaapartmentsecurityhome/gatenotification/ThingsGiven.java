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

public class ThingsGiven extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
    private int givenThingsTo;

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
        openThingsGivenValidationStatus();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user will click on Verify things
     */
    private void openThingsGivenValidationStatus() {
        String mobileNumber = editMobileNumber.getText().toString().trim();
        boolean validationStatus = isValidMobileNumber(mobileNumber);

        Intent intent = new Intent(ThingsGiven.this, ThingsGivenValidationStatus.class);
        intent.putExtra(Constants.SCREEN_TITLE, givenThingsTo);
        intent.putExtra(Constants.VALIDATION_STATUS, validationStatus);
        startActivity(intent);
        finish();
    }
}

package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;

public class ResidentHasGivenThings extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private int thingsGivenTo;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_resident_has_given_things;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Visitors Validation and Daily Services validation Status,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.things_given_to_guest) {
            thingsGivenTo = R.string.things_given_to_guest;
        } else {
            thingsGivenTo = R.string.things_given_to_daily_services;
        }
        return thingsGivenTo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textHasGivenThings = findViewById(R.id.textHasGivenThings);
        Button buttonOk = findViewById(R.id.buttonOk);

        /*Setting fonts to the views*/
        textHasGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        buttonOk.setTypeface(Constants.setLatoLightFont(this));

        if (thingsGivenTo == R.string.things_given_to_daily_services) {
            String GivenThingsTo = getString(R.string.resident_has_given_things_to_daily_service);
            textHasGivenThings.setText(GivenThingsTo);
        }

        /*Setting onClickListener for view*/
        buttonOk.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        updateStatus();
        Intent intentThingsGiven = new Intent(ResidentHasGivenThings.this, NammaApartmentSecurityHome.class);
        intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentThingsGiven);
    }

    /**
     * This method is invoked to change status of Visitor and Daily Service.
     */
    private void updateStatus() {
        String visitorOrDailyServiceUid = getIntent().getStringExtra(Constants.GUEST_UID);
        if (thingsGivenTo == R.string.things_given_to_guest) {
            DatabaseReference visitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                    .child(visitorOrDailyServiceUid)
                    .child(Constants.FIREBASE_CHILD_STATUS);
            visitorReference.setValue(getString(R.string.left));
        } else {
            String serviceType = getIntent().getStringExtra(Constants.SERVICE_TYPE);
            DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE
                    .child(serviceType)
                    .child(visitorOrDailyServiceUid)
                    .child(Constants.FIREBASE_CHILD_STATUS);
            dailyServiceReference.setValue(getString(R.string.left));
        }
    }
}

package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class VisitorsAndDailyServicesValidation extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMobileNumber;
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
         *we set the title based on the user click on NammaApartments Security Home screen*/
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
        String mobileNumber = editMobileNumber.getText().toString().trim();
        if (isValidPhone(mobileNumber)) {
            /*We need Progress Indicator in this screen*/
            showProgressIndicator();
            checkMobileNumberInFirebase(mobileNumber);
        } else {
            editMobileNumber.setError(getString(R.string.number_10digit_validation));
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
     * This method is invoked when user will click on Verify Visitor Or DailyServices
     *
     * @param mobileNumber - that need to be checked whether it is present in Firebase or not
     */
    private void checkMobileNumberInFirebase(final String mobileNumber) {
        if (validationType == R.string.visitors_validation) {
            Constants.PREAPPROVED_VISITORS_MOBILE_REFERENCE
                    .child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        String visitorUid = (String) dataSnapshot.getValue();
                        Intent intent = new Intent(VisitorsAndDailyServicesValidation.this, VisitorAndDailyServiceList.class);
                        intent.putExtra(Constants.SCREEN_TITLE, validationType);
                        intent.putExtra(Constants.FIREBASE_CHILD_VISITOR_UID, visitorUid);
                        startActivity(intent);
                        finish();
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.invalid_visitor));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Constants.PRIVATE_DAILYSERVICES_REFERENCE
                    .child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.exists()) {
                        String dailyServiceUid = (String) dataSnapshot.getValue();
                        Intent intent = new Intent(VisitorsAndDailyServicesValidation.this, VisitorAndDailyServiceList.class);
                        intent.putExtra(Constants.SCREEN_TITLE, validationType);
                        intent.putExtra(Constants.FIREBASE_CHILD_DAILYSERVICE_UID, dailyServiceUid);
                        startActivity(intent);
                        finish();
                    } else {
                        String invalidDailyServices = getString(R.string.invalid_visitor);
                        invalidDailyServices = invalidDailyServices.replace("Visitor", "Daily Services");
                        openValidationStatusDialog(Constants.FAILED, invalidDailyServices);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

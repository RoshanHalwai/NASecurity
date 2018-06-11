package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
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
        /*We need Progress Indicator in this screen*/
        showProgressIndicator();
        checkMobileNumberInFirebase(editMobileNumber.getText().toString().trim());
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
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_VISITORS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER)
                .child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String visitorUid = null;
                    for (DataSnapshot visitorUidDataSnapshot : dataSnapshot.getChildren()) {
                        visitorUid = visitorUidDataSnapshot.getKey();
                    }
                    Intent intent = new Intent(VisitorsAndDailyServicesValidation.this, VisitorAndDailyServiceList.class);
                    intent.putExtra(Constants.SCREEN_TITLE, validationType);
                    intent.putExtra(Constants.FIREBASE_CHILD_VISITOR_UID, visitorUid);
                    startActivity(intent);
                    finish();
                } else {
                    if (validationType == R.string.visitors_validation) {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.invalid_visitor));
                    } else {
                        String invalidDailyService = getString(R.string.invalid_visitor);
                        invalidDailyService = invalidDailyService.replace("Visitor", "Daily Service");
                        openValidationStatusDialog(Constants.FAILED, invalidDailyService);
                    }
                }
                hideProgressIndicator();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

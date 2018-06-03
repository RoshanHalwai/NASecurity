package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsvalidation;

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

public class VisitorsValidation extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private EditText editMobileNumber;
    private View visitorValidationDialog;
    private AlertDialog dialog;
    private boolean validationStatus;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitors_validation;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.visitors_validation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonVerifyVisitor = findViewById(R.id.buttonVerifyVisitor);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyVisitor.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonVerifyVisitor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyVisitor:
                validationStatus = isValidVisitors();
                openVisitorValidationDialog(validationStatus);
                break;
            case R.id.buttonAllowVisitorsAndEIntercom:
                dialog.cancel();
                if (validationStatus) {
                    finish();
                } else {
                    startActivity(new Intent(VisitorsValidation.this, EIntercom.class));
                    finish();
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether visitor has given valid mobile number or not
     *
     * @return it will return boolean value whether number is valid or not
     */
    private boolean isValidVisitors() {
        boolean check;
        String mobileNumber = editMobileNumber.getText().toString().trim();
        // TODO : To Change mobile number here
        check = mobileNumber.equals("7895185103");
        return check;
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
        TextView textVisitorName = visitorValidationDialog.findViewById(R.id.textVisitorName);
        TextView textFlatToVisit = visitorValidationDialog.findViewById(R.id.textFlatToVisit);
        TextView textInvitedBy = visitorValidationDialog.findViewById(R.id.textInvitedBy);
        TextView textVisitorNameValue = visitorValidationDialog.findViewById(R.id.textVisitorNameValue);
        TextView textFlatToVisitValue = visitorValidationDialog.findViewById(R.id.textFlatToVisitValue);
        TextView textInvitedByValue = visitorValidationDialog.findViewById(R.id.textInvitedByValue);
        TextView textInvalidVisitor = visitorValidationDialog.findViewById(R.id.textInvalidVisitor);
        Button buttonAllowVisitorsAndEIntercom = visitorValidationDialog.findViewById(R.id.buttonAllowVisitorsAndEIntercom);

        /*Setting fonts to the views*/
        textVisitorName.setTypeface(Constants.setLatoRegularFont(this));
        textFlatToVisit.setTypeface(Constants.setLatoRegularFont(this));
        textInvitedBy.setTypeface(Constants.setLatoRegularFont(this));
        textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedByValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvalidVisitor.setTypeface(Constants.setLatoBoldFont(this));
        buttonAllowVisitorsAndEIntercom.setTypeface(Constants.setLatoLightFont(this));

        if (validationStatus) {
            layoutValidationSuccessful.setVisibility(View.VISIBLE);
        } else {
            layoutValidationFailed.setVisibility(View.VISIBLE);
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
}

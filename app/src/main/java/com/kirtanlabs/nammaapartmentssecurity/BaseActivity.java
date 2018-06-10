package com.kirtanlabs.nammaapartmentssecurity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;

public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private ImageView backButton;
    private View validationDialog;

    /* ------------------------------------------------------------- *
     * Abstract Methods
     * ------------------------------------------------------------- */

    protected abstract int getLayoutResourceId();

    protected abstract int getActivityTitle();

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void setActivityTitle(int resourceId) {
        TextView activityTitle = findViewById(R.id.textActivityTitle);
        activityTitle.setTypeface(Constants.setLatoRegularFont(this));
        activityTitle.setText(resourceId);
    }

    private void setBackButtonListener() {
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    protected void hideBackButton() {
        backButton.setVisibility(View.INVISIBLE);
    }


    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        backButton = findViewById(R.id.backButton);
        setActivityTitle(getActivityTitle());
        setBackButtonListener();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to create a Validation dialog.
     */
    private void createValidationStatusDialog() {
        AlertDialog.Builder alertValidationDialog = new AlertDialog.Builder(this);
        alertValidationDialog.setView(validationDialog);
        AlertDialog dialog = alertValidationDialog.create();

        new Dialog(this);
        dialog.show();
    }

    /* ------------------------------------------------------------- *
     * Protected Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether visitor or daily services has given his own valid mobile number or not
     *
     * @param mobileNumber - mobile number of Visitor or Daily services
     * @return it will return boolean value whether number is valid or not
     */
    protected boolean isValidMobileNumber(String mobileNumber) {
        // TODO : To Change mobile number here
        return mobileNumber.equals("7895185103");
    }

    /**
     * This method is used to open dialog box according to validation status
     *
     * @param dialogType  - consists of type dialog box in to display
     * @param warningText - consists of message to display in dialog box
     */
    protected void openValidationStatusDialog(String dialogType, String warningText) {
        validationDialog = View.inflate(this, R.layout.layout_validation_type_dialog, null);

        /*Getting Id's for all the views*/
        ImageView imageSuccess = validationDialog.findViewById(R.id.imageSuccess);
        ImageView imageFailed = validationDialog.findViewById(R.id.imageFailed);
        ImageView imageWarning = validationDialog.findViewById(R.id.imageWarning);
        TextView textDescription = validationDialog.findViewById(R.id.textDescription);
        Button buttonEIntercom = validationDialog.findViewById(R.id.buttonEIntercom);

        /*Setting font for all the views*/
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        buttonEIntercom.setTypeface(Constants.setLatoLightFont(this));

        /*Displaying text according to Dialog type type*/
        textDescription.setText(warningText);

        switch (dialogType) {
            case Constants.SUCCESS:
                imageSuccess.setVisibility(View.VISIBLE);
                break;
            case Constants.FAILED:
                imageFailed.setVisibility(View.VISIBLE);
                break;
            case Constants.WARNING:
                imageWarning.setVisibility(View.VISIBLE);
                break;
        }

        /*Setting onClickListener for view*/
        buttonEIntercom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, EIntercom.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        /*This method is used to create ValidationDialog */
        createValidationStatusDialog();
    }
}

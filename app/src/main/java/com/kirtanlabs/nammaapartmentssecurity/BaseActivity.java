package com.kirtanlabs.nammaapartmentssecurity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.PHONE_NUMBER_MAX_LENGTH;

public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private ImageView backButton;
    private View validationDialog;
    private AlertDialog dialog;
    private Calendar calendar;
    private AVLoadingIndicatorView progressIndicator;

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
        dialog = alertValidationDialog.create();

        new Dialog(this);
        dialog.show();
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to open dialog box according to validation status
     *
     * @param dialogType  - consists of type dialog box in to display
     * @param warningText - consists of message to display in dialog box
     */
    public void openValidationStatusDialog(final String dialogType, final String warningText) {
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
                dialog.cancel();
                Intent intent = new Intent(BaseActivity.this, EIntercom.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        /*This method is used to create ValidationDialog */
        createValidationStatusDialog();
    }

    public void showProgressIndicator() {
        progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.smoothToShow();
    }

    public void hideProgressIndicator() {
        if (progressIndicator == null)
            progressIndicator = findViewById(R.id.animationWaitingToLoadData);
        progressIndicator.smoothToHide();
    }

    /**
     * This method checks if user is entering proper phone number or not.
     *
     * @param phone consists of string value of mobile number.
     * @return returns a boolean variable based on the context.
     */
    public boolean isValidPhone(String phone) {
        return !Pattern.matches("[a-zA-Z]+", phone) && phone.length() == PHONE_NUMBER_MAX_LENGTH;
    }

    /**
     * This method is invoked to change status of Visitors, Daily Service and Expected Arrivals
     *
     * @param status          - current status of Visitors, Daily Services and Expected Arrivals
     * @param statusReference - Database Reference of Visitors, Daily Services and Expected Arrivals
     */
    public void changeStatus(final String status, final DatabaseReference statusReference) {
        if (status.equals(getString(R.string.not_entered))) {
            statusReference.setValue(getString(R.string.entered));
        } else if (status.equals(getString(R.string.entered))) {
            statusReference.setValue(getString(R.string.left));
        }
    }

    /**
     * This method gets invoked when user is trying to enter improper format of entering name.
     *
     * @param name contains that particular editText of name
     * @throws NumberFormatException because if user tries to enter number in place of name.
     */
    protected boolean isValidPersonName(String name) throws NumberFormatException {
        boolean check;
        check = !Pattern.matches("[a-zA-Z ]+", name);
        return check;
    }

    /**
     * This method checks if all the editTexts are filled or not.
     *
     * @param fields consists of array of EditTexts.
     * @return consists of boolean variable based on the context.
     */
    protected boolean isAllFieldsFilled(EditText[] fields) {
        for (EditText currentField : fields) {
            if (TextUtils.isEmpty(currentField.getText().toString())) {
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     *  This method is invoked to get Current date
     */
    public String getCurrentDate() {
        calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        return new DateFormatSymbols().getMonths()[currentMonth].substring(0, 3) + " " + currentDay + ", " + currentYear;
    }

    /**
     * This method is invoked to get Current time
     *
     * @return - current time
     */
    public String getCurrentTime() {
        calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
    }
}

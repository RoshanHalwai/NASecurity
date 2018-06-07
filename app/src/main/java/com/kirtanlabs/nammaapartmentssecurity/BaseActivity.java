package com.kirtanlabs.nammaapartmentssecurity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private ImageView backButton;

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
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether visitor or daily services has given his own valid mobile number or not
     *
     * @param mobileNumber - mobile number of Visitor or Daily services
     * @return it will return boolean value whether number is valid or not
     */
    public boolean isValidMobileNumber(String mobileNumber) {
        boolean check;
        // TODO : To Change mobile number here
        check = mobileNumber.equals("7895185103");
        return check;
    }
}

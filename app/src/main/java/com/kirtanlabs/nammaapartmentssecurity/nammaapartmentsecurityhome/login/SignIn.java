package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

public class SignIn extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------------
     *  Private Members
     *----------------------------------------------------*/

    private EditText editMobileNumber;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*Since this is Login Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoBoldFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
        buttonLogin.setTypeface(setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonLogin.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        String mobileNumber = editMobileNumber.getText().toString().trim();
        if (isPhoneNumberValid(mobileNumber)) {
            /*We send mobile number to OTP class for Validation*/
            Intent intent = new Intent(SignIn.this, OTP.class);
            intent.putExtra(Constants.MOBILE_NUMBER, mobileNumber);
            startActivity(intent);
            finish();
        } else {
            editMobileNumber.setError(getString(R.string.mobile_number_validation));
        }
    }
}

package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;

import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

public class OTP extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textResendOTPOrVerificationMessage;
    private TextView textChangeNumberOrTimer;
    private EditText editFirstOTPDigit;
    private EditText editSecondOTPDigit;
    private EditText editThirdOTPDigit;
    private EditText editFourthOTPDigit;
    private EditText editFifthOTPDigit;
    private EditText editSixthOTPDigit;
    private Button buttonVerifyOTP;
    private int RESEND_OTP_SECONDS;
    private int RESEND_OTP_MINUTE;

    /* ------------------------------------------------------------- *
     * Private Members for Phone Authentication
     * ------------------------------------------------------------- */

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String phoneVerificationId, userMobileNumber;

    /* ------------------------------------------------------------- *
     * Private Members for Firebase
     * ------------------------------------------------------------- */

    private FirebaseAuth fbAuth;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.phone_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbAuth = FirebaseAuth.getInstance();
        /* Generate an OTP to user's mobile number */
        userMobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
        sendOTP();

        /* Start the Resend OTP timer, valid for 120 seconds*/
        textResendOTPOrVerificationMessage = findViewById(R.id.textResendOTPOrVerificationMessage);
        textChangeNumberOrTimer = findViewById(R.id.textChangeNumberOrTimer);
        startResendOTPTimer();


        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textDescription = findViewById(R.id.textDescription);
        editFirstOTPDigit = findViewById(R.id.editFirstOTPDigit);
        editSecondOTPDigit = findViewById(R.id.editSecondOTPDigit);
        editThirdOTPDigit = findViewById(R.id.editThirdOTPDigit);
        editFourthOTPDigit = findViewById(R.id.editFourthOTPDigit);
        editFifthOTPDigit = findViewById(R.id.editFifthOTPDigit);
        editSixthOTPDigit = findViewById(R.id.editSixthOTPDigit);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        textResendOTPOrVerificationMessage = findViewById(R.id.textResendOTPOrVerificationMessage);
        textChangeNumberOrTimer = findViewById(R.id.textChangeNumberOrTimer);

        /*Setting font for all the views*/
        textDescription.setTypeface(setLatoRegularFont(this));
        editFirstOTPDigit.setTypeface(setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(setLatoLightFont(this));

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting onClickListener for view*/
        buttonVerifyOTP.setOnClickListener(this);
        textResendOTPOrVerificationMessage.setOnClickListener(v -> resendOTP());
        textChangeNumberOrTimer.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyOTP:
                boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{
                        editFirstOTPDigit,
                        editSecondOTPDigit,
                        editThirdOTPDigit,
                        editFourthOTPDigit,
                        editFifthOTPDigit,
                        editSixthOTPDigit
                });

                if (allFieldsFilled) {

                    /*displaying progress dialog while OTP is being validated*/
                    showProgressDialog(this,
                            getResources().getString(R.string.verifying_account),
                            getResources().getString(R.string.please_wait_a_moment));

                    hideKeyboard();
                    String code = editFirstOTPDigit.getText().toString() + editSecondOTPDigit.getText().toString() +
                            editThirdOTPDigit.getText().toString() + editFourthOTPDigit.getText().toString() + editFifthOTPDigit.getText().toString() +
                            editSixthOTPDigit.getText().toString();
                    signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(phoneVerificationId, code));
                }
                break;
            case R.id.textChangeNumberOrTimer:
                startActivity(new Intent(OTP.this, SignIn.class));
                finish();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    private void sendOTP() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks);
    }

    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                /*displaying progress dialog while OTP is being validated*/
                showProgressDialog(OTP.this,
                        getResources().getString(R.string.verifying_account),
                        getResources().getString(R.string.please_wait_a_moment));

                /*Hiding the Keyboard in case the Auto-Verification is completed*/
                hideKeyboard();
                textResendOTPOrVerificationMessage.setText(R.string.auto_verification_completed);
                textResendOTPOrVerificationMessage.setEnabled(false);
                textChangeNumberOrTimer.setVisibility(View.INVISIBLE);
                if (phoneAuthCredential.getSmsCode() != null) {
                    char[] smsCode = phoneAuthCredential.getSmsCode().toCharArray();
                    editFirstOTPDigit.setText(String.valueOf(smsCode[0]));
                    editSecondOTPDigit.setText(String.valueOf(smsCode[1]));
                    editThirdOTPDigit.setText(String.valueOf(smsCode[2]));
                    editFourthOTPDigit.setText(String.valueOf(smsCode[3]));
                    editFifthOTPDigit.setText(String.valueOf(smsCode[4]));
                    editSixthOTPDigit.setText(String.valueOf(smsCode[5]));
                }
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId = s;
                resendToken = forceResendingToken;
            }

        };
    }

    /**
     * Checking if user's mobile number exists in Firebase or not
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        fbAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, (task) -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        DatabaseReference guardReference = Constants.ALL_SECURITY_GUARDS_REFERENCE.child(userMobileNumber);
                        guardReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                /* Check if User mobile number is found in database */
                                if (dataSnapshot.exists()) {
                                    startActivity(new Intent(OTP.this, NammaApartmentSecurityHome.class));
                                    finish();
                                } else {
                                    Intent intent = new Intent(OTP.this, SignIn.class);
                                    showNotificationDialog(getString(R.string.login_error_message), getString(R.string.mobile_number_found), intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        /*Check if network is available or not*/
                        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (!isConnected) {
                            /*Show this message if user is having no network connection*/
                            textResendOTPOrVerificationMessage.setText(R.string.check_network_connection);
                        } else {
                            /*Show this message if user has entered wrong OTP*/
                            textResendOTPOrVerificationMessage.setText(R.string.wrong_otp_entered);
                        }
                    }
                });
    }

    /**
     * Resend OTP if the user doesn't receive after 120 seconds
     */
    private void resendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
        startResendOTPTimer();
    }

    private void startResendOTPTimer() {
        textResendOTPOrVerificationMessage.setText(R.string.waiting_for_otp);
        RESEND_OTP_MINUTE = 1;
        RESEND_OTP_SECONDS = 59;
        String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
        textChangeNumberOrTimer.setText(timer);
        textChangeNumberOrTimer.setEnabled(false);
        textResendOTPOrVerificationMessage.setEnabled(false);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    RESEND_OTP_SECONDS -= 1;
                    String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                    textChangeNumberOrTimer.setText(timer);
                    if (RESEND_OTP_MINUTE == 0 && RESEND_OTP_SECONDS == 0) {
                        t.cancel();

                        /*User can either change their mobile number or Resend OTP to their mobile number*/
                        textChangeNumberOrTimer.setText(R.string.change_mobile_number);
                        textResendOTPOrVerificationMessage.setText(R.string.resend_otp);
                        textResendOTPOrVerificationMessage.setEnabled(true);
                        textChangeNumberOrTimer.setEnabled(true);
                    } else if (RESEND_OTP_SECONDS == 0) {
                        timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                        textChangeNumberOrTimer.setText(timer);
                        RESEND_OTP_SECONDS = 60;
                        RESEND_OTP_MINUTE = RESEND_OTP_MINUTE - 1;
                    }
                });
            }
        }, 0, 1000);
    }

    private String timeFormatter(int time) {
        return String.format(Locale.ENGLISH, "%02d", time % 60);
    }

    /**
     * Once user enters a digit in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editFirstOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > EDIT_TEXT_MIN_LENGTH) {
                    editSecondOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editSecondOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == EDIT_TEXT_MIN_LENGTH) {
                    editFirstOTPDigit.requestFocus();
                } else {
                    editThirdOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editThirdOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == EDIT_TEXT_MIN_LENGTH) {
                    editSecondOTPDigit.requestFocus();
                } else {
                    editFourthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editFourthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == EDIT_TEXT_MIN_LENGTH) {
                    editThirdOTPDigit.requestFocus();
                } else {
                    editFifthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editFifthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == EDIT_TEXT_MIN_LENGTH) {
                    editFourthOTPDigit.requestFocus();
                } else {
                    editSixthOTPDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editSixthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == EDIT_TEXT_MIN_LENGTH) {
                    editFifthOTPDigit.requestFocus();
                    buttonVerifyOTP.setVisibility(View.INVISIBLE);
                } else {
                    buttonVerifyOTP.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}

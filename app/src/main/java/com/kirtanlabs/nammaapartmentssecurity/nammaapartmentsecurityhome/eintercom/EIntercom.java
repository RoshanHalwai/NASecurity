package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.pojo.Notification;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_NUMBER_FIELD_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_MESSAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getCabMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getGuestMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getPackageMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_TAKE_PICTURE;

public class EIntercom extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private EditText editCabStateCode;
    private EditText editCabRtoNumber;
    private EditText editCabSerialNumberOne;
    private EditText editCabSerialNumberTwo;
    private String eIntercomType, visitorMobileNumber, reference, imageAbsolutePath, userMobileNumber;
    private CircleImageView circleImageView;
    private File profilePhotoPath;
    private EditText editFullName;
    private EditText editVisitorMobileNumber;
    private EditText editMobileNumber;
    private TextView textErrorProfilePic;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_eintercom;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.e_intercom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        circleImageView = findViewById(R.id.visitorProfilePic);
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textVisitorMobileNumber = findViewById(R.id.textVisitorMobileNumber);
        TextView textVisitorCountryCode = findViewById(R.id.textVisitorCountryCode);
        editVisitorMobileNumber = findViewById(R.id.editVisitorMobileNumber);
        LinearLayout layoutVisitorMobileNumber = findViewById(R.id.layoutVisitorMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonSendNotification = findViewById(R.id.buttonSendNotification);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);

        /*Change UI design if E-Intercom type is Cab or Package*/
        eIntercomType = getIntent().getStringExtra(EINTERCOM_TYPE);
        if (eIntercomType.equals(CAB)) {
            LinearLayout cabNumberLayout = findViewById(R.id.cabNumberLayout);
            TextView textCabOrVendorTitle = findViewById(R.id.textCabOrVendorTitle);
            editCabStateCode = findViewById(R.id.editCabStateCode);
            editCabRtoNumber = findViewById(R.id.editCabRtoNumber);
            editCabSerialNumberOne = findViewById(R.id.editCabSerialNumberOne);
            editCabSerialNumberTwo = findViewById(R.id.editCabSerialNumberTwo);

            /*Setting font for all the views*/
            textCabOrVendorTitle.setTypeface(setLatoBoldFont(this));
            editCabStateCode.setTypeface(setLatoRegularFont(this));
            editCabRtoNumber.setTypeface(setLatoRegularFont(this));
            editCabSerialNumberOne.setTypeface(setLatoRegularFont(this));
            editCabSerialNumberTwo.setTypeface(setLatoRegularFont(this));

            /*Hide FullName and Image View*/
            textFullName.setVisibility(View.GONE);
            editFullName.setVisibility(View.GONE);
            circleImageView.setVisibility(View.GONE);
            layoutVisitorMobileNumber.setVisibility(View.GONE);
            textVisitorMobileNumber.setVisibility(View.GONE);

            /*Show Cab Number Layout*/
            textCabOrVendorTitle.setVisibility(View.VISIBLE);
            cabNumberLayout.setVisibility(View.VISIBLE);

            /*Setting events for Cab Number edit text*/
            setEventsForEditText();
        } else {
            if (eIntercomType.equals(PACKAGE)) {
                textFullName.setText(getString(R.string.vendor));
                circleImageView.setVisibility(View.GONE);
                layoutVisitorMobileNumber.setVisibility(View.GONE);
                textVisitorMobileNumber.setVisibility(View.GONE);
            }
            /*Setting font for all the views*/
            textFullName.setTypeface(setLatoBoldFont(this));
            textErrorProfilePic.setTypeface(setLatoRegularFont(this));
            editFullName.setTypeface(setLatoRegularFont(this));
            editMobileNumber.setTypeface(setLatoRegularFont(this));
        }
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        buttonSendNotification.setTypeface(setLatoLightFont(this));
        textVisitorMobileNumber.setTypeface(setLatoBoldFont(this));
        textVisitorCountryCode.setTypeface(setLatoItalicFont(this));
        editVisitorMobileNumber.setTypeface(setLatoRegularFont(this));

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(this);
        buttonSendNotification.setOnClickListener(this);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onClick and OnFocusChange Listener Methods
     *-----------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.visitorProfilePic:
                launchCamera();
                break;
            case R.id.buttonSendNotification:
                if (validateFields()) {
                    sendNotification();
                }
                break;
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TAKE_PICTURE && resultCode == RESULT_OK) {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                    imageAbsolutePath = imageFile.getAbsolutePath();
                    Bitmap dailyServiceProfilePic = getBitmapFromFile(EIntercom.this, imageFile);
                    circleImageView.setImageBitmap(dailyServiceProfilePic);
                    profilePhotoPath = imageFile;
                    if (profilePhotoPath != null) {
                        textErrorProfilePic.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                EasyImage.openCamera(this, 0);
            }
        }
    }

    /**
     * Method is invoked when Guard sends the notification to the user
     */
    private void sendNotification() {
        //displaying progress dialog while image is uploading
        showProgressDialog(this,
                getResources().getString(R.string.notifying_user),
                getResources().getString(R.string.please_wait_a_moment));

        String notificationMessage = null;
        switch (eIntercomType) {
            case GUEST:
                reference = editFullName.getText().toString();
                notificationMessage = getGuestMessage(reference);
                break;
            case CAB:
                String cabNumber = editCabStateCode.getText().toString().trim() + HYPHEN + editCabRtoNumber.getText().toString().trim() + HYPHEN
                        + editCabSerialNumberOne.getText().toString().trim() + HYPHEN + editCabSerialNumberTwo.getText().toString().trim();
                reference = cabNumber;
                notificationMessage = getCabMessage(cabNumber);
                break;
            case PACKAGE:
                reference = editFullName.getText().toString();
                notificationMessage = getPackageMessage(editFullName.getText().toString());
                break;
        }
        storeGateNotificationDetails(notificationMessage);
    }

    /*-------------------------------------------------------------------------------
     * Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method gets invoked to check all the validation fields of editTexts
     */
    private boolean validateFields() {
        String fullName = editFullName.getText().toString().trim();
        String mobileNumber = editMobileNumber.getText().toString().trim();
        userMobileNumber = mobileNumber;
        visitorMobileNumber = editVisitorMobileNumber.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editFullName, editMobileNumber, editVisitorMobileNumber});

        if (profilePhotoPath == null && !(eIntercomType.equals(CAB) || eIntercomType.equals(PACKAGE))) {
            textErrorProfilePic.setVisibility(View.VISIBLE);
            textErrorProfilePic.requestFocus();
            return false;
        } else {
            textErrorProfilePic.setVisibility(View.INVISIBLE);
        }

        if (!fieldsFilled) {
            if (TextUtils.isEmpty(fullName) && !(eIntercomType.equals(CAB))) {
                editFullName.setError(getString(R.string.name_validation));
                return false;
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.mobile_number_validation));
                return false;
            }
            if (TextUtils.isEmpty(visitorMobileNumber) && !(eIntercomType.equals(CAB) || eIntercomType.equals(PACKAGE))) {
                editVisitorMobileNumber.setError(getString(R.string.mobile_number_validation));
                return false;
            }
        } else {
            if (isPersonNameValid(fullName)) {
                editFullName.setError(getString(R.string.accept_alphabets));
                return false;
            }
            if (!isPhoneNumberValid(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.number_10digit_validation));
                return false;
            }
        }
        return true;
    }

    /**
     * Stores the details of the gate notifications request to Firebase so user can accept or reject the request
     */
    private void storeGateNotificationDetails(String notificationMessage) {
        /*We first get the user UID from the mobile number*/
        String mobileNumber = editMobileNumber.getText().toString().trim();
        DatabaseReference userMobileReference = ALL_USERS_REFERENCE.child(mobileNumber);
        userMobileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userUID = dataSnapshot.getValue(String.class);

                    /*We get user information by their UID*/
                    DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(Objects.requireNonNull(userUID));
                    userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);

                            /*We store Notification under FlatDetails->Notification->userUID*/
                            UserFlatDetails userFlatDetails = Objects.requireNonNull(nammaApartmentUser).getFlatDetails();
                            DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                                    .child(userFlatDetails.getCity())
                                    .child(userFlatDetails.getSocietyName())
                                    .child(userFlatDetails.getApartmentName())
                                    .child(userFlatDetails.getFlatNumber());

                            /*We create a unique ID for every push notifications*/
                            DatabaseReference notificationsReference = userDataReference
                                    .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                                    .child(userUID)
                                    .child(EINTERCOM_TYPE_MAP.get(eIntercomType))
                                    .push();
                            String notificationUID = notificationsReference.getKey();

                            /*If E-Intercom Type is Cab or Package, we would not have any Image to be stored in firebase*/
                            if (eIntercomType.equals(CAB) || eIntercomType.equals(PACKAGE)) {
                                notificationsReference.child(FIREBASE_CHILD_PROFILE_PHOTO).setValue("");
                                notificationsReference.child(FIREBASE_CHILD_UID).setValue(notificationUID);
                                notificationsReference.child(FIREBASE_CHILD_MESSAGE).setValue(notificationMessage);
                                hideProgressDialog();
                                callAwaitingResponseActivity(userUID, notificationUID);
                            } else {
                                /* Else we need to store Image of Guest, Daily Service or Family Member*/

                                /*Getting the storage reference*/
                                StorageReference storageReference = FIREBASE_STORAGE.getReference(FIREBASE_CHILD_VISITORS)
                                        .child(FIREBASE_CHILD_PRIVATE)
                                        .child(FIREBASE_CHILD_VISITORS)
                                        .child(EINTERCOM_TYPE_MAP.get(eIntercomType))
                                        .child(notificationsReference.getKey());

                                UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(EIntercom.this, profilePhotoPath));

                                /*Adding the profile photo to storage reference and notification data to real time database under Flat Detail*/
                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    //creating the upload object to store uploaded image details and notification data
                                    String profilePhoto = taskSnapshot.getDownloadUrl().toString();
                                    notificationsReference.setValue(new Notification(notificationMessage, visitorMobileNumber, profilePhoto, notificationUID))
                                            .addOnCompleteListener(task -> {
                                                //dismissing the progress dialog
                                                hideProgressDialog();
                                                callAwaitingResponseActivity(userUID, notificationUID);
                                            });

                                }).addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                } else {
                    /*dismissing the progress dialog*/
                    hideProgressDialog();
                    showNotificationDialog(getString(R.string.invalid_user_title), getString(R.string.invalid_user_message), null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Calls Awaiting Response Activity by passing necessary data
     *
     * @param userUID         UID of the user to whom the notification is sent
     * @param notificationUID UID of the notification details
     */
    private void callAwaitingResponseActivity(String userUID, String notificationUID) {
        /*Call AwaitingResponse activity, by this time user should have received the Notification
         * Since, cloud functions would have been triggered*/
        Intent awaitingResponseIntent = new Intent(EIntercom.this, AwaitingResponse.class);
        awaitingResponseIntent.putExtra("EIntercomType", eIntercomType);
        awaitingResponseIntent.putExtra("SentUserUID", userUID);
        awaitingResponseIntent.putExtra("NotificationUID", notificationUID);
        awaitingResponseIntent.putExtra("Reference", reference);
        awaitingResponseIntent.putExtra("VisitorImageFilePath", imageAbsolutePath);
        awaitingResponseIntent.putExtra("UserMobileNumber", userMobileNumber);
        startActivity(awaitingResponseIntent);
    }

    /**
     * This method gets invoked when the Guard presses the profilePic image to capture a photo
     */
    private void launchCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            EasyImage.openCamera(this, 0);
        }
    }

    /**
     * Once user enters details in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editCabStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabRtoNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editCabRtoNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabStateCode.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editCabSerialNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabRtoNumber.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    editCabSerialNumberTwo.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editCabSerialNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_MIN_LENGTH) {
                    editCabSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

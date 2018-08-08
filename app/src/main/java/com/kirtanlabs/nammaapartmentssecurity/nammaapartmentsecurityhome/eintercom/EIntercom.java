package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.DAILY_SERVICE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FAMILY_MEMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_POSTAPPROVEDVISITORS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getCabMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getDailyServiceMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getFamilyMemberMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getGuestMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.getPackageMessage;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.bitmapToByteArray;

public class EIntercom extends BaseActivity {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    public static String imageFilePath = "";
    EditText editCabStateCode, editCabRtoNumber, editCabSerialNumberOne, editCabSerialNumberTwo;
    private String eIntercomType;

    private CircleImageView circleImageView;
    private EditText editFullName;
    private EditText editMobileNumber;
    private byte[] profilePhotoByteArray;
    private TextView textErrorProfilePic;

    private Intent cameraIntent;

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
        circleImageView = findViewById(R.id.familyMemberProfilePic);
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
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

            /*Show Cab Number Layout*/
            textCabOrVendorTitle.setVisibility(View.VISIBLE);
            cabNumberLayout.setVisibility(View.VISIBLE);
        } else {
            if (eIntercomType.equals(PACKAGE)) {
                textFullName.setText("Vendor");
                circleImageView.setVisibility(View.GONE);
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

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(v -> launchCamera());
        buttonSendNotification.setOnClickListener(v -> {
            sendNotification();

            /*TODO: Uncomment these since for sending notifications currently we are not using profile photo*/
            /*if (profilePhotoByteArray == null) {
                textErrorProfilePic.setVisibility(View.VISIBLE);
                textErrorProfilePic.requestFocus();
            } else {
                textErrorProfilePic.setVisibility(View.INVISIBLE);
            }*/
            // This method gets invoked to check all the validation fields such as editTexts
            /*validateFields();*/
        });

    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmapProfilePic = ImagePicker.getImageFromResult(EIntercom.this, resultCode, data);
            circleImageView.setImageBitmap(bitmapProfilePic);
            profilePhotoByteArray = bitmapToByteArray(bitmapProfilePic);
            if (profilePhotoByteArray != null) {
                textErrorProfilePic.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
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
                notificationMessage = getGuestMessage(editFullName.getText().toString());
                break;
            case DAILY_SERVICE:
                notificationMessage = getDailyServiceMessage(editFullName.getText().toString());
                break;
            case CAB:
                String cabNumber = editCabStateCode.getText().toString().trim() + HYPHEN + editCabRtoNumber.getText().toString().trim() + HYPHEN
                        + editCabSerialNumberOne.getText().toString().trim() + HYPHEN + editCabSerialNumberTwo.getText().toString().trim();
                notificationMessage = getCabMessage(cabNumber);
                break;
            case PACKAGE:
                notificationMessage = getPackageMessage(editFullName.getText().toString());
                break;
            case FAMILY_MEMBER:
                notificationMessage = getFamilyMemberMessage(editFullName.getText().toString());
                break;
        }
        storeGateNotificationDetails(notificationMessage);
    }

    /**
     * This method gets invoked to check all the validation fields of editTexts
     */
    private void validateFields() {
        String fullName = editFullName.getText().toString().trim();
        String mobileNumber = editMobileNumber.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editFullName, editMobileNumber});
        //This condition checks if all fields are not filled and if user presses add button it will then display proper error messages.
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(fullName)) {
                editFullName.setError(getString(R.string.name_validation));
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.mobile_number_validation));
            }
        }

        //This condition checks for if user has filled all the fields and also validates name and mobile number
        //and displays proper error messages.
        if (fieldsFilled) {
            if (!isValidPersonName(fullName)) {
                editFullName.setError(getString(R.string.accept_alphabets));
            }
            if (!isValidPhone(mobileNumber)) {
                editMobileNumber.setError(getString(R.string.number_10digit_validation));
            }
        }

        //This condition checks if name,mobile number are properly validated and then sends notification
        if (!isValidPersonName(fullName) && isValidPhone(mobileNumber)) {
        }
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
                String userUID = dataSnapshot.getValue(String.class);

                /*We get user information by their UID*/
                DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(userUID);
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
                            notificationsReference.child("uid").setValue(notificationUID);
                            notificationsReference.child("message").setValue(notificationMessage);
                            hideProgressDialog();
                            callAwaitingResponseActivity(userUID, notificationUID);
                        } else {
                            /* Else we need to store Image of Guest, Daily Service or Family Member*/

                            /*Getting the storage reference*/
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_VISITORS)
                                    .child(FIREBASE_CHILD_PRIVATE)
                                    .child(FIREBASE_CHILD_POSTAPPROVEDVISITORS)
                                    .child(EINTERCOM_TYPE_MAP.get(eIntercomType))
                                    .child(notificationsReference.getKey());

                            UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(profilePhotoByteArray));

                            /*Adding the profile photo to storage reference and notification data to real time database under Flat Detail*/
                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                //creating the upload object to store uploaded image details and notification data
                                notificationsReference.child(FIREBASE_CHILD_PROFILE_PHOTO).setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString())
                                        .addOnCompleteListener(task -> {
                                            /*Store UID and Message of Notification in Firebase*/
                                            notificationsReference.child("uid").setValue(notificationUID);
                                            notificationsReference.child("message").setValue(notificationMessage);

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
        startActivity(awaitingResponseIntent);
    }

    /**
     * Creates Image File for Captured Image
     *
     * @return captured Image file path
     * @throws IOException if file is not created
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    /**
     * This method gets invoked when the Guard presses the profilePic image to capture a photo
     */
    protected void launchCamera() {
        //TODO: Launch Camera functionality is not working for phone's with API level >=27
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            photoFile = createImageFile();
            imageFilePath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}

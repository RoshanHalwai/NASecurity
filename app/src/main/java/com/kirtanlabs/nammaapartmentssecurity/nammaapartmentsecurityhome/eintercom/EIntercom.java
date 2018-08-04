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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.kirtanlabs.nammaapartmentssecurity.Constants;
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
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.bitmapToByteArray;

public class EIntercom extends BaseActivity implements AdapterView.OnItemSelectedListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    public static String imageFilePath = "";
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
        TextView textEIntercomType = findViewById(R.id.textEIntercomType);
        Spinner spinnerEIntercomType = findViewById(R.id.spinnerEIntercomType);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonSendNotification = findViewById(R.id.buttonSendNotification);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);

        /*Setting font for all the views*/
        textFullName.setTypeface(setLatoBoldFont(this));
        textEIntercomType.setTypeface(setLatoBoldFont(this));
        textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        editFullName.setTypeface(setLatoRegularFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
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

        /*Setting font for all the items in the list*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.e_intercom_type_list)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textEIntercomType = view.findViewById(android.R.id.text1);
                textEIntercomType.setTypeface(Constants.setLatoRegularFont(EIntercom.this));
                return view;
            }
        };
        //Setting adapter to Spinner view
        spinnerEIntercomType.setAdapter(adapter);

    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemSelectedListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: To Write business logic here when user select any item from the list.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        /*We first get the user UID from the mobile number*/
        String mobileNumber = editMobileNumber.getText().toString().trim();
        DatabaseReference userMobileReference = ALL_USERS_REFERENCE.child(mobileNumber);
        userMobileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userUID = dataSnapshot.getValue().toString();

                //We get user information by their UID
                DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(userUID);
                userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);

                        /*We store Notification under FlatDetails->Notification->userUID*/
                        UserFlatDetails userFlatDetails = nammaApartmentUser.getFlatDetails();
                        DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                                .child(userFlatDetails.getCity())
                                .child(userFlatDetails.getSocietyName())
                                .child(userFlatDetails.getApartmentName())
                                .child(userFlatDetails.getFlatNumber());

                        String notificationMessage = editFullName.getText() + " wants to enter your Society";

                        /*We create a unique ID for every push notifications*/
                        DatabaseReference notificationsReference = userDataReference
                                .child("gateNotifications")
                                .child(userUID)
                                .push();

                        //getting the storage reference
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_VISITORS)
                                .child(Constants.FIREBASE_CHILD_PRIVATE)
                                .child(Constants.FIREBASE_CHILD_POSTAPPROVEDVISITORS)
                                .child(userUID); //TODO: PostApproved Visitor UID might be placed here instead of User UID

                        UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(profilePhotoByteArray));

                        //adding the profile photo to storage reference and notification data to real time database under Flat Details
                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            //creating the upload object to store uploaded image details and notification data
                            notificationsReference.child("uid").setValue(notificationsReference.getKey());
                            notificationsReference.child("message").setValue(notificationMessage);
                            notificationsReference.child("profilePhoto").setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString())
                                    .addOnCompleteListener(task -> {

                                        //dismissing the progress dialog
                                        hideProgressDialog();

                                        /*Call AwaitingResponse activity, by this time user should have received the Notification
                                         * Since, cloud functions would have been triggered*/
                                        Intent awaitingResponseIntent = new Intent(EIntercom.this, AwaitingResponse.class);
                                        awaitingResponseIntent.putExtra("SentUserUID", userUID);
                                        awaitingResponseIntent.putExtra("NotificationUID", notificationsReference.getKey());
                                        startActivity(awaitingResponseIntent);
                                    });

                        }).addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show());
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

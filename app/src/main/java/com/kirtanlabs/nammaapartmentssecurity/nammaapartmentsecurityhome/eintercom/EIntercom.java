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
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.bitmapToByteArray;

public class EIntercom extends BaseActivity {

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
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonSendNotification = findViewById(R.id.buttonSendNotification);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);

        /*Setting font for all the views*/
        textFullName.setTypeface(setLatoBoldFont(this));
        textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        editFullName.setTypeface(setLatoRegularFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
        buttonSendNotification.setTypeface(setLatoLightFont(this));

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(v -> launchCamera());
        buttonSendNotification.setOnClickListener(v -> {
            if (profilePhotoByteArray == null) {
                textErrorProfilePic.setVisibility(View.VISIBLE);
                textErrorProfilePic.requestFocus();
            } else {
                textErrorProfilePic.setVisibility(View.INVISIBLE);
            }
            // This method gets invoked to check all the validation fields such as editTexts
            validateFields();
        });

    }

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

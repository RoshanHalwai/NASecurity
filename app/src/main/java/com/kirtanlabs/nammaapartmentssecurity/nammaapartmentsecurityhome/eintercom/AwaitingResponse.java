package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification.NammaApartmentExpectedArrivals;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation.NammaApartmentVisitor;

import java.io.File;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.ACCEPTED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.ALL_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_DELIVERIES;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.IGNORED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PLACE_CALL_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_DELIVERIES_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.REJECTED;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.SENT_USER_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.USER_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_IMAGE_FILE_PATH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_IMAGE_URL;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.ImagePicker.getBitmapFromFile;

public class AwaitingResponse extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private LinearLayout layoutAwaitingResponse, layoutApproveOrUnApprove, layoutUserResponse;
    private android.support.v7.widget.CardView layoutCallUser;
    private TextView textUserResponse;
    private ImageView imageResponseStatus;
    private Intent callIntent;
    private String notificationUID, userUID, visitorType, reference;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_awaiting_response;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.e_intercom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getStringExtra(NOTIFICATION_UID) != null) {
            notificationUID = getIntent().getStringExtra(NOTIFICATION_UID);
            userUID = getIntent().getStringExtra(SENT_USER_UID);
            visitorType = getIntent().getStringExtra(EINTERCOM_TYPE);
            reference = getIntent().getStringExtra(REFERENCE);
        }

        /*Get Id for UI Views*/
        layoutAwaitingResponse = findViewById(R.id.layoutAwaitingResponse);
        layoutUserResponse = findViewById(R.id.layoutUserResponse);
        layoutCallUser = findViewById(R.id.layoutCallUser);
        layoutApproveOrUnApprove = findViewById(R.id.layoutApproveOrUnApprove);
        de.hdodenhof.circleimageview.CircleImageView imageVisitorPic = findViewById(R.id.imageVisitorPic);
        imageResponseStatus = findViewById(R.id.imageResponseStatus);
        TextView textReference = findViewById(R.id.textReference);
        TextView textNotificationSent = findViewById(R.id.textNotificationSent);
        TextView textCallUser = findViewById(R.id.textCallUser);
        TextView textApprove = findViewById(R.id.textApprove);
        TextView textUnApprove = findViewById(R.id.textUnApprove);
        textUserResponse = findViewById(R.id.textUserResponse);

        /*Set Fonts for UI Views*/
        textReference.setTypeface(setLatoRegularFont(this));
        textNotificationSent.setTypeface(setLatoRegularFont(this));
        textCallUser.setTypeface(setLatoRegularFont(this));
        textApprove.setTypeface(setLatoRegularFont(this));
        textUnApprove.setTypeface(setLatoRegularFont(this));
        textUserResponse.setTypeface(setLatoBoldFont(this));

        textReference.setText(reference);
        switch (visitorType) {
            case GUEST:
                File guestImage = new File(getIntent().getStringExtra(VISITOR_IMAGE_FILE_PATH));
                Bitmap dailyServiceProfilePic = getBitmapFromFile(AwaitingResponse.this, guestImage);
                imageVisitorPic.setImageBitmap(dailyServiceProfilePic);
                break;
            case CAB:
                imageVisitorPic.setImageDrawable(getResources().getDrawable(R.drawable.taxi));
                break;
            default:
                imageVisitorPic.setImageDrawable(getResources().getDrawable(R.drawable.delivery_man));
        }

        /*Setting on click listeners to the Views*/
        textApprove.setOnClickListener(this);
        textUnApprove.setOnClickListener(this);
        textCallUser.setOnClickListener(this);

        /*Keep track of user response*/
        getUserResponse(visitorType);

        /*Action to be performed when no response received - Timeout 45 Seconds*/
        Handler h = new Handler(Looper.getMainLooper());
        long delayInMilliseconds = 45000;
        h.postDelayed(() -> setNotificationToIgnored(userUID, visitorType, notificationUID), delayInMilliseconds);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onClick Method
     *-----------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCallUser:
                callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getIntent().getStringExtra(USER_MOBILE_NUMBER)));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PLACE_CALL_PERMISSION_REQUEST_CODE);
                } else {
                    startActivity(callIntent);
                    layoutApproveOrUnApprove.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.textApprove:
                storeVisitorDetailsInFirebase();
                break;
            case R.id.textUnApprove:
                Intent intent = new Intent(AwaitingResponse.this, NammaApartmentSecurityHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onRequestPermissionResult Method
     *-----------------------------------------------------------------------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PLACE_CALL_PERMISSION_REQUEST_CODE && grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            layoutApproveOrUnApprove.setVisibility(View.VISIBLE);
            startActivity(callIntent);
        }
    }

    /*-------------------------------------------------------------------------------
     * Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked to store "Post Approved" Visitors data in firebase.
     */
    private void storeVisitorDetailsInFirebase() {
        /*Getting Current Date and Time*/
        String currentDateAndTime = getCurrentDate() + "\t\t" + " " + getCurrentTime();

        DatabaseReference usersReference = PRIVATE_USERS_REFERENCE.child(userUID);
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference currentUserDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber());

                /*Here we are creating reference for storing guardApproved Visitors under userdata->userFlatNumber*/
                DatabaseReference currentUserVisitorReference = null;
                switch (visitorType) {
                    case GUEST:
                        String visitorMobileNumber = getIntent().getStringExtra(VISITOR_MOBILE_NUMBER);
                        String visitorImageUrl = getIntent().getStringExtra(VISITOR_IMAGE_URL);

                        /*Mapping Mobile number with visitor's UID in firebase under (visitors->all)*/
                        DatabaseReference visitorsMobileNumberReference = ALL_VISITORS_REFERENCE.child(visitorMobileNumber);
                        visitorsMobileNumberReference.setValue(notificationUID);

                        NammaApartmentVisitor nammaApartmentVisitor = new NammaApartmentVisitor(currentDateAndTime,
                                reference, userUID, visitorMobileNumber, visitorImageUrl, getString(R.string.entered), notificationUID);
                        nammaApartmentVisitor.setApprovalType(getString(R.string.guard_approved));

                        /*Storing visitor's details in firebase under (visitors->private->visitorUID)*/
                        DatabaseReference visitorPrivateReference = PRIVATE_VISITORS_REFERENCE.child(notificationUID);
                        visitorPrivateReference.setValue(nammaApartmentVisitor);

                        /* Creating database reference of firebase till (userData->private->userFlatNumber->visitors) to store visitor uid*/
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_VISITORS);
                        break;
                    case CAB:
                        /*Mapping Cab number with cab UID in firebase under (cabs->all)*/
                        DatabaseReference cabsAllReference = ALL_CABS_REFERENCE.child(reference);
                        cabsAllReference.setValue(notificationUID);

                        NammaApartmentExpectedArrivals nammaApartmentCabArrivals = new NammaApartmentExpectedArrivals(currentDateAndTime,
                                reference, getString(R.string.entered), userUID, getString(R.string.valid_for_default_value));
                        nammaApartmentCabArrivals.setApprovalType(getString(R.string.guard_approved));

                        /*Storing cab details in firebase under (cab->private->cabUID)*/
                        DatabaseReference cabPrivateReference = PRIVATE_CABS_REFERENCE.child(notificationUID);
                        cabPrivateReference.setValue(nammaApartmentCabArrivals);

                        /*Creating database reference of firebase till (userData->private->userFlatNumber->cabs) to store cab number uid*/
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_CABS);
                        break;
                    case PACKAGE:
                        NammaApartmentExpectedArrivals nammaApartmentPackageArrivals = new NammaApartmentExpectedArrivals(currentDateAndTime,
                                reference, getString(R.string.entered), userUID, getString(R.string.valid_for_default_value));
                        nammaApartmentPackageArrivals.setApprovalType(getString(R.string.guard_approved));

                        DatabaseReference deliveriesPrivateReference = PRIVATE_DELIVERIES_REFERENCE.child(notificationUID);
                        deliveriesPrivateReference.setValue(nammaApartmentPackageArrivals);

                        /*Creating database reference of firebase till (userData->private->userFlatNumber->deliveries) to store package vendor uid*/
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_DELIVERIES);
                        break;
                }
                /*Mapping Visitor's UID with true in firebase under (userData->private->userFlatNumber->"visitorType")*/
                Objects.requireNonNull(currentUserVisitorReference).child(userUID).child(notificationUID).setValue(true);

                showNotificationSentDialog(getString(R.string.approval_title), getString(R.string.approval_message));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Sets the status of the notification to IGNORED since the notification timed out
     *
     * @param currentUserID   - userUID of the the LoggedIn user
     * @param visitorType     - can be either one of these Guest/Cab/Package
     * @param notificationUID - unique UID to identify each gate notifications
     */
    private void setNotificationToIgnored(final String currentUserID, final String visitorType, final String notificationUID) {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(currentUserID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference currentUserDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber());

                /*Here we are setting the notification status so that Guard know that this notification has been ignored by the user*/
                DatabaseReference currentUserNotificationReference = currentUserDataReference
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(currentUserID)
                        .child(EINTERCOM_TYPE_MAP.get(visitorType))
                        .child(notificationUID);

                currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).setValue(IGNORED);
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
     * This method is invoked to get the user's response for e-intercom notification that is
     * sent for visitor approval
     *
     * @param visitorType - type of visitor
     */
    private void getUserResponse(String visitorType) {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(userUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference userDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber())
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(userUID)
                        .child(EINTERCOM_TYPE_MAP.get(visitorType));
                userDataReference.child(notificationUID).child(FIREBASE_CHILD_STATUS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            hideProgressIndicator();
                            switch (Objects.requireNonNull(dataSnapshot.getValue(String.class))) {
                                case ACCEPTED:
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.request_accepted_na));
                                    textUserResponse.setText(getString(R.string.accepted_request));
                                    break;
                                case REJECTED:
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));
                                    textUserResponse.setText(getString(R.string.rejected_request));
                                    break;
                                default:
                                    //TODO: Add new Image for Ignored case
                                    imageResponseStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));
                                    textUserResponse.setText(getString(R.string.ignored_request));
                                    layoutCallUser.setVisibility(View.VISIBLE);
                            }
                            layoutAwaitingResponse.setVisibility(View.GONE);
                            layoutUserResponse.setVisibility(View.VISIBLE);
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

}

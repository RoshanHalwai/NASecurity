package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_USER_APARTMENT_NAME;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_USER_FLAT_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_USER_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB_APPROVAL_USER_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE_MAP;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUARD_CITY_NAME;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUARD_SOCIETY_NAME;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_USER_APARTMENT_NAME;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_USER_FLAT_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_USER_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_USER_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST_APPROVAL_VISITOR_IMAGE_PATH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.NAMMA_APARTMENTS_SECURITY_PREFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_USER_APARTMENT_NAME;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_USER_FLAT_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_USER_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE_APPROVAL_USER_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.REFERENCE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.SENT_USER_UID;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.USER_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_IMAGE_FILE_PATH;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_IMAGE_URL;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.VISITOR_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/5/2018
 */
public class EIntercomTypeAdapter extends RecyclerView.Adapter<EIntercomTypeAdapter.VisitorsListHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<Type> visitorsList;
    private SharedPreferences sharedPreferences;
    private String userUID, userApartmentName, userFlatNumber, reference,
            userMobileNumber, imageAbsolutePath;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    EIntercomTypeAdapter(Context mCtx, List<Type> visitorsList) {
        this.mCtx = mCtx;
        this.visitorsList = visitorsList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public VisitorsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_eintercom_type, parent, false);
        return new VisitorsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorsListHolder holder, int position) {
        Type nammaApartmentService = visitorsList.get(position);
        holder.textNotification.setTypeface(setLatoRegularFont(mCtx));
        holder.textNotification.setText(nammaApartmentService.getIntercomType());
        holder.imageNotificationService.setImageResource(nammaApartmentService.getIntercomTypeImage());
    }

    @Override
    public int getItemCount() {
        return visitorsList.size();
    }

    /* ------------------------------------------------------------- *
     *  View Holder class
     * ------------------------------------------------------------- */

    public class VisitorsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final TextView textNotification;
        final ImageView imageNotificationService;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorsListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textNotification = itemView.findViewById(R.id.textNotification);
            imageNotificationService = itemView.findViewById(R.id.imageNotificationService);
        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            checkPreviousEIntercomRequest(position);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether any eIntercom request is raised for particular eIntercom Type
     *
     * @param position of that particular cardview.
     */
    private void checkPreviousEIntercomRequest(final int position) {
        String notificationUID = null;
        String eIntercomType = null;

        /*Retrieving user's previous eIntercom notification UID from shared preference*/
        sharedPreferences = Objects.requireNonNull(mCtx.getSharedPreferences(NAMMA_APARTMENTS_SECURITY_PREFERENCE, MODE_PRIVATE));
        switch (position) {
            case 0:
                eIntercomType = GUEST;
                notificationUID = sharedPreferences.getString(GUEST_APPROVAL_NOTIFICATION_UID, null);
                break;
            case 1:
                eIntercomType = CAB;
                notificationUID = sharedPreferences.getString(CAB_APPROVAL_NOTIFICATION_UID, null);
                break;
            case 2:
                eIntercomType = PACKAGE;
                notificationUID = sharedPreferences.getString(PACKAGE_APPROVAL_NOTIFICATION_UID, null);
                break;
        }

        if (notificationUID != null) {
            checkPreviousRequestStatus(notificationUID, eIntercomType);
        } else {
            Intent intent = new Intent(mCtx, EIntercom.class);
            intent.putExtra(EINTERCOM_TYPE, eIntercomType);
            mCtx.startActivity(intent);
        }
    }

    /**
     * This method is invoked to check the status of previous E-Intercom request for particular E-Intercom type
     *
     * @param notificationUID - notification UID of previous request.
     * @param eIntercomType   - E-Intercom type.
     */
    private void checkPreviousRequestStatus(final String notificationUID, final String eIntercomType) {

        switch (eIntercomType) {
            case GUEST:
                userUID = sharedPreferences.getString(GUEST_APPROVAL_USER_UID, null);
                userApartmentName = sharedPreferences.getString(GUEST_APPROVAL_USER_APARTMENT_NAME, null);
                userFlatNumber = sharedPreferences.getString(GUEST_APPROVAL_USER_FLAT_NUMBER, null);
                break;
            case CAB:
                userUID = sharedPreferences.getString(CAB_APPROVAL_USER_UID, null);
                userApartmentName = sharedPreferences.getString(CAB_APPROVAL_USER_APARTMENT_NAME, null);
                userFlatNumber = sharedPreferences.getString(CAB_APPROVAL_USER_FLAT_NUMBER, null);
                break;
            case PACKAGE:
                userUID = sharedPreferences.getString(PACKAGE_APPROVAL_USER_UID, null);
                userApartmentName = sharedPreferences.getString(PACKAGE_APPROVAL_USER_APARTMENT_NAME, null);
                userFlatNumber = sharedPreferences.getString(PACKAGE_APPROVAL_USER_FLAT_NUMBER, null);
                break;
        }

        DatabaseReference eIntercomNotificationReference = PRIVATE_USER_DATA_REFERENCE
                .child(GUARD_CITY_NAME)
                .child(GUARD_SOCIETY_NAME)
                .child(Objects.requireNonNull(userApartmentName))
                .child(Objects.requireNonNull(userFlatNumber))
                .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                .child(Objects.requireNonNull(userUID))
                .child(EINTERCOM_TYPE_MAP.get(eIntercomType))
                .child(notificationUID);

        /*Checking status of previous e-intercom notification from (userData->
        userFlatNumber->gateNotification->userUID->eIntercomType->notificationUID->status) in firebase*/
        eIntercomNotificationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(FIREBASE_CHILD_STATUS).exists()) {
                    /*User has responded to the previous eIntercom request so we navigate guard to E-intercom screen*/
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    /*Clearing data from shared preference for particular eIntercom type*/
                    switch (eIntercomType) {
                        case GUEST:
                            editor.putString(GUEST_APPROVAL_USER_UID, null);
                            editor.putString(GUEST_APPROVAL_NOTIFICATION_UID, null);
                            editor.putString(GUEST_APPROVAL_REFERENCE, null);
                            editor.putString(GUEST_APPROVAL_USER_MOBILE_NUMBER, null);
                            editor.putString(GUEST_APPROVAL_USER_APARTMENT_NAME, null);
                            editor.putString(GUEST_APPROVAL_USER_FLAT_NUMBER, null);
                            editor.putString(GUEST_APPROVAL_VISITOR_IMAGE_PATH, null);
                            break;
                        case CAB:
                            editor.putString(CAB_APPROVAL_USER_UID, null);
                            editor.putString(CAB_APPROVAL_NOTIFICATION_UID, null);
                            editor.putString(CAB_APPROVAL_REFERENCE, null);
                            editor.putString(CAB_APPROVAL_USER_MOBILE_NUMBER, null);
                            editor.putString(CAB_APPROVAL_USER_APARTMENT_NAME, null);
                            editor.putString(CAB_APPROVAL_USER_FLAT_NUMBER, null);
                            break;
                        case PACKAGE:
                            editor.putString(PACKAGE_APPROVAL_USER_UID, null);
                            editor.putString(PACKAGE_APPROVAL_NOTIFICATION_UID, null);
                            editor.putString(PACKAGE_APPROVAL_REFERENCE, null);
                            editor.putString(PACKAGE_APPROVAL_USER_MOBILE_NUMBER, null);
                            editor.putString(PACKAGE_APPROVAL_USER_APARTMENT_NAME, null);
                            editor.putString(PACKAGE_APPROVAL_USER_FLAT_NUMBER, null);
                            break;
                    }
                    editor.apply();

                    Intent intent = new Intent(mCtx, EIntercom.class);
                    intent.putExtra(EINTERCOM_TYPE, eIntercomType);
                    mCtx.startActivity(intent);

                } else {
                    /*User has still not responded to the previous E-Intercom request so we navigate guard to Awaiting Response screen*/
                    switch (eIntercomType) {
                        case GUEST:
                            reference = sharedPreferences.getString(GUEST_APPROVAL_REFERENCE, null);
                            userMobileNumber = sharedPreferences.getString(GUEST_APPROVAL_USER_MOBILE_NUMBER, null);
                            imageAbsolutePath = sharedPreferences.getString(GUEST_APPROVAL_VISITOR_IMAGE_PATH, null);
                            break;
                        case CAB:
                            reference = sharedPreferences.getString(CAB_APPROVAL_REFERENCE, null);
                            userMobileNumber = sharedPreferences.getString(CAB_APPROVAL_USER_MOBILE_NUMBER, null);
                            break;
                        case PACKAGE:
                            reference = sharedPreferences.getString(PACKAGE_APPROVAL_REFERENCE, null);
                            userMobileNumber = sharedPreferences.getString(PACKAGE_APPROVAL_USER_MOBILE_NUMBER, null);
                            break;
                    }

                    Intent awaitingResponseIntent = new Intent(mCtx, AwaitingResponse.class);
                    awaitingResponseIntent.putExtra(EINTERCOM_TYPE, eIntercomType);
                    awaitingResponseIntent.putExtra(SENT_USER_UID, userUID);
                    awaitingResponseIntent.putExtra(NOTIFICATION_UID, notificationUID);
                    awaitingResponseIntent.putExtra(REFERENCE, reference);
                    awaitingResponseIntent.putExtra(USER_MOBILE_NUMBER, userMobileNumber);
                    if (eIntercomType.equals(GUEST)) {
                        awaitingResponseIntent.putExtra(VISITOR_IMAGE_FILE_PATH, imageAbsolutePath);
                        awaitingResponseIntent.putExtra(VISITOR_MOBILE_NUMBER, dataSnapshot.child(MOBILE_NUMBER).getValue(String.class));
                        awaitingResponseIntent.putExtra(VISITOR_IMAGE_URL, dataSnapshot.child(FIREBASE_CHILD_PROFILE_PHOTO).getValue(String.class));
                    }
                    mCtx.startActivity(awaitingResponseIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


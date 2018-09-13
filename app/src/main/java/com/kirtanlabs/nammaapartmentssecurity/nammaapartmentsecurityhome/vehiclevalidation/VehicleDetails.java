package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.vehiclevalidation;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

public class VehicleDetails extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textVehicleNumberValue, textVehicleOwnerNameValue,
            textVehicleTypeValue;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_vehicle_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.vehicle_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textVehicleNumber = findViewById(R.id.textVehicleNumber);
        TextView textVehicleOwnerName = findViewById(R.id.textVehicleOwnerName);
        TextView textVehicleType = findViewById(R.id.textVehicleType);
        textVehicleNumberValue = findViewById(R.id.textVehicleNumberValue);
        textVehicleOwnerNameValue = findViewById(R.id.textVehicleOwnerNameValue);
        textVehicleTypeValue = findViewById(R.id.textVehicleTypeValue);

        /*Setting font for all the views*/
        textVehicleNumber.setTypeface(setLatoRegularFont(this));
        textVehicleOwnerName.setTypeface(setLatoRegularFont(this));
        textVehicleType.setTypeface(setLatoRegularFont(this));
        textVehicleNumberValue.setTypeface(setLatoBoldFont(this));
        textVehicleOwnerNameValue.setTypeface(setLatoBoldFont(this));
        textVehicleTypeValue.setTypeface(setLatoBoldFont(this));

        String vehicleNumberTitle = getString(R.string.vehicle_number) + ":";
        textVehicleNumber.setText(vehicleNumberTitle);

        /*To retrieve Vehicle details from firebase*/
        retrieveVehicleDetailsFromFirebase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve vehicle details from (vehicle->private->vehicleUID) in firebase
     */
    private void retrieveVehicleDetailsFromFirebase() {
        String vehicleUID = getIntent().getStringExtra(Constants.VEHICLE_UID);
        DatabaseReference vehicleDetailsReference = Constants.PRIVATE_VEHICLES_REFERENCE
                .child(vehicleUID);
        /*Getting Vehicle Details from vehicle->private->vehicleUID in firebase*/
        vehicleDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String vehicleNumber = dataSnapshot.child(Constants.FIREBASE_CHILD_VEHICLE_NUMBER).getValue(String.class);
                String ownerName = dataSnapshot.child(Constants.FIREBASE_CHILD_OWNER_NAME).getValue(String.class);
                String vehicleType = dataSnapshot.child(Constants.FIREBASE_CHILD_VEHICLE_TYPE).getValue(String.class);

                textVehicleNumberValue.setText(vehicleNumber);
                textVehicleOwnerNameValue.setText(ownerName);
                textVehicleTypeValue.setText(vehicleType);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

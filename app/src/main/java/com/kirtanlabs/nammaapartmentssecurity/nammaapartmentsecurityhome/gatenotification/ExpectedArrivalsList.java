package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.ArrayList;
import java.util.List;

public class ExpectedArrivalsList extends BaseActivity {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private ExpectedArrivalsListAdapter adapter;
    private List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList;
    private int validationStatusOf;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expected_arrivals_list;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Cab Driver and Package Vendor Validation Status,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.expected_cab_arrivals) {
            validationStatusOf = R.string.cab_driver_validation_status;
        } else {
            validationStatusOf = R.string.package_vendor_validation_status;
        }
        return validationStatusOf;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id of recycler view*/
        RecyclerView recyclerViewExpectedArrivalsList = findViewById(R.id.recyclerViewExpectedArrivalsList);
        recyclerViewExpectedArrivalsList.setHasFixedSize(true);
        recyclerViewExpectedArrivalsList.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentExpectedArrivalsList = new ArrayList<>();
        adapter = new ExpectedArrivalsListAdapter(this, nammaApartmentExpectedArrivalsList);

        //Setting adapter to recycler view
        recyclerViewExpectedArrivalsList.setAdapter(adapter);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //To retrieve Cab driver and Package Vendor details from firebase
        retrieveDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveDataFromFireBase() {
        if (validationStatusOf == R.string.cab_driver_validation_status) {
            String cabDriverUid = getIntent().getStringExtra(Constants.EXPECTED_ARRIVAL_UID);

            DatabaseReference cabDriverReference = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.FIREBASE_CHILD_CABS)
                    .child(Constants.FIREBASE_CHILD_PUBLIC)
                    .child(cabDriverUid);
            cabDriverReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals = dataSnapshot.getValue(NammaApartmentExpectedArrivals.class);
                    nammaApartmentExpectedArrivalsList.add(0, nammaApartmentExpectedArrivals);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
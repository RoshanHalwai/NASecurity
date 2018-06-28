package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private int index = 0;

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
        adapter = new ExpectedArrivalsListAdapter(this, nammaApartmentExpectedArrivalsList, validationStatusOf);

        //Setting adapter to recycler view
        recyclerViewExpectedArrivalsList.setAdapter(adapter);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //To retrieve Cab driver and Package Vendor details from firebase
        retrieveExpectedArrivalDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveExpectedArrivalDataFromFireBase() {
        if (validationStatusOf == R.string.cab_driver_validation_status) {
            String cabDriverUid = getIntent().getStringExtra(Constants.EXPECTED_ARRIVAL_UID);

            // Retrieving all Details of Cab driver from(Cab->Public->CabDriverUid) in Firebase.
            DatabaseReference expectedArrivalReference = Constants.PUBLIC_CABS_REFERENCE
                    .child(cabDriverUid);
            expectedArrivalReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals = dataSnapshot.getValue(NammaApartmentExpectedArrivals.class);
                    assert nammaApartmentExpectedArrivals != null;
                    nammaApartmentExpectedArrivals.setExpectedArrivalUid(cabDriverUid);
                    nammaApartmentExpectedArrivalsList.add(index++, nammaApartmentExpectedArrivals);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            String apartment = getIntent().getStringExtra(Constants.FIREBASE_CHILD_APARTMENTS);
            String flat = getIntent().getStringExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER);

            // Retrieving UID of Package Vendor from (userData->private->apartment->flat) in firebase.
            DatabaseReference packageVendorReference = Constants.PRIVATE_USER_DATA_REFERENCE
                    .child(Constants.FIREBASE_CHILD_BANGALORE)
                    .child(Constants.FIREBASE_CHILD_SALARPURIA_CAMBRIDGE)
                    .child(apartment)
                    .child(flat);
            packageVendorReference.child(Constants.FIREBASE_CHILD_FLATMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ownerUidDataSnapshot : dataSnapshot.getChildren()) {
                        String ownerUid = ownerUidDataSnapshot.getKey();

                        // Getting Package Vendor UID from (UserData->private->apartment->flat->deliveries) in firebase.
                        packageVendorReference.child(Constants.FIREBASE_CHILD_DELIVERIES)
                                .child(ownerUid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot packageVendorUidDataSnapshot : dataSnapshot.getChildren()) {
                                            String packageVendorUid = packageVendorUidDataSnapshot.getKey();

                                            // Retrieving all Details of Package Vendor from (deliveries->public->PackageVendorUid) in firebase.
                                            DatabaseReference packageVendorReference = Constants.PUBLIC_DELIVERIES_REFERENCE
                                                    .child(packageVendorUid);
                                            packageVendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    hideProgressIndicator();
                                                    String status = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_STATUS).getValue();
                                                    assert status != null;
                                                    if (!status.equals(getString(R.string.left))) {
                                                        NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals = dataSnapshot.getValue(NammaApartmentExpectedArrivals.class);
                                                        assert nammaApartmentExpectedArrivals != null;
                                                        nammaApartmentExpectedArrivals.setExpectedArrivalUid(packageVendorUid);
                                                        nammaApartmentExpectedArrivalsList.add(index++, nammaApartmentExpectedArrivals);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
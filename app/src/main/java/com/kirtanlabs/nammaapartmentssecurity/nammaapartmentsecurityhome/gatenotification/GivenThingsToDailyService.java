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
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation.NammaApartmentDailyService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GivenThingsToDailyService extends BaseActivity {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private ThingsGivenToDailyServiceAdapter thingsGivenToDailyServiceAdapter;
    private String currentDate;
    private int index = 0;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_given_things_to_daily_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.things_given_to_daily_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id of recycler view*/
        RecyclerView recyclerViewGivenThings = findViewById(R.id.recyclerViewGivenThings);
        recyclerViewGivenThings.setHasFixedSize(true);
        recyclerViewGivenThings.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentDailyServiceList = new ArrayList<>();
        thingsGivenToDailyServiceAdapter = new ThingsGivenToDailyServiceAdapter(this, nammaApartmentDailyServiceList);

        //Setting adapter to recycler view
        recyclerViewGivenThings.setAdapter(thingsGivenToDailyServiceAdapter);

        //To retrieve daily service details from firebase
        retrieveDailyServiceDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve details of Daily service.
     */
    private void retrieveDailyServiceDataFromFireBase() {
        // Getting Current Date here.
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDate = simpleDateFormat.format(date);

        String dailyServiceUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_DAILYSERVICE_UID);
        String serviceType = getIntent().getStringExtra(Constants.SERVICE_TYPE);

        DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE
                .child(serviceType)
                .child(dailyServiceUid);

        /*Retrieving Each Owners UID of that particular Daily Service to check which owner of that Daily Service has given things*/
        dailyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dailyServiceDataSnapshot) {
                hideProgressIndicator();
                for (DataSnapshot ownersUidDataSnapshot : dailyServiceDataSnapshot.getChildren()) {
                    String ownersUid = ownersUidDataSnapshot.getKey();
                    if (!Constants.FIREBASE_CHILD_STATUS.equals(ownersUid)) {
                        /*Get data and checking if resident has given something to its daily service in today's date or not.*/
                        dailyServiceReference.
                                child(ownersUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(Constants.FIREBASE_CHILD_HANDED_THINGS).child(currentDate).exists()) {
                                    NammaApartmentDailyService nammaApartmentDailyService = dataSnapshot.getValue(NammaApartmentDailyService.class);
                                    assert nammaApartmentDailyService != null;
                                    nammaApartmentDailyService.setOwnerUid(ownersUid);
                                    nammaApartmentDailyService.setDailyServiceType(serviceType);
                                    nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                    thingsGivenToDailyServiceAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

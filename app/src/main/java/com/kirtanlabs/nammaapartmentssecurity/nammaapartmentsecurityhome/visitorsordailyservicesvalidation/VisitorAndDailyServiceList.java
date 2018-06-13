package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

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

public class VisitorAndDailyServiceList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private RecyclerView recyclerViewVisitorAndDailyServiceList;
    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private VisitorListAdapter visitorListAdapter;
    private DailyServiceListAdapter dailyServiceListAdapter;
    private int validationStatusOf;
    private String serviceType;
    private String dailyServiceUid;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitor_and_daily_service_list;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Valid Visitors and Daily Services,
         *we set the title based on the user navigating to the screen*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.visitors_validation) {
            validationStatusOf = R.string.visitor_validation_status;
        } else {
            validationStatusOf = R.string.daily_service_validation_status;
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
        recyclerViewVisitorAndDailyServiceList = findViewById(R.id.recyclerViewValidVisitorAndDailyService);
        recyclerViewVisitorAndDailyServiceList.setHasFixedSize(true);
        recyclerViewVisitorAndDailyServiceList.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentVisitorList = new ArrayList<>();
        nammaApartmentDailyServiceList = new ArrayList<>();
        visitorListAdapter = new VisitorListAdapter(this, nammaApartmentVisitorList);
        dailyServiceListAdapter = new DailyServiceListAdapter(this, nammaApartmentDailyServiceList, serviceType);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //To retrieve visitor and daily service details from firebase
        retrieveDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveDataFromFireBase() {
        if (validationStatusOf == R.string.visitor_validation_status) {
            String visitorUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_VISITOR_UID);

            FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_VISITORS)
                    .child(Constants.FIREBASE_CHILD_PREAPPROVEDVISITORS)
                    .child(visitorUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NammaApartmentVisitor nammaApartmentValidVisitor = dataSnapshot.getValue(NammaApartmentVisitor.class);
                    nammaApartmentVisitorList.add(0, nammaApartmentValidVisitor);
                    //Setting adapter to recycler view
                    recyclerViewVisitorAndDailyServiceList.setAdapter(visitorListAdapter);
                    visitorListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            dailyServiceUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_DAILYSERVICE_UID);
            DatabaseReference serviceTypeReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_DAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_ALL)
                    .child(Constants.FIREBASE_CHILD_PUBLIC)
                    .child(Constants.FIREBASE_CHILD_SERVICETYPE);
            serviceTypeReference.child(dailyServiceUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot serviceTypeDataSnapshot : dataSnapshot.getChildren()) {
                        serviceType = serviceTypeDataSnapshot.getKey();
                    }
                    DatabaseReference dailyServiceDataReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_DAILYSERVICES)
                            .child(Constants.FIREBASE_CHILD_ALL)
                            .child(Constants.FIREBASE_CHILD_PUBLIC)
                            .child(serviceType);
                    dailyServiceDataReference.child(dailyServiceUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentDailyService nammaApartmentDailyService = dataSnapshot.getValue(NammaApartmentDailyService.class);
                            nammaApartmentDailyService.setDailyServiceType(serviceType);
                            nammaApartmentDailyServiceList.add(0, nammaApartmentDailyService);
                            //Setting adapter to recycler view
                            recyclerViewVisitorAndDailyServiceList.setAdapter(dailyServiceListAdapter);
                            dailyServiceListAdapter.notifyDataSetChanged();
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
        hideProgressIndicator();
    }
}

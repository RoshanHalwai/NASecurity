package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

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
import java.util.Objects;

public class VisitorAndDailyServiceList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private RecyclerView recyclerViewVisitorAndDailyServiceList;
    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private VisitorListAdapter visitorListAdapter;
    private DailyServiceListAdapter dailyServiceListAdapter;
    private NammaApartmentDailyService nammaApartmentDailyService;
    private int validationStatusOf;
    private String serviceType;
    private String dailyServiceStatus;

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

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id of recycler view*/
        recyclerViewVisitorAndDailyServiceList = findViewById(R.id.recyclerViewValidVisitorAndDailyService);
        recyclerViewVisitorAndDailyServiceList.setHasFixedSize(true);
        recyclerViewVisitorAndDailyServiceList.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentVisitorList = new ArrayList<>();
        nammaApartmentDailyServiceList = new ArrayList<>();
        visitorListAdapter = new VisitorListAdapter(this, nammaApartmentVisitorList);
        dailyServiceListAdapter = new DailyServiceListAdapter(this, nammaApartmentDailyServiceList);

        //To retrieve visitor and daily service details from firebase
        retrieveDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method invoked to retrieve data of Visitors and Daily service from firebase.
     */
    private void retrieveDataFromFireBase() {
        if (validationStatusOf == R.string.visitor_validation_status) {
            String visitorUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_VISITOR_UID);

            DatabaseReference visitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                    .child(visitorUid);
            /*Get data and add to the list for displaying in Visitor List*/
            visitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
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
            serviceType = getIntent().getStringExtra(Constants.FIREBASE_CHILD_DAILYSERVICETYPE);
            String dailyServiceUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_DAILYSERVICE_UID);
            DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE
                    .child(serviceType)
                    .child(dailyServiceUid);

            /*Retrieving Each Owners UID of that particular Daily Service */
            dailyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dailyServiceDataSnapshot) {
                    dailyServiceStatus = (String) dailyServiceDataSnapshot.child(Constants.FIREBASE_CHILD_STATUS).getValue();
                    hideProgressIndicator();
                    for (DataSnapshot ownersUidDataSnapshot : dailyServiceDataSnapshot.getChildren()) {
                        String ownersUid = ownersUidDataSnapshot.getKey();
                        if (!Constants.FIREBASE_CHILD_STATUS.equals(ownersUid)) {
                            /*Get data and add to the list for displaying in Daily Service List*/
                            dailyServiceReference.
                                    child(ownersUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    nammaApartmentDailyService = dataSnapshot.getValue(NammaApartmentDailyService.class);
                                    assert nammaApartmentDailyService != null;
                                    nammaApartmentDailyService.setOwnerUid(ownersUid);
                                    Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(serviceType.substring(0, 1).toUpperCase() + serviceType.substring(1));
                                    nammaApartmentDailyService.setStatus(dailyServiceStatus);
                                    nammaApartmentDailyServiceList.add(0, nammaApartmentDailyService);
                                    dailyServiceListAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    //Setting adapter to recycler view
                    recyclerViewVisitorAndDailyServiceList.setAdapter(dailyServiceListAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}

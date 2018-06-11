package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    private List<NammaApartmentVisitor> nammaApartmentValidVisitorList;
    private ValidVisitorAndDailyServiceListAdapter adapter;
    private int validationStatusOf;

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
        RecyclerView recyclerViewValidVisitorAndDailyService = findViewById(R.id.recyclerViewValidVisitorAndDailyService);
        recyclerViewValidVisitorAndDailyService.setHasFixedSize(true);
        recyclerViewValidVisitorAndDailyService.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentValidVisitorList = new ArrayList<>();
        adapter = new ValidVisitorAndDailyServiceListAdapter(this, validationStatusOf, nammaApartmentValidVisitorList);

        //Setting adapter to recycler view
        recyclerViewValidVisitorAndDailyService.setAdapter(adapter);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //To retrieve visitor details from firebase
        retrieveDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveDataFromFireBase() {
        String visitorUid = getIntent().getStringExtra(Constants.FIREBASE_CHILD_VISITOR_UID);

        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_VISITORS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_PREAPPROVEDVISITORS)
                .child(visitorUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentVisitor nammaApartmentValidVisitor = dataSnapshot.getValue(NammaApartmentVisitor.class);
                nammaApartmentValidVisitorList.add(nammaApartmentValidVisitor);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        hideProgressIndicator();
    }
}

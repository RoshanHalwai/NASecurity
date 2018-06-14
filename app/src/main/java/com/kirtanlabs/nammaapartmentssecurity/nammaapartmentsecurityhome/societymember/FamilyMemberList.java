package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

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

public class FamilyMemberList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NammaApartmentFamilyMember> nammaApartmentFamilyMembersList;
    private FamilyMemberListAdapter adapter;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_family_member_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.family_member_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentFamilyMembersList = new ArrayList<>();
        adapter = new FamilyMemberListAdapter(this, nammaApartmentFamilyMembersList);

        //Setting adapter to recycler view
        recyclerView.setAdapter(adapter);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //To retrieve family member details from firebase
        retrieveDataFromFireBase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to Retrieve details of Family member from FireBase
     */
    private void retrieveDataFromFireBase() {
        String flatNumber = getIntent().getStringExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER);
        DatabaseReference familyMemberUid = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_FLATS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(flatNumber);
        familyMemberUid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                for (DataSnapshot familyMemberDataSnapshot : dataSnapshot.getChildren()) {
                    String familyMemberUid = familyMemberDataSnapshot.getKey();

                    DatabaseReference familyMemberDetails = FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FIREBASE_CHILD_USERS)
                            .child(Constants.FIREBASE_CHILD_PRIVATE);
                    assert familyMemberUid != null;
                    familyMemberDetails.child(familyMemberUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentFamilyMember nammaApartmentFamilyMember = dataSnapshot.getValue(NammaApartmentFamilyMember.class);
                            nammaApartmentFamilyMembersList.add(0, nammaApartmentFamilyMember);
                            adapter.notifyDataSetChanged();
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
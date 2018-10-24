package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.SEARCH_FLAT_NUMBER_REQUEST_CODE;

public class Emergency extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editSearchFlatNumber;
    private TextView textFeatureUnavailable;
    private LinearLayout layoutNoEmergency;
    private DatabaseReference emergencyReference;
    private List<NammaApartmentEmergency> nammaApartmentEmergencyList;
    private EmergencyAdapter adapter;
    private String emergencyUID;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_emergency;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.emergency;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        editSearchFlatNumber = findViewById(R.id.editSearchFlatNumber);
        textFeatureUnavailable = findViewById(R.id.textFeatureUnavailable);
        layoutNoEmergency = findViewById(R.id.layoutNoEmergency);
        RecyclerView recyclerViewEmergency = findViewById(R.id.recyclerViewEmergency);

        /*Setting font for all the views*/
        textFeatureUnavailable.setTypeface(Constants.setLatoItalicFont(this));

        /*Creating recycler view adapter*/
        nammaApartmentEmergencyList = new ArrayList<>();
        recyclerViewEmergency.setHasFixedSize(true);
        recyclerViewEmergency.setLayoutManager(new LinearLayoutManager(this));

        /*Creating recycler view adapter*/
        adapter = new EmergencyAdapter(this, nammaApartmentEmergencyList);

        /*Setting adapter to recycler view*/
        recyclerViewEmergency.setAdapter(adapter);

        /*We don't want the keyboard to be displayed when user clicks on the time edit field*/
        editSearchFlatNumber.setInputType(InputType.TYPE_NULL);

        /*Setting onClickListener for view*/
        editSearchFlatNumber.setOnClickListener(this);
        editSearchFlatNumber.setOnFocusChangeListener(this);

        /*To retrieve all details of emergency occurred in society from firebase*/
        retrieveEmergencyDetailsFromFireBase();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Emergency.this, SearchFlatNumber.class);
        startActivityForResult(intent, SEARCH_FLAT_NUMBER_REQUEST_CODE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(editSearchFlatNumber);
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_FLAT_NUMBER_REQUEST_CODE && resultCode == RESULT_OK) {
            String flatNumber = data.getStringExtra(Constants.FLAT_NUMBER);
            DatabaseReference flatNumberReference = Constants.ALL_EMERGENCIES_REFERENCE.child(flatNumber);
            /*Getting Emergency UID from emergencies->private->all in firebase.*/
            flatNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nammaApartmentEmergencyList.clear();
                    adapter.notifyDataSetChanged();
                    if (dataSnapshot.exists()) {
                        layoutNoEmergency.setVisibility(View.GONE);
                        for (DataSnapshot emergencyUidDataSnapshot : dataSnapshot.getChildren()) {
                            emergencyUID = emergencyUidDataSnapshot.getKey();
                            displayEmergencyList(emergencyUID);
                        }
                    } else {
                        layoutNoEmergency.setVisibility(View.VISIBLE);
                        textFeatureUnavailable.setText(R.string.no_emergency_raised_in_this_flat_message);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /*-------------------------------------------------------------------------------
     * Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked to Retrieve details of emergency occurred in society from firebase
     */
    private void retrieveEmergencyDetailsFromFireBase() {
        emergencyReference = Constants.PUBLIC_EMERGENCIES_REFERENCE;
        emergencyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    editSearchFlatNumber.setVisibility(View.VISIBLE);
                    for (DataSnapshot emergencyUidDataSnapshot : dataSnapshot.getChildren()) {
                        /*Getting Emergency UID from emergencies->public*/
                        emergencyUID = emergencyUidDataSnapshot.getKey();
                        displayEmergencyList(emergencyUID);
                    }
                } else {
                    layoutNoEmergency.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to display list of all emergencies occurred in a particular society.
     *
     * @param emergencyUID contains that Emergency UID of that respected flat number.
     */
    private void displayEmergencyList(String emergencyUID) {
        /*Retrieving details of Emergency from emergencies->public->emergencyUID from firebase and adding in a list.*/
        emergencyReference
                .child(emergencyUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentEmergency nammaApartmentEmergency = dataSnapshot.getValue(NammaApartmentEmergency.class);
                nammaApartmentEmergencyList.add(nammaApartmentEmergency);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

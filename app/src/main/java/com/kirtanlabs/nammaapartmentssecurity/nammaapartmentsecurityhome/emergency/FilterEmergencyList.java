package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*We are not using this file. This file is kept on hold for future reference*/

public class FilterEmergencyList extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ArrayAdapter<String> adapter;
    private final List<String> itemsInList = new ArrayList<>();

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_filter_emergency_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.Filter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RadioButton radioButtonApartment = findViewById(R.id.radioButtonApartment);
        RadioButton radioButtonFlat = findViewById(R.id.radioButtonFlat);
        RadioButton radioButtonDate = findViewById(R.id.radioButtonDate);
        ListView listView = findViewById(R.id.listView);

        /*Setting font for all the items in the list view*/
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsInList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textFlatNumber = view.findViewById(android.R.id.text1);
                textFlatNumber.setTypeface(Constants.setLatoRegularFont(FilterEmergencyList.this));
                return view;
            }
        };
        listView.setAdapter(adapter);

        /*Setting onClickListener for view*/
        radioButtonApartment.setOnClickListener(this);
        radioButtonFlat.setOnClickListener(this);
        radioButtonDate.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        itemsInList.clear();
        adapter.notifyDataSetChanged();
        switch (v.getId()) {
            case R.id.radioButtonApartment:
                displayApartmentsInList();
                break;
            case R.id.radioButtonFlat:
                displayFlatsInList();
                break;
            case R.id.radioButtonDate:
                Toast.makeText(this, "Yet to Implement", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Updates list according with all Apartment values.
     */
    private void displayApartmentsInList() {
        DatabaseReference apartmentReference = Constants.CITIES_REFERENCE
                .child(Constants.FIREBASE_CHILD_BANGALURU)
                .child(Constants.FIREBASE_CHILD_SOCIETIES)
                .child(Constants.FIREBASE_CHILD_SALARPURIA_CAMBRIDGE)
                .child(Constants.FIREBASE_CHILD_APARTMENTS);
        // Retrieving List of all Apartment names from firebase.
        apartmentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot apartmentDataSnapshot : dataSnapshot.getChildren()) {
                    itemsInList.add(apartmentDataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
                Collections.sort(itemsInList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * Updates list according with all Flats values.
     */
    private void displayFlatsInList() {
        itemsInList.clear();
        DatabaseReference apartmentReference = Constants.CITIES_REFERENCE
                .child(Constants.FIREBASE_CHILD_BANGALURU)
                .child(Constants.FIREBASE_CHILD_SOCIETIES)
                .child(Constants.FIREBASE_CHILD_SALARPURIA_CAMBRIDGE)
                .child(Constants.FIREBASE_CHILD_APARTMENTS);
        // Retrieving List of all Apartment names from firebase.
        apartmentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot apartmentDataSnapshot : dataSnapshot.getChildren()) {
                    String apartmentName = apartmentDataSnapshot.getKey();

                    // Retrieving List flat number of each apartment from firebase.
                    apartmentReference
                            .child(apartmentName)
                            .child(Constants.FIREBASE_CHILD_FLATS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot flatDataSnapshot : dataSnapshot.getChildren()) {
                                itemsInList.add(flatDataSnapshot.getKey());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                Collections.sort(itemsInList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

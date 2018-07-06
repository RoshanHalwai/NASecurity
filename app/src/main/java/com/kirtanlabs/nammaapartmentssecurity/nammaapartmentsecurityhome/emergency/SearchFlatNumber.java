package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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

public class SearchFlatNumber extends BaseActivity implements SearchView.OnQueryTextListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ArrayAdapter<String> flatNumberAdapter;
    private List<String> itemsInList = new ArrayList<>();

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search_flat_number;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.search_flat_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listView = findViewById(R.id.listView);
        SearchView searchFlatNumber = findViewById(R.id.searchFlatNumber);

        /*Setting font for all the items in the list view*/
        flatNumberAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsInList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(SearchFlatNumber.this));
                return view;
            }
        };
        //Setting adapter to listView view
        listView.setAdapter(flatNumberAdapter);

        /*Attaching listeners to ListView*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String flatNumber = (String) listView.getItemAtPosition(position);
            searchSelectedFlat(flatNumber);
        });

        // Adding all flat numbers in a list.
        initializeListWithFlatNumbers();

        /*Setting OnQueryTextListener for view*/
        searchFlatNumber.setOnQueryTextListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnQueryTextListener Methods
     * ------------------------------------------------------------- */

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchSelectedFlat(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        flatNumberAdapter.getFilter().filter(newText);
        return true;
    }


    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Updates list with all Flats values.
     */
    private void initializeListWithFlatNumbers() {
        DatabaseReference apartmentReference = Constants.CITIES_REFERENCE
                .child(Constants.FIREBASE_CHILD_BANGALORE)
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
                                flatNumberAdapter.notifyDataSetChanged();
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

    /**
     * This method is invoked to send back flat number to Emergency Screen to display details for that particular flat.
     *
     * @param flatNumber of which emergency details to be displayed.
     */
    private void searchSelectedFlat(String flatNumber) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FLAT_NUMBER, flatNumber);
        setResult(RESULT_OK, intent);
        finish();
    }
}

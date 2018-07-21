package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification.ExpectedArrivalsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SocietyMemberAndExpectedPackageArrival extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textFlat;
    private Button buttonVerifyResidentAndPackageVendor;
    private EditText editApartment;
    private EditText editFlat;
    private Dialog dialog;
    private ListView listView;
    private DatabaseReference flatReference;
    private ArrayAdapter<String> adapter;
    private List<String> itemsInList = new ArrayList<>();
    private int screenTitle;
    private String apartment;
    private String flat;
    private boolean vendorsToArrive;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_member_and_expected_package_arrival;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Society Member and Expected Package Arrivals so,
         *we set the title based on the user navigating to the screen*/
        screenTitle = getIntent().getIntExtra(Constants.SCREEN_TITLE, 0);
        if (screenTitle == R.string.society_member) {
            screenTitle = R.string.society_member;
        } else {
            screenTitle = R.string.expected_package_arrivals;
        }
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textApartment = findViewById(R.id.textApartment);
        textFlat = findViewById(R.id.textFlat);
        editApartment = findViewById(R.id.editApartment);
        editFlat = findViewById(R.id.editFlat);
        buttonVerifyResidentAndPackageVendor = findViewById(R.id.buttonVerifyResidentAndPackageVendor);

        /*Setting font for all the views*/
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyResidentAndPackageVendor.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same layout for Society Members and Expected Package Arrivals we need to
         *change buttonVerifyResident Text in Expected Package Arrivals*/
        if (screenTitle == R.string.expected_package_arrivals) {
            String verifyTo = getString(R.string.verify_package_vendor);
            buttonVerifyResidentAndPackageVendor.setText(verifyTo);
        }

        /*Allow users to search for Apartment and Flat*/
        initializeList();

        /*We don't want the keyboard to be displayed when user clicks edit views*/
        editApartment.setInputType(InputType.TYPE_NULL);
        editFlat.setInputType(InputType.TYPE_NULL);

        /*Show only Apartment during start of activity*/
        hideViews();

        /*Attaching listeners to Views*/
        editApartment.requestFocus();
        editApartment.setOnFocusChangeListener(this);
        editFlat.setOnFocusChangeListener(this);

        editApartment.setOnClickListener(this);
        editFlat.setOnClickListener(this);
        buttonVerifyResidentAndPackageVendor.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editApartment:
                searchItemInList(R.id.editApartment);
                break;
            case R.id.editFlat:
                searchItemInList(R.id.editFlat);
                break;
            case R.id.buttonVerifyResidentAndPackageVendor:
                /*We need Progress Indicator in this screen*/
                showProgressIndicator();
                checkFlatMembersDetailsInFirebase();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(v);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void initializeList() {
        dialog = new Dialog(SocietyMemberAndExpectedPackageArrival.this);
        dialog.setContentView(R.layout.apartment_and_flats_listview);
        listView = dialog.findViewById(R.id.list);

        /*Setting font for all the items in the list view*/
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsInList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(SocietyMemberAndExpectedPackageArrival.this));
                return view;
            }
        };
        listView.setAdapter(adapter);

        /*Attaching listeners to ListView*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) listView.getItemAtPosition(position);
            int viewId = Objects.requireNonNull(getCurrentFocus()).getId();
            showViews(viewId);
            dialog.cancel();
            ((EditText) findViewById(viewId)).setText(itemValue);
        });

    }

    /**
     * Updates list according to views
     *
     * @param viewId of edit text views
     */
    private void searchItemInList(int viewId) {
        itemsInList.clear();
        switch (viewId) {
            case R.id.editApartment:
                hideViews();
                updateItemsInList(Constants.APARTMENTS_REFERENCE
                        .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY));
                break;
            case R.id.editFlat:
                updateItemsInList(Constants.FLATS_REFERENCE
                        .child(editApartment.getText().toString()));
                break;
        }
    }

    /**
     * Updates the list by getting values from firebase and shows it to user
     *
     * @param databaseReference for getting values from firebase
     */
    private void updateItemsInList(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    itemsInList.add(values.getKey());
                }
                adapter.notifyDataSetChanged();
                Collections.sort(itemsInList);
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Hides view which are not required
     */
    private void hideViews() {
        textFlat.setVisibility(View.INVISIBLE);
        editFlat.setVisibility(View.INVISIBLE);
        buttonVerifyResidentAndPackageVendor.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows view which are required
     *
     * @param viewId from which other views need to be shown
     */
    private void showViews(int viewId) {
        switch (viewId) {
            case R.id.editApartment:
                textFlat.setVisibility(View.VISIBLE);
                editFlat.setVisibility(View.VISIBLE);
                editFlat.getText().clear();
                break;
            case R.id.editFlat:
                buttonVerifyResidentAndPackageVendor.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * This method is used to check whether person has given his own valid apartment & flat or not
     * and also package vendor has given correct flat and apartment details or not.
     */
    private void checkFlatMembersDetailsInFirebase() {
        apartment = editApartment.getText().toString();
        flat = editFlat.getText().toString();

        //Database Reference for Retrieving all details of that particular flat from (userData->private->apartment->flat) in firebase.
        flatReference = Constants.PRIVATE_USER_DATA_REFERENCE
                .child(Constants.FIREBASE_CHILD_BANGALURU)
                .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                .child(apartment)
                .child(flat);
        if (screenTitle == R.string.society_member) {
            // Checking if any members lives in this flat or not.
            flatReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hideProgressIndicator();
                    if (dataSnapshot.hasChildren()) {
                        openFlatMemberOrPackageArrivalList();
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.no_member_in_this_flat));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            // Checking if any member in this particular flat has ordered any package or not.
            DatabaseReference packageVendorReference = flatReference.child(Constants.FIREBASE_CHILD_DELIVERIES);
            packageVendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot packageDataSnapshot) {

                    hideProgressIndicator();

                    if (packageDataSnapshot.exists()) {
                        for (DataSnapshot ownerUID : packageDataSnapshot.getChildren()) {
                            DatabaseReference ownerUIDReference = flatReference.child(Constants.FIREBASE_CHILD_DELIVERIES).child(ownerUID.getKey());
                            ownerUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot deliveryUID : dataSnapshot.getChildren()) {
                                        if (deliveryUID.getValue(Boolean.class)) {
                                            vendorsToArrive = true;
                                            break;
                                        }
                                    }

                                    if (vendorsToArrive) {
                                        openFlatMemberOrPackageArrivalList();
                                    } else {
                                        openValidationStatusDialog(Constants.FAILED, getString(R.string.not_ordered_any_packages));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    } else {
                        openValidationStatusDialog(Constants.FAILED, getString(R.string.not_ordered_any_packages));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * This method is invoked to open List of Family Member or Expected Arrival List of that particular flat
     * according to the screen title.
     */
    private void openFlatMemberOrPackageArrivalList() {
        Intent intent;
        if (screenTitle == R.string.society_member) {
            intent = new Intent(SocietyMemberAndExpectedPackageArrival.this, FamilyMemberList.class);
        } else {
            intent = new Intent(SocietyMemberAndExpectedPackageArrival.this, ExpectedArrivalsList.class);
            intent.putExtra(Constants.SCREEN_TITLE, R.string.expected_package_arrivals);
        }
        intent.putExtra(Constants.FIREBASE_CHILD_APARTMENTS, apartment);
        intent.putExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER, flat);
        startActivity(intent);
        finish();
    }
}

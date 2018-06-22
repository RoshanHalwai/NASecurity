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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;

public class SocietyMember extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textFlat;
    private Button buttonVerifySocietyMember;
    private EditText editApartment;
    private EditText editFlat;
    private Dialog dialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> itemsInList = new ArrayList<>();

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_member;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.society_member;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textApartment = findViewById(R.id.textApartment);
        textFlat = findViewById(R.id.textFlat);
        editApartment = findViewById(R.id.editApartment);
        editFlat = findViewById(R.id.editFlat);
        buttonVerifySocietyMember = findViewById(R.id.buttonVerifySocietyMember);

        /*Setting font for all the views*/
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifySocietyMember.setTypeface(Constants.setLatoLightFont(this));

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
        buttonVerifySocietyMember.setOnClickListener(this);
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
            case R.id.buttonVerifySocietyMember:
                if (editFlat.length() > EDIT_TEXT_MIN_LENGTH) {
                    /*We need Progress Indicator in this screen*/
                    showProgressIndicator();
                    checkFlatMembersInFirebase(editApartment.getText().toString().trim(), editFlat.getText().toString().trim());
                } else {
                    editFlat.setError(getString(R.string.field_cant_be_empty));
                }
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
        dialog = new Dialog(SocietyMember.this);
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
                textView.setTypeface(Constants.setLatoRegularFont(SocietyMember.this));
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
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(Constants.FIREBASE_CHILD_BANGALORE)
                        .child(Constants.FIREBASE_CHILD_SOCIETIES)
                        .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                        .child(Constants.FIREBASE_CHILD_APARTMENTS));
                break;
            case R.id.editFlat:
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(Constants.FIREBASE_CHILD_BANGALORE)
                        .child(Constants.FIREBASE_CHILD_SOCIETIES)
                        .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                        .child(Constants.FIREBASE_CHILD_APARTMENTS)
                        .child(editApartment.getText().toString())
                        .child(Constants.FIREBASE_CHILD_FLATS));
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
                    adapter.notifyDataSetChanged();
                }
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
        buttonVerifySocietyMember.setVisibility(View.INVISIBLE);
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
                buttonVerifySocietyMember.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * This method is used to check whether person has given his own valid apartment & flat or not
     *
     * @param apartment - that need to be checked whether it is present in Firebase or not
     * @param flat      - that need to be checked whether it is present in Firebase or not
     */
    private void checkFlatMembersInFirebase(final String apartment, final String flat) {
        final DatabaseReference apartmentReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_USERDATA)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_BANGALORE)
                .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                .child(apartment)
                .child(flat);
        apartmentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.hasChildren()) {
                    Intent intent = new Intent(SocietyMember.this, FamilyMemberList.class);
                    intent.putExtra(Constants.FIREBASE_CHILD_APARTMENTS, apartment);
                    intent.putExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER, flat);
                    startActivity(intent);
                    finish();
                } else {
                    openValidationStatusDialog(Constants.FAILED, getString(R.string.no_member_in_this_flat));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

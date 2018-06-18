package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

    private Dialog dialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> itemsInList = new ArrayList<>();
    private EditText editApartment;
    private EditText editFlat;

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
        TextView textFlat = findViewById(R.id.textFlat);
        editApartment = findViewById(R.id.editApartment);
        editFlat = findViewById(R.id.editFlat);
        Button buttonVerifySocietyMember = findViewById(R.id.buttonVerifySocietyMember);

        /*Setting font for all the views*/
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifySocietyMember.setTypeface(Constants.setLatoLightFont(this));

        /*Allow users to search for Apartment and Flat*/
        initializeListWithSearchView();

        /*Show only Apartment during start of activity*/
        hideViews(R.id.editApartment);

        /*We don't want the keyboard to be displayed when user clicks edit views*/
        editApartment.setInputType(InputType.TYPE_NULL);
        editFlat.setInputType(InputType.TYPE_NULL);

        /*Attaching listeners to Views*/
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
                    checkFlatNumberInFirebase(editApartment.getText().toString().trim(), editFlat.getText().toString().trim());
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

    private void initializeListWithSearchView() {
        dialog = new Dialog(SocietyMember.this);
        dialog.setContentView(R.layout.blocks_and_flats_listview);

        EditText inputSearch = dialog.findViewById(R.id.inputSearch);
        listView = dialog.findViewById(R.id.list);
        inputSearch.setTypeface(Constants.setLatoItalicFont(SocietyMember.this));

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

        /*Attaching listeners to Search Field*/
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                SocietyMember.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    /**
     * Hides unwanted views and updates list
     *
     * @param viewId of edit text views
     */
    private void searchItemInList(int viewId) {
        itemsInList.clear();
        switch (viewId) {
            case R.id.editApartment:
                hideViews(R.id.editApartment);
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(Constants.FIREBASE_CHILD_BANGALORE)
                        .child(Constants.FIREBASE_CHILD_SOCIETIES)
                        .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                        .child(Constants.FIREBASE_CHILD_APARTMENTS));
                break;
            case R.id.editFlat:
                hideViews(R.id.editFlat);
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
     *
     * @param viewId from which other views need to be hidden
     */
    private void hideViews(int viewId) {
        switch (viewId) {
            case R.id.editApartment:
            case R.id.textFlat:
                findViewById(R.id.textFlat).setVisibility(View.INVISIBLE);
                findViewById(R.id.editFlat).setVisibility(View.INVISIBLE);
            case R.id.editFlat:
                findViewById(R.id.buttonVerifySocietyMember).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Shows view which are required
     *
     * @param viewId from which other views need to be shown
     */
    private void showViews(int viewId) {
        switch (viewId) {
            case R.id.editApartment:
            case R.id.textFlat:
                findViewById(R.id.textFlat).setVisibility(View.VISIBLE);
                findViewById(R.id.editFlat).setVisibility(View.VISIBLE);
                break;
            case R.id.editFlat:
                findViewById(R.id.buttonVerifySocietyMember).setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is used to check whether person has given his own valid apartment & flat or not
     *
     * @param apartment - that need to be checked whether it is present in Firebase or not
     * @param flat      - that need to be checked whether it is present in Firebase or not
     */
    private void checkFlatNumberInFirebase(final String apartment, final String flat) {
        final DatabaseReference apartmentReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_USERDATA)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_BANGALORE)
                .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                .child(apartment);
        apartmentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    apartmentReference.child(flat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent intent = new Intent(SocietyMember.this, FamilyMemberList.class);
                                intent.putExtra(Constants.FIREBASE_CHILD_APARTMENTS, apartment);
                                intent.putExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER, flat);
                                startActivity(intent);
                                finish();
                            } else {
                                String invalidFlatNumber = getResources().getString(R.string.invalid_visitor);
                                invalidFlatNumber = invalidFlatNumber.replace("Visitor", "Flat");
                                openValidationStatusDialog(Constants.FAILED, invalidFlatNumber);
                            }
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

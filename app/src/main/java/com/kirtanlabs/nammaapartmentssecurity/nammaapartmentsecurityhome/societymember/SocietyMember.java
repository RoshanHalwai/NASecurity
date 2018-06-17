package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.EDIT_TEXT_MIN_LENGTH;

public class SocietyMember extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editBlockName;
    private EditText editFlatNumber;

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
        TextView textBlockName = findViewById(R.id.textBlockName);
        TextView textFlatNumber = findViewById(R.id.textFlatNumber);
        editBlockName = findViewById(R.id.editBlockName);
        editFlatNumber = findViewById(R.id.editFlatNumber);
        Button buttonVerifySocietyMember = findViewById(R.id.buttonVerifySocietyMember);

        /*Setting font for all the views*/
        textBlockName.setTypeface(Constants.setLatoBoldFont(this));
        textFlatNumber.setTypeface(Constants.setLatoBoldFont(this));
        editBlockName.setTypeface(Constants.setLatoRegularFont(this));
        editFlatNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifySocietyMember.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonVerifySocietyMember.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (editFlatNumber.length() > EDIT_TEXT_MIN_LENGTH) {
            /*We need Progress Indicator in this screen*/
            showProgressIndicator();
            checkFlatNumberInFirebase(editBlockName.getText().toString().trim(), editFlatNumber.getText().toString().trim());
        } else {
            editFlatNumber.setError(getString(R.string.field_cant_be_empty));
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether person has given his own valid flat number or not
     *
     * @param blockName  - that need to be checked whether it is present in Firebase or not
     * @param flatNumber - that need to be checked whether it is present in Firebase or not
     */
    private void checkFlatNumberInFirebase(final String blockName, final String flatNumber) {
        final DatabaseReference blockNameReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_USERDATA)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_BANGALORE)
                .child(Constants.FIREBASE_CHILD_BRIGADEGATEWAY)
                .child(blockName);
        blockNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    blockNameReference.child(flatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent intent = new Intent(SocietyMember.this, FamilyMemberList.class);
                                intent.putExtra(Constants.FIREBASE_CHILD_BLOCKNAME, blockName);
                                intent.putExtra(Constants.FIREBASE_CHILD_FLAT_NUMBER, flatNumber);
                                startActivity(intent);
                                finish();
                            } else {
                                String invalidFlatNumber = getResources().getString(R.string.invalid_visitor);
                                invalidFlatNumber = invalidFlatNumber.replace("Visitor", "Flat Number");
                                openValidationStatusDialog(Constants.FAILED, invalidFlatNumber);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    String invalidBlockName = getResources().getString(R.string.invalid_visitor);
                    invalidBlockName = invalidBlockName.replace("Visitor", "Block Name");
                    openValidationStatusDialog(Constants.FAILED, invalidBlockName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

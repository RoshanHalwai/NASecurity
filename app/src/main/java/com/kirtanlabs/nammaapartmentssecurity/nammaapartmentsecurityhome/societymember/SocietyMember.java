package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        TextView textFlatNumber = findViewById(R.id.textFlatNumber);
        editFlatNumber = findViewById(R.id.editFlatNumber);
        Button buttonVerifySocietyMember = findViewById(R.id.buttonVerifySocietyMember);

        /*Setting font for all the views*/
        textFlatNumber.setTypeface(Constants.setLatoBoldFont(this));
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
            checkFlatNumberInFirebase(editFlatNumber.getText().toString().trim());
        } else {
            editFlatNumber.setError(getString(R.string.flat_number_validation));
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether person has given his own valid flat number or not
     *
     * @param flatNumber - that need to be checked whether it is present in Firebase or not
     */
    private void checkFlatNumberInFirebase(final String flatNumber) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_FLATS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(flatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.exists()) {
                    Intent intent = new Intent(SocietyMember.this, FamilyMemberList.class);
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
    }
}

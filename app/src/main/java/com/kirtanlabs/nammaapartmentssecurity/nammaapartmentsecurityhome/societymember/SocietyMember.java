package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class SocietyMember extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private View invalidFlatNumberDialog;
    private AlertDialog dialog;
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

        switch (v.getId()) {
            case R.id.buttonVerifySocietyMember:
                if (isValidFlatNumber()) {
                    startActivity(new Intent(SocietyMember.this, FamilyMemberList.class));
                    finish();
                } else {
                    openInvalidFlatNumberDialog();
                }
                break;
            case R.id.buttonOk:
                dialog.cancel();
                editFlatNumber.setText("");
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check whether person has given his own valid flat number or not
     *
     * @return it will return boolean value whether flat number is valid or not
     */
    private boolean isValidFlatNumber() {
        boolean check;
        String flatNumber = editFlatNumber.getText().toString().trim();
        // TODO : To change Flat number here
        check = flatNumber.equalsIgnoreCase("c504");
        return check;
    }

    /**
     * This method is invoked when Resident gives invalid flat number
     */
    private void openInvalidFlatNumberDialog() {
        invalidFlatNumberDialog = View.inflate(this, R.layout.layout_things_given_dialog, null);
        LinearLayout layoutValidationFailed = invalidFlatNumberDialog.findViewById(R.id.layoutValidationFailed);
        TextView textInvalidFlatNumber = invalidFlatNumberDialog.findViewById(R.id.textNotGivenThings);
        Button buttonOk = invalidFlatNumberDialog.findViewById(R.id.buttonOk);

        /*Setting fonts to the views*/
        textInvalidFlatNumber.setTypeface(Constants.setLatoBoldFont(this));
        buttonOk.setTypeface(Constants.setLatoLightFont(this));

        layoutValidationFailed.setVisibility(View.VISIBLE);

        String invalidType = getResources().getString(R.string.invalid_visitor);
        invalidType = invalidType.replace("Visitor", "Flat Number");
        textInvalidFlatNumber.setText(invalidType);

        /*This method is used to create createInvalidFlatNumberDialog*/
        createInvalidFlatNumberDialog();

        /*Setting onClickListener for view*/
        buttonOk.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a Invalid Flat Number dialog
     */
    private void createInvalidFlatNumberDialog() {
        AlertDialog.Builder alertInvalidFlatNumber = new AlertDialog.Builder(this);
        alertInvalidFlatNumber.setView(invalidFlatNumberDialog);

        dialog = alertInvalidFlatNumber.create();
        new Dialog(this);
        dialog.show();
    }
}

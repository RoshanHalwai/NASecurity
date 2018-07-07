package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.SEARCH_FLAT_NUMBER_REQUEST_CODE;

public class Emergency extends BaseActivity implements View.OnClickListener {

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
        EditText editSearchFlatNumber = findViewById(R.id.editSearchFlatNumber);
        RecyclerView recyclerViewEmergency = findViewById(R.id.recyclerViewEmergency);
        recyclerViewEmergency.setHasFixedSize(true);
        recyclerViewEmergency.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        EmergencyAdapter adapter = new EmergencyAdapter(this);

        //Setting adapter to recycler view
        recyclerViewEmergency.setAdapter(adapter);

        /*We don't want the keyboard to be displayed when user clicks on the time edit field*/
        editSearchFlatNumber.setInputType(InputType.TYPE_NULL);

        /*Setting onClickListener for view*/
        editSearchFlatNumber.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Emergency.this, SearchFlatNumber.class);
        startActivityForResult(intent, SEARCH_FLAT_NUMBER_REQUEST_CODE);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: Card View corresponding to the Flat Number will be displayed here. To be done once Emergency is implemented in the NammaApartments User app.
    }
}

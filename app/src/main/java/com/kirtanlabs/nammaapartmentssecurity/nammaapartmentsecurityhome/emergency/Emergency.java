package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;

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
        RecyclerView recyclerViewEmergency = findViewById(R.id.recyclerViewEmergency);
        recyclerViewEmergency.setHasFixedSize(true);
        recyclerViewEmergency.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        EmergencyAdapter adapter = new EmergencyAdapter(this);

        //Setting adapter to recycler view
        recyclerViewEmergency.setAdapter(adapter);

        /*Since we have filter button here, we would want to perform navigate user to Filter Emergency List
         * and display data*/
        ImageView filterButton = findViewById(R.id.filterButton);
        filterButton.setVisibility(View.VISIBLE);
        filterButton.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        startActivity(new Intent(Emergency.this, FilterEmergencyList.class));
    }
}

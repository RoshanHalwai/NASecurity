package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class GateNotificationHome extends BaseActivity implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_gate_notification_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.gate_notification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting id for list view*/
        ListView listViewGateNotificationServices = findViewById(R.id.listViewGateNotificationServices);

        /*Setting the Adapter to list view*/
        listViewGateNotificationServices.setAdapter(getAdapter());

        /*Setting event for list view items*/
        listViewGateNotificationServices.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intentCabArrivals = new Intent(GateNotificationHome.this, Expected_Arrivals.class);
                intentCabArrivals.putExtra(Constants.ARRIVAL_TYPE, R.string.expected_cab_arrivals);
                startActivity(intentCabArrivals);
                break;
            case 1:
                Intent intentPackageArrivals = new Intent(GateNotificationHome.this, Expected_Arrivals.class);
                intentPackageArrivals.putExtra(Constants.ARRIVAL_TYPE, R.string.expected_package_arrivals);
                startActivity(intentPackageArrivals);
                break;
            case 2:
                Intent intentThingsGivenToGuest = new Intent(GateNotificationHome.this, Things_Given.class);
                intentThingsGivenToGuest.putExtra(Constants.GIVEN_THINGS_TO, R.string.things_given_to_guest);
                startActivity(intentThingsGivenToGuest);
                break;
            case 3:
                Intent intentThingsGivenToDailyService = new Intent(GateNotificationHome.this, Things_Given.class);
                intentThingsGivenToDailyService.putExtra(Constants.GIVEN_THINGS_TO, R.string.things_given_to_daily_services);
                startActivity(intentThingsGivenToDailyService);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private GateNotificationHomeAdapter getAdapter() {
        int[] imageGateNotificationServices = {
                R.drawable.taxi,
                R.drawable.delivery_man,
                R.drawable.gift,
                R.drawable.delivery
        };

        String[] stringGateNotificationServices = {getString(R.string.expected_cab_arrivals),
                getString(R.string.expected_package_arrivals),
                getString(R.string.things_given_to_guest),
                getString(R.string.things_given_to_daily_services)
        };
        return new GateNotificationHomeAdapter(this, imageGateNotificationServices, stringGateNotificationServices);
    }
}

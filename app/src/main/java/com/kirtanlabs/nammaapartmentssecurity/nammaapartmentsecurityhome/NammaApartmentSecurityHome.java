package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class NammaApartmentSecurityHome extends BaseActivity implements AdapterView.OnItemClickListener {
    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_namma_apartment_security_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Namma Apartments Security Home Screen we wouldn't want the users to go back,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting for grid view*/
        GridView gridNammaApartmentsSecurity = findViewById(R.id.gridNammaApartmentSecurity);

        // Setting the imageAdapter
        gridNammaApartmentsSecurity.setAdapter(getAdapter());
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private NammaApartmentSecurityHomeAdapter getAdapter() {
        int[] imageGuardServices = {
                R.drawable.e_intercom,
                R.drawable.otp_valdation,
                R.drawable.daily_service,
                R.drawable.notify_gate,
                R.drawable.society_member,
                R.drawable.emergency
        };

        String[] stringGuardServices = {getString(R.string.e_intercom),
                getString(R.string.otp_validation),
                getString(R.string.daily_services),
                getString(R.string.notify_gate),
                getString(R.string.society_member),
                getString(R.string.emergency)
        };
        return new NammaApartmentSecurityHomeAdapter(this, imageGuardServices, stringGuardServices);
    }
}

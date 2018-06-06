package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification.GateNotificationHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation.VisitorsAndDailyServicesValidation;

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

        /*Setting event for grid view items*/
        gridNammaApartmentsSecurity.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(NammaApartmentSecurityHome.this, EIntercom.class));
                break;
            case 1:
                Intent intentVisitorsValidation = new Intent(NammaApartmentSecurityHome.this, VisitorsAndDailyServicesValidation.class);
                intentVisitorsValidation.putExtra(Constants.SCREEN_TITLE, R.string.visitors_validation);
                startActivity(intentVisitorsValidation);
                break;
            case 2:
                Intent intentDailyServicesValidation = new Intent(NammaApartmentSecurityHome.this, VisitorsAndDailyServicesValidation.class);
                intentDailyServicesValidation.putExtra(Constants.SCREEN_TITLE, R.string.daily_services_validation);
                startActivity(intentDailyServicesValidation);
                break;
            case 3:
                startActivity(new Intent(NammaApartmentSecurityHome.this, GateNotificationHome.class));
                break;
            default:
                Toast.makeText(this, "Yet to Implement", Toast.LENGTH_SHORT).show();
        }
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
                getString(R.string.visitors_validation),
                getString(R.string.daily_services_validation),
                getString(R.string.gate_notification),
                getString(R.string.society_member),
                getString(R.string.emergency)
        };
        return new NammaApartmentSecurityHomeAdapter(this, imageGuardServices, stringGuardServices);
    }
}

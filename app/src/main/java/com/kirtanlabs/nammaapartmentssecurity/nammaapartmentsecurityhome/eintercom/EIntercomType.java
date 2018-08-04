package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.ArrayList;
import java.util.List;

public class EIntercomType extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_eintercom_type;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.e_intercom_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listViewNotifyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //We need an Image and Text, hence we use the class used for Services List
        List<Type> notificationServicesList = new ArrayList<>();

        notificationServicesList.add(new Type(R.drawable.team, getString(R.string.guest)));
        notificationServicesList.add(new Type(R.drawable.waiter, getString(R.string.daily_service)));
        notificationServicesList.add(new Type(R.drawable.taxi, getString(R.string.cab)));
        notificationServicesList.add(new Type(R.drawable.delivery_man, getString(R.string.package_vendor)));
        notificationServicesList.add(new Type(R.drawable.family, getString(R.string.family_member)));

        /*Creating the Adapter*/
        EIntercomTypeAdapter adapter = new EIntercomTypeAdapter(this, notificationServicesList);

        /*Attaching adapter to the listview */
        recyclerView.setAdapter(adapter);
    }

}

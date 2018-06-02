package com.kirtanlabs.nammaapartmentssecurity;


import android.os.Bundle;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideBackButton();
    }
}

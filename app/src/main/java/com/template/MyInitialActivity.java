package com.template;

import android.os.Bundle;

public class MyInitialActivity extends SmartActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_initial);

        startActivity(SplashActivity.class);
    }
}
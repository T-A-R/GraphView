package com.template;

import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends SmartActivity {

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = findViewById(R.id.progress_bar);

        showLoader();
    }

    private void showLoader() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                progress.setProgress(i);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startActivity(MainActivity.class);
        }).start();
    }
}
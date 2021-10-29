package com.template;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class SmartActivity  extends AppCompatActivity {

    public void startActivity(Class newClass) {
        Intent intent = new Intent(this, newClass);
        startActivity(intent);
        finish();
    }
}

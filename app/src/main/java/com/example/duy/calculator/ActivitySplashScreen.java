package com.example.duy.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;


public class ActivitySplashScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(ActivitySplashScreen.this, BasicCalculatorActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}

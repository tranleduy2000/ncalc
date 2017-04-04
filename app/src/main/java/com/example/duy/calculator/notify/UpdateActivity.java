package com.example.duy.calculator.notify;

import android.os.Bundle;
import android.os.Handler;

import com.example.duy.calculator.version_old.activities.abstract_class.AbstractAppCompatActivity;

public class UpdateActivity extends AbstractAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                rateApp();
            }

        }, 300);
    }
}

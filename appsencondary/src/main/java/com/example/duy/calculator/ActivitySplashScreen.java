/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.example.duy.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;


public class ActivitySplashScreen extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startMainCalc();
    }

    private void startMainCalc() {
        Intent intent = new Intent(ActivitySplashScreen.this, BasicCalculatorActivity.class);
        startActivity(intent);
    }

}

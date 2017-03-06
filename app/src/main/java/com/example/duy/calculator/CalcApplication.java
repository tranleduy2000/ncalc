package com.example.duy.calculator;

import android.support.multidex.MultiDexApplication;

import com.example.duy.calculator.notify.RateManager;
import com.google.firebase.FirebaseApp;


public class CalcApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //init dialog rate
        RateManager.init(this);
        FirebaseApp.initializeApp(this);
    }
}
package com.example.duy.calculator;

import android.support.multidex.MultiDexApplication;

import com.example.duy.calculator.userinterface.FontManager;
import com.example.duy.calculator.voice.MathVoiceManager;


public class CalcApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //init font from asset file
        FontManager.getInstance(this);
        MathVoiceManager.initVoice(this, getPackageName());
    }

}
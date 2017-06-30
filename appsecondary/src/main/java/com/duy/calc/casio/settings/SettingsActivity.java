/*
 * Copyright (c) 2017 by Tran Le Duy
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duy.calc.casio.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.duy.calc.casio.activities.AbstractActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SettingsActivity extends AbstractActivity {

    public static final int MIN_DIGITS = 6, MAX_DIGITS = 12;
    public static final Integer[] FONT_SIZES = {42, 48, 64, 72, 84, 96, 108, 120};

    private CalculatorSetting mSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Reads mPreferences
        mSetting = new CalculatorSetting(this);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.settings);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setupDecimalSpinner();
        setupFontSizeSpinner();
        setupSwitches();
        setupAngleMode();
        setupCalculateMode();
    }

    private void setupAngleMode() {
        RadioButton modeRad = (RadioButton) findViewById(R.id.rad_radian);
        RadioButton modeDeg = (RadioButton) findViewById(R.id.rad_deg);
        modeDeg.setChecked(!mSetting.useRadian());
        modeRad.setChecked(mSetting.useRadian());

        modeRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSetting.setAngleMode(isChecked);
            }
        });
    }

    private void setupCalculateMode() {
        RadioButton modeFraction = (RadioButton) findViewById(R.id.rad_fraction);
        RadioButton modeDecimal = (RadioButton) findViewById(R.id.rad_decimal);
        modeFraction.setChecked(mSetting.useFraction());
        modeDecimal.setChecked(!mSetting.useFraction());
        modeFraction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSetting.setFraction(isChecked);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    /**
     * Sets up the precision spinner.
     */
    public void setupDecimalSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.decimal_spinner);
        List<Integer> list = new ArrayList<>();
        for (int i = MIN_DIGITS; i <= MAX_DIGITS; i++) {
            list.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                int roundTo = position + MIN_DIGITS;
                mSetting.setRoundTo(roundTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        spinner.setSelection(mSetting.getRoundTo() - MIN_DIGITS);
    }

    /**
     * Sets up the precision spinner.
     */
    public void setupFontSizeSpinner() {
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.font_spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, FONT_SIZES);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                int fontSize = FONT_SIZES[position];
                mSetting.setFontSize(fontSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setSelection(Arrays.binarySearch(FONT_SIZES, mSetting.getFontSize()));
    }

    /**
     * Sets up the switches.
     */
    public void setupSwitches() {

        CheckBox ckbVibrate = (CheckBox) findViewById(R.id.ckb_vibrate);
        CheckBox ckbInstantResult = (CheckBox) findViewById(R.id.ckb_instant_result);
        CheckBox ckbShowStatusBar = (CheckBox) findViewById(R.id.ckb_show_statusbar);

        boolean feedbackOn = mSetting.hasVibrate();
        boolean instantResult = mSetting.isAutoCalculate();
        boolean showStatusBar = mSetting.isShowStatusBar();

        ckbVibrate.setChecked(feedbackOn);
        ckbVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSetting.setVibrate(isChecked);
            }
        });
        ckbInstantResult.setChecked(instantResult);
        ckbInstantResult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSetting.setAutoCalc(isChecked);
            }
        });
        ckbShowStatusBar.setChecked(showStatusBar);
        ckbShowStatusBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSetting.setShowStatusBar(isChecked);
            }
        });
    }


}


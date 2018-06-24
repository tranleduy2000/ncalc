/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractEvaluatorActivity;
import com.duy.calculator.calc.BasicCalculatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.model.DerivativeItem;
import com.duy.calculator.utils.ConfigApp;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import static com.duy.calculator.R.string.derivative;


/**
 * Đạo hàm cấp n
 * Created by Duy on 19/7/2016
 */
public class DerivativeActivity extends AbstractEvaluatorActivity {
    private static final String STARTED
            = DerivativeActivity.class.getName() + "started";
    private boolean isDataNull = true;

    private ArrayList<String> mLevel = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.derivative));
        mHint1.setHint(getString(R.string.enter_function));
        btnSolve.setText(derivative);

        //add data
        for (int i = 1; i < 21; i++) {
            mLevel.add(String.valueOf(i));
        }

        //set adapter data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DerivativeActivity.this,
                android.R.layout.simple_list_item_single_choice, mLevel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setVisibility(View.VISIBLE);
        mSpinner.setAdapter(adapter);

        getIntentData();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputFormula.setText("sqrt(x) + ln(x)");
            clickHelp();
        }
    }

    /**
     * get data from activity start it
     */
    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BasicCalculatorActivity.DATA);
        if (bundle != null) {
            String data = bundle.getString(BasicCalculatorActivity.DATA);
            if (data != null) {
                mInputFormula.setText(data);
                isDataNull = false;
                clickEvaluate();
            }
        }
    }

    @Override
    public void clickHelp() {
        Log.d(TAG, "clickHelp: ");
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_function),
                getString(R.string.input_der_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target1 = TapTarget.forView(mSpinner,
                getString(R.string.derivative_level),
                getString(R.string.der_level_desc))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);
        //if is not start
        TapTarget target = TapTarget.forView(btnSolve,
                getString(derivative),
                getString(R.string.push_der_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(DerivativeActivity.this);
        sequence.targets(target0, target1, target);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                clickEvaluate();
            }


            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                editor.putBoolean(STARTED, true);
                editor.apply();
                clickEvaluate();
            }
        });
        sequence.start();
    }

    @Override
    protected String getExpression() {
        String expr = mInputFormula.getCleanText();
        String lever = mSpinner.getSelectedItem().toString();
        DerivativeItem derivativeItem = new DerivativeItem(expr, "x", lever);
        return derivativeItem.getInput();
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(DerivativeActivity.this);
                String fraction = MathEvaluator.getInstance().derivativeFunction(input,
                        config.setEvalMode(EvaluateConfig.FRACTION));
                return Lists.newArrayList(fraction);
            }
        };
    }


}

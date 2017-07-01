/*
 * Copyright 2017 Tran Le Duy
 *
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

package com.duy.calculator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.duy.calculator.R;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.tokenizer.Tokenizer;
import com.duy.calculator.utils.ConfigApp;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;

/**
 * Created by Duy on 19/7/2016
 */
public class FactorExpressionActivity extends AbstractEvaluatorActivity {

    private static final String STARTED = FactorExpressionActivity.class.getName() + "started";
    private final static String TAG = FactorExpressionActivity.class.getSimpleName();
    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.factor));

        btnSolve.setText(R.string.factor);
        mHint1.setHint(getString(R.string.enter_expression));
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputFormula.setText("x^4 - 1");
            clickHelp();
        }
    }

    @Override
    public void clickHelp() {
        final SharedPreferences.Editor editor = preferences.edit();
        //if is not start

        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_expression),
                getString(R.string.input_analyze_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.factor_polynomial),
                getString(R.string.push_analyze_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(FactorExpressionActivity.this);
        sequence.targets(target0, target);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                clickEvaluate();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        });
        sequence.start();
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
                data = new Tokenizer().getNormalExpression(data);
                isDataNull = false;
                clickEvaluate();
            }
        }
    }


    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {

                String factorStr = "Factor(" + input + ", GaussianIntegers->True)";

                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(factorStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));

                String decimal = MathEvaluator.getInstance().evaluateWithResultAsTex(factorStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.DECIMAL));

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
    }


}

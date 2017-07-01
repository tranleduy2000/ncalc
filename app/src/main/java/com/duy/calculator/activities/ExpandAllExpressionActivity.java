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
public class ExpandAllExpressionActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = ExpandAllExpressionActivity.class.getName() + "started";
    SharedPreferences preferences;
    private boolean isDataNull = true;


    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.expand));
        mHint1.setHint(getString(R.string.enter_expression));
        btnSolve.setText(R.string.expand);
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputFormula.setText("(x + 2a)^8");
            clickHelp();
        }
    }

    @Override
    public void clickHelp() {
        //if is not start
        final SharedPreferences.Editor editor = preferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_expression),
                getString(R.string.input_expand_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.expand),
                getString(R.string.push_expand_button));
        target.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);
        TapTargetSequence sequence = new TapTargetSequence(ExpandAllExpressionActivity.this);
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

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {

                StringBuilder diffStr = new StringBuilder();
                diffStr.append("ExpandAll(").append(input).append(")");

                String fraction = MathEvaluator.getInstance().derivativeFunction(diffStr.toString(),
                        EvaluateConfig.loadFromSetting(ExpandAllExpressionActivity.this)
                                .setEvalMode(EvaluateConfig.FRACTION));

                String decimal = MathEvaluator.getInstance().derivativeFunction(diffStr.toString(),
                        EvaluateConfig.loadFromSetting(ExpandAllExpressionActivity.this)
                                .setEvalMode(EvaluateConfig.DECIMAL));

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
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

}

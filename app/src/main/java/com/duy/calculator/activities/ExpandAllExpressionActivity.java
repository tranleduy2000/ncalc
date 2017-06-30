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
import com.duy.calculator.item_math_type.ExpressionItem;
import com.duy.calculator.item_math_type.ExprInput;
import com.duy.calculator.item_math_type.ItemResult;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.LogicEvaluator;
import com.duy.calculator.tokenizer.Tokenizer;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

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
            showHelp();
        }
    }

    @Override
    public void showHelp() {
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
                doEval();
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
                data = new Tokenizer(this).getNormalExpression(data);
                isDataNull = false;
                new ExpandTask().execute(new ExpressionItem(data));
            }
        }
    }


    @Override
    public void doEval() {
        String inp = mInputFormula.getCleanText();
        if (inp.isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }
        new ExpandTask().execute(new ExpressionItem(inp));
    }

    private class ExpandTask extends ATaskEval {

        @Override
        protected ItemResult doInBackground(ExprInput... params) {
            ExprInput item = params[0];
            //check error
            if (MathEvaluator.newInstance(getApplicationContext())
                    .isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(),
                        MathEvaluator.newInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            MathEvaluator.newInstance(getApplicationContext())
                    .expandAll(item.getInput(),
                            new LogicEvaluator.EvaluateCallback() {
                                @Override
                                public void onEvaluated(String expr, String result, int errorResourceId) {
                                    res[0] = new ItemResult(expr, result, errorResourceId);
                                }

                                @Override
                                public void onCalculateError(Exception e) {

                                }
                            });
            return res[0];
        }
    }

}

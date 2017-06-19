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
import android.util.Log;

import com.duy.calculator.R;
import com.duy.calculator.item_math_type.AExprInput;
import com.duy.calculator.item_math_type.ItemResult;
import com.duy.calculator.item_math_type.SimplifyItem;
import com.duy.calculator.math_eval.LogicEvaluator;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by Duy on 19/7/2016
 */
public class SimplifyEquationActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = SimplifyEquationActivity.class.getName() + "started";
    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    /**
     * convert expression to english
     */
    @Override
    public void doEval() {
        String inp = mInputFormula.getCleanText();
        if (inp.isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }

        new TaskSimplify().execute(new SimplifyItem(inp));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.simplify_expression);
        btnSolve.setText(R.string.simplify);
        mHint1.setHint(getString(R.string.enter_expression));
        getIntentData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputFormula.setText("a - b + 2a - b");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();

        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_expression),
                getString(R.string.input_simplify_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);


        //if is not start
        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.simplify_expression),
                getString(R.string.push_simplify_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);
        TapTargetSequence sequence = new TapTargetSequence(SimplifyEquationActivity.this);
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
                doEval();
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
                isDataNull = false;
                doEval();
            }
        }
    }

    protected class TaskSimplify extends ATaskEval {

        @Override
        protected ItemResult doInBackground(AExprInput... params) {
            SimplifyItem item = (SimplifyItem) params[0];
            Log.d(TAG, "doInBackground: " + item.getInput());

            //check error
            if (mEvaluator.isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(),
                        mEvaluator.getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            mEvaluator.setFraction(false);
            mEvaluator.simplifyExpression(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluated(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }

}

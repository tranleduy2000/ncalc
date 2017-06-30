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
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.item_math_type.ExprInput;
import com.duy.calculator.item_math_type.IntegrateItem;
import com.duy.calculator.item_math_type.ItemResult;
import com.duy.calculator.evaluator.FormatExpression;
import com.duy.calculator.evaluator.LogicEvaluator;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Integrate(f(x), {x, a, b})
 * Integrate of function f(x) with variable x, with lower limit a, upper limit b
 * Created by DUy on 07-Dec-16.
 */

public class IntegrateActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = IntegrateActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.integrate));
        mHint1.setHint(getString(R.string.enter_function));
        btnSolve.setText(R.string.integrate);

        //hide and show view
        mLayoutLimit.setVisibility(View.VISIBLE);
        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputFormula.setText("sqrt(1-x^2)/x^2");
                editFrom.setText("sqrt(2)/2");
                editTo.setText("1");
            }
            showHelp();
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
                doEval();
            }
        }
    }


    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_function),
                getString(R.string.input_integrate_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target1 = TapTarget.forView(editFrom,
                getString(R.string.lower_limit),
                getString(R.string.limit_from_desc))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target2 = TapTarget.forView(editTo,
                getString(R.string.upper_limit),
                getString(R.string.limit_to_desc));
        target2.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTarget target3 = TapTarget.forView(btnSolve,
                getString(R.string.integrate),
                getString(R.string.push_integrate_button));
        target3.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTargetSequence sequence = new TapTargetSequence(IntegrateActivity.this);
        sequence.targets(target0, target1, target2, target3);
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

    //evaluate
    @Override
    public void doEval() {
        String inp = mInputFormula.getCleanText();

        //check empty input
        if (inp.isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }
        //check empty input
        String from = editFrom.getText().toString();
        if (from.isEmpty()) {
            editFrom.requestFocus();
            editFrom.setError(getString(R.string.enter_limit));
            return;
        }

        //check empty input
        String to = editTo.getText().toString();
        if (to.isEmpty()) {
            editTo.requestFocus();
            editTo.setError(getString(R.string.enter_limit));
            return;
        }

        inp = FormatExpression.cleanExpression(inp, this);

        IntegrateItem integrateItem = new IntegrateItem(inp,
                editFrom.getText().toString(),
                editTo.getText().toString());
        new ATaskEval().execute(integrateItem);
    }

    /**
     * task for evaluate anti derivative
     */
    protected class TaskIntegrate extends ATaskEval {

        @Override
        protected ItemResult doInBackground(ExprInput... aExprInputs) {
            ExprInput item = aExprInputs[0];
            //check error
            if (mEvaluator.isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), mEvaluator.getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            mEvaluator.integrateFunction(item.getInput(), new LogicEvaluator.EvaluateCallback() {
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

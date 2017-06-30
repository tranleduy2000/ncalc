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
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.item_math_type.AExprInput;
import com.duy.calculator.item_math_type.ItemResult;
import com.duy.calculator.item_math_type.SolveItem;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.LogicEvaluator;
import com.duy.calculator.evaluator.Tokenizer;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import static com.duy.calculator.R.string.solve;


public class SolveEquationActivity extends AbstractEvaluatorActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String STARTED = SolveEquationActivity.class.getName() + "started";

    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.solve_equation);
        mHint1.setHint(getString(R.string.input_equation));
        btnSolve.setText(solve);
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputFormula.setText("2x^2 + 3x + 1");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.input_equation),
                getString(R.string.input_equation_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.solve_equation),
                getString(R.string.push_solve_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(SolveEquationActivity.this);
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
                data = new Tokenizer(this).getNormalExpression(data);
                isDataNull = false;
                if (!data.isEmpty()) {
                    new TaskSolve().execute(new SolveItem(data));
                } else {
                    //
                }
            }
        }
    }


    @Override
    public int getIdStringHelp() {
        return R.string.help_solve_equation;
    }


    @Override
    public void doEval() {
        String inp = mInputFormula.getCleanText();
        if (inp.isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }

        SolveItem item = new SolveItem(inp);
        TaskSolve solve = new TaskSolve();
        solve.execute(item);
    }


    /**
     * clickSolveEquation equation with activity
     */
    public class TaskSolve extends ATaskEval {

        @Override
        protected ItemResult doInBackground(AExprInput... aExprInputs) {
            SolveItem item = (SolveItem) aExprInputs[0];
            if (ConfigApp.DEBUG) Log.d(TAG, "doInBackground: " + item.getInput());
            if (!item.getExpr().contains("x")) {
                return new ItemResult(item.toString(),
                        getString(R.string.not_variable), LogicEvaluator.RESULT_FAILED);
            }
            //check error
            if (MathEvaluator.newInstance(getApplicationContext()).isSyntaxError(item.getExpr())) {
                return new ItemResult(item.getExpr(), MathEvaluator.newInstance(getApplicationContext()).getError(item.getExpr()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            MathEvaluator.newInstance(getApplicationContext()).solveEquation(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluated(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }
}

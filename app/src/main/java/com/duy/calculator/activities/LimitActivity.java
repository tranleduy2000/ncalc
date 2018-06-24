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
import android.view.Gravity;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractEvaluatorActivity;
import com.duy.calculator.calc.BasicCalculatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.exceptions.ExpressionChecker;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.model.LimitItem;
import com.duy.calculator.utils.ConfigApp;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by Duy on 07-Dec-16.
 */

public class LimitActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = LimitActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.limit));
        mHint1.setHint(getString(R.string.enter_function));
        btnSolve.setText(R.string.eval);

        mLayoutLimit.setVisibility(View.VISIBLE);
        mHint1.setHint("");
        editFrom.post(new Runnable() {
            @Override
            public void run() {
                editFrom.setText("x → ");
                editFrom.setEnabled(false);
                editFrom.setHint(null);
                editFrom.setGravity(Gravity.END);
            }
        });

        getIntentData();
        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputFormula.setText("1/x + 2");
                editTo.setText("inf");
            }
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
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_function));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target1 = TapTarget.forView(editTo,
                getString(R.string.input_limit));
        target1.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target2 = TapTarget.forView(btnSolve,
                getString(R.string.eval));
        target2.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(LimitActivity.this);
        sequence.targets(target0, target1, target2);
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
        String limit = editTo.getText().toString();

        try {
            ExpressionChecker.checkExpression(limit);
        } catch (Exception e) {
            hideKeyboard();
            handleExceptions(editTo, e);
            return null;
        }

        LimitItem limitItem = new LimitItem(expr, limit);
        return limitItem.getInput();
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {

                EvaluateConfig config = EvaluateConfig.loadFromSetting(getApplicationContext());
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        config.setEvalMode(EvaluateConfig.FRACTION));

                return Lists.newArrayList(fraction);
            }
        };
    }
}

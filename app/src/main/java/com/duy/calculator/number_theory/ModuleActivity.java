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

package com.duy.calculator.number_theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.activities.BasicCalculatorActivity;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.item_math_type.ModuleItem;
import com.duy.calculator.utils.ConfigApp;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class ModuleActivity extends AbstractEvaluatorActivity {
    public static final String TYPE_NUMBER = "TYPE_NUMBER";
    public static final int TYPE_PERMUTATION = 0;
    public static final int TYPE_COMBINATION = 1;
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.module);
        mHint1.setHint("A = ");
        mHint2.setVisibility(View.VISIBLE);
        mHint2.setHint("B = ");
        btnSolve.setText("A mod B");

        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mInputDisplay2.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputFormula.setText("100");
                mInputDisplay2.setText("20");
            }
            clickHelp();
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BasicCalculatorActivity.DATA);
        if (bundle != null) {
            try {
                String num1 = bundle.getString("num1");
                mInputFormula.setText(num1);

                String num2 = bundle.getString("num2");
                if (num2 == null) return;
                mInputDisplay2.setText(num2);
                isDataNull = false;
                clickEvaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getIdStringHelp() {
        return R.string.nullable;
    }

    @Override
    public void clickHelp() {

    }

    @Override
    protected String getExpression() {
        String numberA = mInputFormula.getCleanText();
        if (mInputDisplay2.getCleanText().isEmpty()) {
            mInputDisplay2.requestFocus();
            mInputDisplay2.setError(getString(R.string.enter_expression));
            return null;
        }
        String numberB = mInputDisplay2.getCleanText();
        return new ModuleItem(numberA, numberB).getInput();

    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input
                );
                return Lists.newArrayList(fraction);
            }
        };
    }

}

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

import com.duy.calculator.R;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.document.DialogFragmentHelpFunction;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.item_math_type.NumberIntegerItem;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import static com.duy.calculator.number_theory.NumberType.CATALAN_FUNCTION;

/**
 * Number activity
 * Created by Duy on 15-Feb-17.
 */

public class NumberActivity extends AbstractEvaluatorActivity {
    public static final String DATA = "DATA";
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            type = intent.getIntExtra(DATA, NumberType.CATALAN);
        }
        setup();
    }

    /**
     * set up interface
     * - label of btnSolve
     * - hint of mInputFormula
     */
    private void setup() {
        btnSolve.setText(R.string.eval);
        switch (type) {
            case NumberType.CATALAN:
                mHint1.setHint(getString(R.string.catalan_desc));
                setTitle(R.string.catalan_number);
                break;
            case NumberType.FIBONACCI:
                mHint1.setHint(getString(R.string.fibo_desc));
                setTitle(R.string.fibonacci);
                break;
            case NumberType.PRIME:
                mHint1.setHint(getString(R.string.prime_desc));
                setTitle(R.string.prime);
                break;
            case NumberType.DIVISORS:
                mHint1.setHint(getString(R.string.divisors));
                setTitle(R.string.divisors);
            default:
                mHint1.setHint(getString(R.string.enter_number));
                break;
        }
    }

    @Override
    protected String getExpression() {
        String number = mInputFormula.getCleanText();
        NumberIntegerItem item = new NumberIntegerItem(number);
        switch (type) {
            case NumberType.CATALAN:
                item.setFunction(CATALAN_FUNCTION);
                break;
            case NumberType.FIBONACCI:
                item.setFunction(NumberType.FIBONACCI_FUNCTION);
                break;
            case NumberType.PRIME:
                item.setFunction(NumberType.PRIME_FUNCTION);
                break;
            case NumberType.DIVISORS:
                item.setFunction(NumberType.DIVISORS_FUNCTION);
                break;
            default:
                break;
        }
        return item.getInput();
    }

    @Override
    public int getIdStringHelp() {
        return 0;
    }

    @Override
    public void clickHelp() {
        String function;
        switch (type) {
            case NumberType.CATALAN:
                function = NumberType.CATALAN_FUNCTION;
                break;
            case NumberType.FIBONACCI:
                function = (NumberType.FIBONACCI_FUNCTION);
                break;
            case NumberType.PRIME:
                function = (NumberType.PRIME_FUNCTION);
                break;
            case NumberType.DIVISORS:
                function = (NumberType.DIVISORS_FUNCTION);
                break;
            default:
                function = "";
                break;
        }
        DialogFragmentHelpFunction dialogFragmentHelp = DialogFragmentHelpFunction.newInstance(function);
        dialogFragmentHelp.show(getSupportFragmentManager(), DialogFragmentHelpFunction.TAG);
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                String fraction = MathEvaluator.newInstance().evaluateWithResultAsTex(input,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));
                return Lists.newArrayList(fraction);
            }
        };
    }
}

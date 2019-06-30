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

package com.duy.calculator.symja.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.symja.models.NumberIntegerItem;
import com.duy.ncalc.document.MarkdownDocumentActivity;
import com.duy.ncalc.document.model.FunctionDocumentItem;
import com.gx.common.collect.Lists;

import java.util.ArrayList;

import static com.duy.calculator.symja.activities.NumberActivity.NumberType.CATALAN_FUNCTION;

/**
 * Number activity
 * Created by Duy on 15-Feb-17.
 */

public class NumberActivity extends BaseEvaluatorActivity {
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
        mBtnEvaluate.setText(R.string.eval);
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
        MarkdownDocumentActivity.open(this, new FunctionDocumentItem("doc/functions/" + function, function, ""));
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));
                return Lists.newArrayList(fraction);
            }
        };
    }

    /**
     * Created by Duy on 15-Feb-17.
     */

    public static class NumberType {
        public static final int PRIME = 1;
        public static final int PRIME_Q = 2;
        public static final int CO_PRIME_Q = 3;
        public static final int FACTOR_PRIME = 4;
        public static final int CATALAN = 5;
        public static final int FIBONACCI = 6;
        public static final int LCM = 7;
        public static final int GCD = 8;
        public static final int DIVISORS = 9;

        public static final String PRIME_FUNCTION = "Prime";
        public static final String CATALAN_FUNCTION = "CatalanNumber";
        public static final String FIBONACCI_FUNCTION = "Fibonacci";
        public static final String DIVISORS_FUNCTION = "Divisors";
    }
}

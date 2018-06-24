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

package com.duy.calculator.number;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;

import com.duy.calculator.R;
import com.duy.calculator.calc.BasicCalculatorActivity;
import com.duy.calculator.activities.base.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.utils.ConfigApp;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class FactorPrimeActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.factor_prime));

        btnSolve.setText(R.string.factor);
        mHint1.setHint(getString(R.string.input_number));
        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputFormula.setText("102013124");
            }
            clickHelp();
        }

    }

    @Override
    public void clickHelp() {

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
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                String fraction = MathEvaluator.getInstance().factorPrime(input);
                return Lists.newArrayList(fraction);
            }
        };
    }
}

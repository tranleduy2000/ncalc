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

import com.duy.calculator.R;
import com.duy.calculator.activities.BasicCalculatorActivity;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.utils.ConfigApp;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PiActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = PiActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pi_number));

        btnSolve.setText(R.string.calc);
        mHint1.setHint(getString(R.string.input_number));
        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputFormula.setText("1000");
            }
            clickHelp();
        }

    }

    @Override
    public int getIdStringHelp() {
        return R.string.nullable;
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
    protected String getExpression() {
        return "N(Pi," + mInputFormula.getCleanText() + ")";
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                String result = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        EvaluateConfig.newInstance().setEvalMode(EvaluateConfig.DECIMAL));
                return Lists.newArrayList(result);
            }
        };
    }
}

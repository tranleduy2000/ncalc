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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.activities.BasicCalculatorActivity;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.item_math_type.CombinationItem;
import com.duy.calculator.item_math_type.PermutationItem;
import com.duy.calculator.utils.ConfigApp;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PermutationActivity extends AbstractEvaluatorActivity {
    public static final String TYPE_NUMBER = "TYPE_NUMBER";
    public static final int TYPE_PERMUTATION = 0;
    public static final int TYPE_COMBINATION = 1;
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;
    private int type;
    private MathEvaluator evaluator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        int bundle = intent.getIntExtra(TYPE_NUMBER, -1);
        if (bundle == -1) {
            Toast.makeText(this, "Bundle nullable", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: bundle nullable, please input type for activity");
            finish();
            return;
        }

        if (bundle == TYPE_PERMUTATION) {
            setTitle(R.string.permutation);
        } else if (bundle == TYPE_COMBINATION) {
            setTitle(R.string.combination);
        } else {
            Toast.makeText(this, "Can not init activity", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: bundle nullable, please input type for activity");
            finish();
            return;
        }
        evaluator = MathEvaluator.getInstance();
        btnSolve.setText(R.string.eval);

        mHint2.setVisibility(View.VISIBLE);
        mHint1.setHint(Constants.C + " = ");
        mHint2.setHint(Constants.K + " = ");

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
        return R.string.help_permutation;
    }

    @Override
    public void clickHelp() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_solve:
                clickEvaluate();
                break;
            case R.id.btn_clear:
                super.clickClear();
                break;
        }
    }

    @Override
    public void clickEvaluate() {
        super.clickEvaluate();

        String inp = mInputFormula.getCleanText();
        //check empty input
        if (inp.isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_number));
            return;
        }

        if (mInputDisplay2.getCleanText().isEmpty()) {
            mInputDisplay2.requestFocus();
            mInputDisplay2.setError(getString(R.string.enter_number));
            return;
        }

        if (type == TYPE_PERMUTATION) {
            PermutationItem item = new PermutationItem(mInputFormula.getCleanText(),
                    mInputDisplay2.getCleanText());

            //check input error
            if (item.isError(evaluator)) {
                Toast.makeText(this, item.getError(evaluator, getApplicationContext()), Toast.LENGTH_SHORT).show();
                return; // return if input error
            }
//            new TaskPermutation().execute(item); //execute task to evaluate
        } else {
            CombinationItem item = new CombinationItem(mInputFormula.getCleanText(),
                    mInputDisplay2.getCleanText());
            if (item.isError(evaluator)) {
                Toast.makeText(this, item.getError(evaluator, getApplicationContext()), Toast.LENGTH_SHORT).show();
                return;
            }
//            new TaskPermutation().execute(item);
        }
    }


    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {

                String primitiveStr = "Integrate(" + input + ",x)";
// TODO: 30-Jun-17  trig
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(primitiveStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));

                String decimal = MathEvaluator.getInstance().evaluateWithResultAsTex(primitiveStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.DECIMAL));

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
    }
}

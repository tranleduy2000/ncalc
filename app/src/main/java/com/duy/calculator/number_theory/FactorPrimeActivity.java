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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.activities.BasicCalculatorActivity;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.utils.ConfigApp;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;

/**
 * Created by DUy on 06-Jan-17.
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

       setAutoEval();
    }

    private void setAutoEval() {
        //auto eval
      /*  mInputFormula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 10
                        && editable.toString().length() >= 1) {
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex("FactorInteger(" +
                            mInputFormula.getCleanText() + ")", new LogicEvaluator.EvaluateCallback() {
                        @Override
                        public void onEvaluated(String mExpression, String mResult, int errorResourceId) {
                            if (errorResourceId == LogicEvaluator.RESULT_OK) {
//                                mMathView.setText(mResult);
                            }
                        }
                    });
                }
//                else mMathView.setText("");
            }
        });
*/
    }

    @Override
    public int getIdStringHelp() {
        return R.string.nullable;
    }

    @Override
    public void clickHelp() {
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.enter_number),
                getString(R.string.input_integrate_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);


        TapTarget target3 = TapTarget.forView(btnSolve,
                getString(R.string.factor),
                getString(R.string.push_integrate_button));
        target3.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTargetSequence sequence = new TapTargetSequence(FactorPrimeActivity.this);
        sequence.targets(target0, target3);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                clickEvaluate();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        });
        sequence.start();
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

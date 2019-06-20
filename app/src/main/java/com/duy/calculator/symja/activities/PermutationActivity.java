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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseEvaluatorActivity;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.utils.DLog;
import com.gx.common.collect.Lists;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PermutationActivity extends BaseEvaluatorActivity {
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
        type = bundle;

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
        mBtnEvaluate.setText(R.string.eval);

        mHint2.setVisibility(View.VISIBLE);
        mHint1.setHint(Constants.C + " = ");
        mHint2.setHint(Constants.K + " = ");

        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mInputFormula2.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted || DLog.UI_TESTING_MODE) && isDataNull) {
            mInputFormula.setText("100");
            mInputFormula2.setText("20");
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
                mInputFormula2.setText(num2);
                isDataNull = false;
                clickEvaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    protected String getExpression() {
        if (mInputFormula2.getCleanText().isEmpty()) {
            mInputFormula2.requestFocus();
            mInputFormula2.setError(getString(R.string.enter_number));
            return null;
        }
        return "";
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                IExpr function = F.Binomial;
                if (type == TYPE_PERMUTATION) {
                    // ( Gamma(#+1) / Gamm(#2+1) )&
                    function = F.Function( //
                              F.Divide(F.Gamma(F.Plus(F.C1,F.Slot1)),F.Gamma(F.Plus(F.C1,F.Slot2))) //
                    );
                }
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(
                            EvaluateConfig.loadFromSetting(getApplicationContext())
                                    .setEvalMode(EvaluateConfig.FRACTION), function, mInputFormula.getCleanText(), mInputFormula2.getCleanText());

                return Lists.newArrayList(fraction);
            }
        };
    }

}

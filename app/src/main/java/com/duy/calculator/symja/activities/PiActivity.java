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

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseEvaluatorActivity;
import com.duy.calculator.evaluator.LaTexFactory;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.utils.DLog;
import com.gx.common.collect.Lists;

import org.matheclipse.core.interfaces.IExpr;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PiActivity extends BaseEvaluatorActivity {
    private static final String STARTED = PiActivity.class.getName() + "started";
    private boolean isDataNull = true;
    private String precision="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pi_number));

        mBtnEvaluate.setText(R.string.eval);
        mHint1.setHint(getString(R.string.precision_));
        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted || DLog.UI_TESTING_MODE) && isDataNull) {
            mInputFormula.setText("1000");
        }

    }

    @Override
    public void clickHelp() {

    }

    private  void setPrecision(String precision){
        this.precision = precision;
    }

    private  String getPrecision(){
        return this.precision;
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
        setPrecision(mInputFormula.getCleanText());
        if(Integer.parseInt(getPrecision())<=16)
            return "N(Pi," + "18" + ")";
        else
        return "N(Pi," +getPrecision() + ")";
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                IExpr iExpr = MathEvaluator.getInstance().evalStr(input);
                String result = LaTexFactory.toLaTeX(iExpr);
                if(Integer.parseInt(getPrecision())<=16){
                    result = result.substring(0,Integer.parseInt(getPrecision())+3)+"$$";
                }
                return Lists.newArrayList(result);
            }
        };
    }
}

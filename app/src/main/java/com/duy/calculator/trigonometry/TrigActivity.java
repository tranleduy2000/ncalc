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

package com.duy.calculator.trigonometry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.data.SampleData;
import com.duy.calculator.item_math_type.ExprInput;
import com.duy.calculator.item_math_type.ItemResult;
import com.duy.calculator.item_math_type.TrigItem;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;

import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.REDUCE;

/**
 * Trigonometric Activity
 * Created by DUy on 31-Jan-17.
 */

public class TrigActivity extends AbstractEvaluatorActivity {
    public static final String TYPE = "TrigActivity";
    public static final String TAG = "TrigActivity";
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mType = intent.getIntExtra(TYPE, -1);
        if (mType == -1) {
            Toast.makeText(this, "Bundle nullable!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        init();
    }

    /**
     * - set title for activity
     * - Set text for button solve
     * - if this is first start activity or debug enable, give example data
     */
    private void init() {
        btnSolve.setText(R.string.eval);
        boolean started;
        switch (mType) {
            case EXPAND:
                setTitle(R.string.tit_trig_expand);
                started = mPreferences.getBoolean(TAG + "expand", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputFormula.setText(SampleData.TRIG_EXPAND_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "expand", true).apply();
                }
                break;
            case REDUCE:
                setTitle(R.string.tit_trig_reduce);
                started = mPreferences.getBoolean(TAG + "reduce", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputFormula.setText(SampleData.TRIG_REDUCE_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "reduce", true).apply();
                }
                break;
            case EXPONENT:
                setTitle(R.string.tit_trig_to_exp);
                started = mPreferences.getBoolean(TAG + "exponent", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputFormula.setText(SampleData.TRIG_EXPONENT_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "exponent", true).apply();
                }
                break;

        }
    }

    @Override
    public void doEval() {
        if (mInputFormula.getText().toString().isEmpty()) {
//            Drawable drawable = getApplicationContext().getResources().getDrawable(
//                    R.drawable.ic_mode_edit_white_24dp);
//            mInputFormula.setError(getString(R.string.enter_expression), drawable);
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }
        Log.d(TAG, "doEval: ");
        TrigItem item = new TrigItem(mInputFormula.getCleanText());
        switch (mType) {
            case EXPAND:
                item.setType(EXPAND);
                break;
            case REDUCE:
                item.setType(REDUCE);
                break;
            case EXPONENT:
                item.setType(EXPONENT);
                break;
        }
        new TrigTask().execute(item);
    }


    @Override
    public int getIdStringHelp() {
        return 0;
    }

    @Override
    public void showHelp() {

    }

    /**
     * class for trig evaluate
     */
    private class TrigTask extends ATaskEval {
        @Override
        protected ItemResult doInBackground(ExprInput... params) {
            Log.d(TAG, "doInBackground: trig task");
            MathEvaluator.newInstance(getApplicationContext()).setFraction(true);
            return super.doInBackground(params);
        }
    }
}

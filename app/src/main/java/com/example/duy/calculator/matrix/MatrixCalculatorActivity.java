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

package com.example.duy.calculator.matrix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.example.duy.calculator.R;
import com.example.duy.calculator.activities.abstract_class.AbstractNavDrawerActionBarActivity;

public class MatrixCalculatorActivity extends AbstractNavDrawerActionBarActivity
        implements FragmentMatrixInput.OnMatrixEvalListener {
    private static final String TAG = MatrixCalculatorActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_calculator);

        //show fragment input by add FragmentMatrixInput into screen
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new FragmentMatrixInput());
        fragmentTransaction.commit();
    }

    @Override
    public void doCalculate(String matrix1, String matrix2, MatrixOpt opt) {

    }

    @Override
    public void doCalculate(final String expression) {
        FragmentStepEval fragmentMatrixEval = FragmentStepEval.newInstance(expression);
        fragmentMatrixEval.show(getSupportFragmentManager(), FragmentStepEval.TAG);
    }


    /*            result + "], resultId = [" + resultId + "]"
        if (resultId == LogicEvaluator.RESULT_ERROR) {

            //display the error by set an item error for recycleview adapter
            resultAdapter.setResult(result);
            resultAdapter.notifyDataSetChanged();

        } else if (resultId == LogicEvaluator.RESULT_OK) {
            ArrayList<String> entries = new ArrayList<>();
            resultAdapter.setResults(entries);
            resultAdapter.notifyDataSetChanged();

        } else if (resultId == LogicEvaluator.INPUT_EMPTY) {
            resultAdapter.clear();
            resultAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Please enter your matrix", Toast.LENGTH_SHORT).show();
        }*/

}

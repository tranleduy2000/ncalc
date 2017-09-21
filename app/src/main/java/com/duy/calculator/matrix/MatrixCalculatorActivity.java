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

package com.duy.calculator.matrix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractNavDrawerActionBarActivity;

public class MatrixCalculatorActivity extends AbstractNavDrawerActionBarActivity
        implements FragmentMatrixInput.OnMatrixEvalListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_calculator);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, FragmentMatrixInput.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void doCalculate(String matrix1, String matrix2, MatrixOpt opt) {

    }

    @Override
    public void doCalculate(final String expression) {
        FragmentMatrixResult fragmentMatrixEval = FragmentMatrixResult.newInstance(expression);
        fragmentMatrixEval.show(getSupportFragmentManager(), FragmentMatrixResult.TAG);
    }
}

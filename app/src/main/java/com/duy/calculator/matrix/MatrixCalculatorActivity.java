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

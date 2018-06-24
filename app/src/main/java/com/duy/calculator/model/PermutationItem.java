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

package com.duy.calculator.model;

import android.content.Context;
import android.util.Log;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.Constants;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PermutationItem extends ExprInput {
    private static final String TAG = PermutationItem.class.getSimpleName();
    protected String numberN = "0";
    protected String numberK = "0";

    public PermutationItem(String n, String k) {
        this.numberN = n;
        this.numberK = k;
    }

    public String getNumberN() {
        return numberN;
    }

    public void setNumberN(String numberN) {
        this.numberN = numberN;
    }

    public String getNumberK() {
        return numberK;
    }

    public void setNumberK(String numberK) {
        this.numberK = numberK;
    }

    @Override
    public boolean isError(MathEvaluator evaluator) {
        try {
            long a1 = Long.parseLong(numberN);
            long a2 = Long.parseLong(numberK);
            boolean b = (a1 > 0 && a2 > 0 && a2 <= a1);
            if (!b) {
                Log.e(TAG, "isError: input error");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "isError: input error");
            return true;
        }
    }

    @Override
    public String toString() {
        return Constants.P + Constants.LEFT_PAREN + this.numberN + "," + this.numberK
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getInput() {
        return Constants.P + Constants.LEFT_PAREN + this.numberN + "," + this.numberK
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getError(MathEvaluator evaluator, Context context) {
        try {
            long a1 = Long.parseLong(numberN);
            long a2 = Long.parseLong(numberK);
            if (a1 < 0 || a2 < 0) return context.getString(R.string.gt_zero);
            else return "";
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.not_a_number);
        }
    }
}

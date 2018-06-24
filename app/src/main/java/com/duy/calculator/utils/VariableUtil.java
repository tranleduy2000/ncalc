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

package com.duy.calculator.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.Constants;

import java.util.ArrayList;

/**
 * Created by Duy on 19/7/2016
 */

public class VariableUtil {

    @NonNull
    public static ArrayList<String> getListFunction(Context context) {
        ArrayList<String> mList = new ArrayList<>();
        mList.add(Character.toString(Constants.INFINITY_UNICODE));
        mList.add("x");
        mList.add("E");
        mList.add("Pi");
        mList.add(context.getString(R.string.sqrt_sym) + "(");
        mList.add("I");
        mList.add("=");
        mList.add("!");
        mList.add("Abs" + "(");
        mList.add("ArcCos" + "(");
        mList.add("ArcCot" + "(");
        mList.add("ArcSin" + "(");
        mList.add("ArcTan" + "(");
        mList.add("Binomial" + "(");
        mList.add("Cos" + "(");
        mList.add("Cot" + "(");
        mList.add("Exp" + "(");
        mList.add("Floor" + "(");
        mList.add("GCD" + "(");
        mList.add("LCM" + "(");
        mList.add("Log" + "(");
        mList.add("Ln" + "(");
        mList.add("Max" + "(");
        mList.add("Mean" + "(");
        mList.add("Min" + "(");
        mList.add("Round" + "(");
        mList.add("Sign" + "(");
        mList.add("Sin" + "(");
        mList.add("Sinh" + "(");
        mList.add("Sqrt" + "(");
        mList.add("Sum" + "(");
        mList.add("Tan" + "(");
        mList.add("Tanh" + "(");
        return mList;
    }
}


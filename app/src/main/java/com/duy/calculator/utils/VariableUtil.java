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


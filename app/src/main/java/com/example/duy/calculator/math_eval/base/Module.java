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

package com.example.duy.calculator.math_eval.base;

import com.example.duy.calculator.math_eval.Constants;

/**
 * A super class for BaseModule, GraphModule
 */
public class Module {
    // Used for formatting Dec, Bin, and Hex.
    // Dec looks like 1,234,567. Bin is 1010 1010. Hex is 0F 1F 2F.
    private static final int mDecSeparatorDistance = 3;
    private static final int mBinSeparatorDistance = 4;
    private static final int mHexSeparatorDistance = 2;
    private static final int mOctSeparatorDistance = 3;
    // Used whenever math is necessary
    private final Evaluator mEvaluator;


    public Module(Evaluator evaluator) {
        mEvaluator = evaluator;
    }

    public int getDecSeparatorDistance() {
        return mDecSeparatorDistance;
    }

    public int getBinSeparatorDistance() {
        return mBinSeparatorDistance;
    }

    public int getHexSeparatorDistance() {
        return mHexSeparatorDistance;
    }

    public int getOctSeparatorDistance() {
        return mOctSeparatorDistance;
    }

    public char getDecimalPoint() {
        return Constants.DECIMAL_POINT;
    }

    public char getDecSeparator() {
        return Constants.DECIMAL_SEPARATOR;
    }

    public char getBinSeparator() {
        return Constants.BINARY_SEPARATOR;
    }

    public char getHexSeparator() {
        return Constants.HEXADECIMAL_SEPARATOR;
    }

    public char getOctSeparator() {
        return Constants.OCTAL_SEPARATOR;
    }

    public Evaluator getSolver() {
        return mEvaluator;
    }
}

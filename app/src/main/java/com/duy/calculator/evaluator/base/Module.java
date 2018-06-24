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

package com.duy.calculator.evaluator.base;

import com.duy.calculator.evaluator.Constants;

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

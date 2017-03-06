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

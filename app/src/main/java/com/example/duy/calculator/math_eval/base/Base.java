package com.example.duy.calculator.math_eval.base;

/**
 * Represents changing the number of characters available when writing numbers.
 */
public enum Base {
    BINARY(2),
    OCTAL(8),
    DECIMAL(10),
    HEXADECIMAL(16);

    int quickSerializable;

    Base(int num) {
        this.quickSerializable = num;
    }

    public int getQuickSerializable() {
        return quickSerializable;
    }
}

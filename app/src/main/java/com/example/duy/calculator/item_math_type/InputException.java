package com.example.duy.calculator.item_math_type;

/**
 * Created by DUy on 29-Dec-16.
 */

public class InputException extends Exception {

    public static final int ERROR_INPUT_FROM = 1;
    public static final int ERROR_INPUT_TO = 2;
    public static final int ERROR_INPUT_FUNCTION = 3;
    public static final int ERROR_INPUT_EXPRESSION = 4;
    public static final int ERROR_INPUT_LIMIT = 5;
    public static final int ERROR_MATH = 6;

    private int errorId;
    private String msg;

    public InputException(int id, String e) {
        this.errorId = id;
        this.msg = e;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return this.errorId + " " + msg;
    }
}

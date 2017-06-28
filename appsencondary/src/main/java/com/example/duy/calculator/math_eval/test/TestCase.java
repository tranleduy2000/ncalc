package com.example.duy.calculator.math_eval.test;

import com.example.duy.calculator.DLog;

/**
 * Created by DUy on 20-Jan-17.
 */

public abstract class TestCase {
    public TestCase(String name) {

    }

    public void assertEquals(String input, String result) {
        DLog.d(input + " = " + result + " | " + Boolean.parseBoolean(String.valueOf(input.equalsIgnoreCase(result))));
    }

    public void assertEquals(Object input, Object result) {
        DLog.d(input.equals(result) ? "true" : "false");
    }

}

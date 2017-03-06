package com.example.duy.calculator.math_eval;

/**
 * Error when handling large numbers or small numers
 * <p>
 * Created by DUy on 24-Jan-17.
 */
public class TooBigNumberException extends Exception {

    /**
     * Constructor
     * @param isTooBig - is value too big
     */
    public TooBigNumberException(boolean isTooBig) {
        super(isTooBig ? "Number too big" : "Number to small");
    }

}

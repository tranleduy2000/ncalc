package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;

import java.io.Serializable;

/**
 * <<<<<<< HEAD
 * Abstract class for Item Input Math
 * =======
 * >>>>>>> refs/remotes/origin/master
 * Created by DUy on 29-Dec-16.
 */

public abstract class IExprInput implements Serializable {
    /**
     * check error syntax for input
     *
     * @param evaluator - Class for evaluate
     * @return - true if input error
     */
    public abstract boolean isError(BigEvaluator evaluator);

    /**
     * build and return input
     * <p>
     * such as:
     * <p>
     * Solve(2x + 1, x)
     * Int(x, x)
     * D(2x + 3)
     * Int(sqrt(x), {x, 2, 4}
     *
     * @return - input
     */
    public abstract String getInput();

    /**
     * return error of input
     *
     * @param evaluator          - BigEvaluator for evaluate input if needed
     * @param applicationContext - mContext of application for get string language
     * @return - error found
     */
    public abstract String getError(BigEvaluator evaluator, Context applicationContext);
}

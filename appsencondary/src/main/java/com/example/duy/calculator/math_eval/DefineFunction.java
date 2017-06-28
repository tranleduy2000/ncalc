package com.example.duy.calculator.math_eval;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <<<<<<< HEAD
 * Define function
 * <p>
 * =======
 * >>>>>>> refs/remotes/origin/master
 * Created by DUy on 21-Jan-17.
 */

public class DefineFunction {

    private BigEvaluator mEvaluator;

    public DefineFunction(BigEvaluator e) {
        this.mEvaluator = e;
    }

    /**
     * @param var   - name of variable
     * @param value - init value of variable
     */
    public void define(String var, double value) {
        try {
            mEvaluator.getEvalUtilities().defineVariable(var, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param var   - name of variable
     * @param value - init value of variable
     */
    public void define(String var, String value) {
        try {
            Double res = Double.valueOf(
                    String.valueOf(mEvaluator.getEvalUtils().evaluate("N(" + value + ")")));
            define(var, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get list variable in expression
     * <p>
     * input: 2x + 3a + b + 1 + i1 + i2= 2
     * output is [x, a, b, i1, i2]
     *
     * @param expr input expression
     * @return -  List
     */
    public ArrayList<String> getListVariables(String expr) {
        ArrayList<String> variables = new ArrayList<>();
        try {
            String res = mEvaluator.evaluateWithResultNormal("Variables(" + expr + ")");
            res = res.replace("{", "");
            res = res.replace("}", "");
            String tmp[] = res.split(",");
            Collections.addAll(variables, tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }
}

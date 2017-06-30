package com.example.duy.calculator.item_math_type;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;

/**
 * Created by DUy on 07-Jan-17.
 */
public class CombinationItem extends PermutationItem {

    public CombinationItem(String n, String k) {
        super(n, k);
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return super.isError(evaluator);
    }

    @Override
    public String toString() {
        return Constants.C + Constants.LEFT_PAREN + numberN + "," + numberK
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getInput() {
        return Constants.C + Constants.LEFT_PAREN + numberN + "," + numberK
                + Constants.RIGHT_PAREN;
    }
}

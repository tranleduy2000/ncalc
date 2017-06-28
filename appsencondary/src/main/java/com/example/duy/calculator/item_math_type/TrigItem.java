package com.example.duy.calculator.item_math_type;

import android.util.Log;

import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.EXPAND;
import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.EXPONENT;
import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.REDUCE;


/**
 * Trigonometric Item
 * Created by DUy on 31-Jan-17.
 */

public class TrigItem extends ExpressionItem {
    private int mType = EXPAND;

    public TrigItem(String expr) {
        super(expr);
    }

    public String getInput(int type) {
        Log.d(TAG, "getInput: " + type);
        switch (type) {
            case EXPAND:
                return "TrigExpand(" + getExpr() + ")";
            case REDUCE:
                return "TrigReduce(" + getExpr() + ")";
            case EXPONENT:
                return "TrigToExp(" + getExpr() + ")";
            default:
                return getExpr();
        }
    }

    @Override
    public String getInput() {
        return getInput(mType);
    }

    public void setType(int type) {
        this.mType = type;
    }


}

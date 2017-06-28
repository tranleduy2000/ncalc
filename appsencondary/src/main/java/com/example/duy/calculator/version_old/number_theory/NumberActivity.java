package com.example.duy.calculator.version_old.number_theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.NumberIntegerItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;


/**
 * Number activity
 * Created by Duy on 15-Feb-17.
 */

public class NumberActivity extends AbstractEvaluatorActivity {
    public static final String DATA = "DATA";
    public static final String TAG = NumberActivity.class.getSimpleName();
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            type = intent.getIntExtra(DATA, NumberType.CATALAN);
        }
        setup();
    }

    /**
     * set up interface
     * - label of btnSolve
     * - hint of mInputDisplay
     */
    private void setup() {
        btnSolve.setText(R.string.eval);
        switch (type) {
            case NumberType.CATALAN:
                mHint1.setHint(getString(R.string.catalan_desc));
                setTitle(R.string.catalan_number);
                break;
            case NumberType.FIBONACCI:
                mHint1.setHint(getString(R.string.fibo_desc));
                setTitle(R.string.fibonacci);
                break;
            case NumberType.PRIME:
                mHint1.setHint(getString(R.string.prime_desc));
                setTitle(R.string.prime);
                break;
            case NumberType.GCD:
                setTitle(R.string.gcd);
                break;
            case NumberType.LCM:
                setTitle(R.string.lcm);
                break;
            default:
                mHint1.setHint(getString(R.string.enter_number));
                break;
        }
    }

    @Override
    public void doEval() {
        if (mInputDisplay.getCleanText().isEmpty()) {
            mInputDisplay.setError(getString(R.string.please_enter_number));
            return;
        }
        String input = mInputDisplay.getCleanText();
        NumberIntegerItem item = new NumberIntegerItem(input);
        switch (type) {
            case NumberType.CATALAN:
                item.setCmd(NumberType.CATALAN_CMD);
                break;
            case NumberType.FIBONACCI:
                item.setCmd(NumberType.FIBONACCI_CMD);
                break;
            case NumberType.PRIME:
                item.setCmd(NumberType.PRIME_CMD);
                break;
            case NumberType.GCD:
                item.setCmd(NumberType.GCD_CMD);
                break;
            case NumberType.LCM:
                item.setCmd(NumberType.LCM_CMD);
                break;
            default:
                break;
        }
        //disable fraction mode
        BigEvaluator.newInstance(this).setFraction(true);
        //create new task eval and exec it with NumberIntegerItem
        new ATaskEval().execute(item);
    }

    @Override
    public int getIdStringHelp() {
        return 0;
    }

    @Override
    public void showHelp() {

    }
}

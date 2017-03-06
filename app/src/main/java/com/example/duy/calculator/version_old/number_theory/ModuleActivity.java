package com.example.duy.calculator.version_old.number_theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.ModuleItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;

/**
 * Created by DUy on 06-Jan-17.
 */

public class ModuleActivity extends AbstractEvaluatorActivity {
    public static final String TYPE_NUMBER = "TYPE_NUMBER";
    public static final int TYPE_PERMUTATION = 0;
    public static final int TYPE_COMBINATION = 1;
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.module);
        mHint1.setHint("A = ");
        mHint2.setVisibility(View.VISIBLE);
        mHint2.setHint("B = ");
        btnSolve.setText("A mod B");

        mInputDisplay.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mInputDisplay2.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputDisplay.setText("100");
                mInputDisplay2.setText("20");
            }
            showHelp();
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BasicCalculatorActivity.DATA);
        if (bundle != null) {
            try {
                String num1 = bundle.getString("num1");
                mInputDisplay.setText(num1);

                String num2 = bundle.getString("num2");
                if (num2 == null) return;
                mInputDisplay2.setText(num2);
                isDataNull = false;
                doEval();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getIdStringHelp() {
        return R.string.nullable;
    }

    @Override
    public void showHelp() {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.btn_solve:
                doEval();
                break;
            case R.id.btn_clear:
                super.onClear();
                break;
        }
    }

    @Override
    public void doEval() {

        String inp = mInputDisplay.getCleanText();
        //check empty input
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }

        if (mInputDisplay2.getCleanText().isEmpty()) {
            mInputDisplay2.requestFocus();
            mInputDisplay2.setError(getString(R.string.enter_expression));
            return;
        }
        if (type == TYPE_PERMUTATION) {
            new TaskModule().execute(new ModuleItem(mInputDisplay.getCleanText(),
                    mInputDisplay2.getCleanText()));
        } else {
            new TaskModule().execute(new ModuleItem(mInputDisplay.getCleanText(),
                    mInputDisplay2.getCleanText()));
        }
    }


    protected class TaskModule extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            ModuleItem item = (ModuleItem) params[0];
            //check error
            if (BigEvaluator.getInstance(getApplicationContext()).isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), BigEvaluator.getInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }
}

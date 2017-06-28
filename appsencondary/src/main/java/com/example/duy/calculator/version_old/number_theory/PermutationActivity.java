package com.example.duy.calculator.version_old.number_theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.CombinationItem;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.PermutationItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;

/**
 * Created by DUy on 06-Jan-17.
 */

public class PermutationActivity extends AbstractEvaluatorActivity {
    public static final String TYPE_NUMBER = "TYPE_NUMBER";
    public static final int TYPE_PERMUTATION = 0;
    public static final int TYPE_COMBINATION = 1;
    public static final String TAG = PermutationActivity.class.getSimpleName();
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        int bundle = intent.getIntExtra(TYPE_NUMBER, -1);
        if (bundle == -1) {
            Toast.makeText(this, "Bundle nullable", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: bundle nullable, please input type for activity");
            finish();
            return;
        }

        if (bundle == TYPE_PERMUTATION) {
            setTitle(R.string.permutation);
        } else if (bundle == TYPE_COMBINATION) {
            setTitle(R.string.combination);
        } else {
            Toast.makeText(this, "Can not init activity", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: bundle nullable, please input type for activity");
            finish();
            return;
        }
        btnSolve.setText(R.string.eval);

        mHint2.setVisibility(View.VISIBLE);
        mHint1.setHint(Constants.C + " = ");
        mHint2.setHint(Constants.K + " = ");

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
        return R.string.help_permutation;
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
            mInputDisplay.setError(getString(R.string.enter_number));
            return;
        }

        if (mInputDisplay2.getCleanText().isEmpty()) {
            mInputDisplay2.requestFocus();
            mInputDisplay2.setError(getString(R.string.enter_number));
            return;
        }

        if (type == TYPE_PERMUTATION) {
            PermutationItem item = new PermutationItem(mInputDisplay.getCleanText(),
                    mInputDisplay2.getCleanText());

            //check input error
            if (item.isError(BigEvaluator.newInstance(getApplicationContext()))) {
                Toast.makeText(this, item.getError(BigEvaluator.newInstance(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
                return; // return if input error
            }
            new TaskPermutation().execute(item); //execute task to evaluate
        } else {
            CombinationItem item = new CombinationItem(mInputDisplay.getCleanText(),
                    mInputDisplay2.getCleanText());
            if (item.isError(BigEvaluator.newInstance(getApplicationContext()))) {
                Toast.makeText(this, item.getError(BigEvaluator.newInstance(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
                return;
            }
            new TaskPermutation().execute(item);
        }
    }


    protected class TaskPermutation extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            PermutationItem item = (PermutationItem) params[0];
            //check error
            if (BigEvaluator.newInstance(getApplicationContext()).isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), BigEvaluator.newInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.newInstance(getApplicationContext()).evaluateWithResultAsTex(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }
}

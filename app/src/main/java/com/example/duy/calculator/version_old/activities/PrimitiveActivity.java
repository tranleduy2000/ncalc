package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.PrimitiveItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Integrate(f(x), {x, a, b})
 * Integrate of function f(x) with variable x, from a to b
 * Created by DUy on 07-Dec-16.
 */

public class PrimitiveActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = PrimitiveActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutLimit.setVisibility(View.GONE);
        setTitle(R.string.primitive);
        mHint1.setHint(getString(R.string.enter_function));
        btnSolve.setText(R.string.primitive);

        //receive data from another activity
        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputDisplay.setText("x * sin(x)");
            }
            showHelp();
        }

    }

    /**
     * get data from activity start it
     */
    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BasicCalculatorActivity.DATA);
        if (bundle != null) {
            String data = bundle.getString(BasicCalculatorActivity.DATA);
            if (data != null) {
                mInputDisplay.setText(data);
                doEval();
            }
        }
    }

    private String getExpression(String inp) {
        PrimitiveItem item = new PrimitiveItem(inp, Constants.X);
        return item.getInput();
    }


    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.enter_function),
                getString(R.string.input_primitive_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);


        TapTarget target3 = TapTarget.forView(btnSolve,
                getString(R.string.primitive),
                getString(R.string.push_button_primitive))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(PrimitiveActivity.this);
        sequence.targets(target0, target3);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                doEval();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        });
        sequence.start();
    }

    //evaluate
    @Override
    public void doEval() {
        String inp = mInputDisplay.getCleanText();

        //if input empty, do not evaluate
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }

        //crate new input
        PrimitiveItem item = new PrimitiveItem(inp, Constants.X);

        //evaluate
        TaskEvalPrimitive evalPrimitive = new TaskEvalPrimitive();
        evalPrimitive.execute(item);
    }



    /**
     * task for evaluate anti derivative
     */
    protected class TaskEvalPrimitive extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... iExprInputs) {
            PrimitiveItem item = (PrimitiveItem) iExprInputs[0];
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

package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.IntegrateItem;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.math_eval.FormatExpression;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Integrate(f(x), {x, a, b})
 * Integrate of function f(x) with variable x, with lower limit a, upper limit b
 * Created by DUy on 07-Dec-16.
 */

public class IntegrateActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = IntegrateActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.integrate));
        mHint1.setHint(getString(R.string.enter_function));
        btnSolve.setText(R.string.integrate);

        //hide and show view
        mLayoutLimit.setVisibility(View.VISIBLE);
        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputDisplay.setText("sqrt(1-x^2)/x^2");
                editFrom.setText("sqrt(2)/2");
                editTo.setText("1");
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
                isDataNull = false;
                doEval();
            }
        }
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
                getString(R.string.input_integrate_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target1 = TapTarget.forView(editFrom,
                getString(R.string.lower_limit),
                getString(R.string.limit_from_desc))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target2 = TapTarget.forView(editTo,
                getString(R.string.upper_limit),
                getString(R.string.limit_to_desc));
        target2.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTarget target3 = TapTarget.forView(btnSolve,
                getString(R.string.integrate),
                getString(R.string.push_integrate_button));
        target3.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTargetSequence sequence = new TapTargetSequence(IntegrateActivity.this);
        sequence.targets(target0, target1, target2, target3);
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

        //check empty input
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }
        //check empty input
        String from = editFrom.getText().toString();
        if (from.isEmpty()) {
            editFrom.requestFocus();
            editFrom.setError(getString(R.string.enter_limit));
            return;
        }

        //check empty input
        String to = editTo.getText().toString();
        if (to.isEmpty()) {
            editTo.requestFocus();
            editTo.setError(getString(R.string.enter_limit));
            return;
        }

        inp = FormatExpression.cleanExpression(inp, this);

        IntegrateItem integrateItem = new IntegrateItem(inp,
                editFrom.getText().toString(),
                editTo.getText().toString());
        new ATaskEval().execute(integrateItem);
    }

    /**
     * task for evaluate anti derivative
     */
    protected class TaskIntegrate extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... iExprInputs) {
            IExprInput item = iExprInputs[0];
            //check error
            if (mEvaluator.isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), mEvaluator.getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            mEvaluator.integrateFunction(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];

        }

    }

}

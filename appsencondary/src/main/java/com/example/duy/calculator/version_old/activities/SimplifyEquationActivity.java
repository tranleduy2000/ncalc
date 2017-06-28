package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.SimplifyItem;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by Duy on 19/7/2016
 */
public class SimplifyEquationActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = SimplifyEquationActivity.class.getName() + "started";
    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    /**
     * convert expression to english
     */
    @Override
    public void doEval() {
        String inp = mInputDisplay.getCleanText();
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }

        new TaskSimplify().execute(new SimplifyItem(inp));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.simplify_expression);
        btnSolve.setText(R.string.simplify);
        mHint1.setHint(getString(R.string.enter_expression));
        getIntentData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputDisplay.setText("a - b + 2a - b");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();

        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.enter_expression),
                getString(R.string.input_simplify_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);


        //if is not start
        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.simplify_expression),
                getString(R.string.push_simplify_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);
        TapTargetSequence sequence = new TapTargetSequence(SimplifyEquationActivity.this);
        sequence.targets(target0, target);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                doEval();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                doEval();
            }
        });
        sequence.start();
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

    protected class TaskSimplify extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            SimplifyItem item = (SimplifyItem) params[0];
            Log.d(TAG, "doInBackground: " + item.getInput());

            //check error
            if (mEvaluator.isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(),
                        mEvaluator.getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            mEvaluator.setFraction(false);
            mEvaluator.simplifyExpression(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }

}

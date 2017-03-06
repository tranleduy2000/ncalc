package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.ExpressionItem;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by Duy on 19/7/2016
 */
public class ExpandAllExpressionActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = ExpandAllExpressionActivity.class.getName() + "started";
    SharedPreferences preferences;
    private boolean isDataNull = true;


    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.expand));
        mHint1.setHint(getString(R.string.enter_expression));
        btnSolve.setText(R.string.expand);
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputDisplay.setText("(x + 2a)^8");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        //if is not start
        final SharedPreferences.Editor editor = preferences.edit();
        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.enter_expression),
                getString(R.string.input_expand_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.expand),
                getString(R.string.push_expand_button));
        target.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);
        TapTargetSequence sequence = new TapTargetSequence(ExpandAllExpressionActivity.this);
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
                data = new Tokenizer(this).getNormalExpression(data);
                isDataNull = false;
                new ExpandTask().execute(new ExpressionItem(data));
            }
        }
    }


    @Override
    public void doEval() {
        String inp = mInputDisplay.getCleanText();
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }
        new ExpandTask().execute(new ExpressionItem(inp));
    }

    private class ExpandTask extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            IExprInput item = params[0];
            //check error
            if (BigEvaluator.getInstance(getApplicationContext())
                    .isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(),
                        BigEvaluator.getInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.getInstance(getApplicationContext())
                    .expandAll(item.getInput(),
                            new LogicEvaluator.EvaluateCallback() {
                                @Override
                                public void onEvaluate(String expr, String result, int errorResourceId) {
                                    res[0] = new ItemResult(expr, result, errorResourceId);
                                }
                            });
            return res[0];
        }
    }

}

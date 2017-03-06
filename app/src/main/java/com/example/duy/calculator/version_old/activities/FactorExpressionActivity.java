package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.FactorExpressionItem;
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
public class FactorExpressionActivity extends AbstractEvaluatorActivity {

    private static final String STARTED = FactorExpressionActivity.class.getName() + "started";
    private final static String TAG = FactorExpressionActivity.class.getSimpleName();
    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public int getIdStringHelp() {
        return R.string.help_expression;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.factor));

        btnSolve.setText(R.string.factor);
        mHint1.setHint(getString(R.string.enter_expression));
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputDisplay.setText("x^4 - 1");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();
        //if is not start

        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.enter_expression),
                getString(R.string.input_analyze_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.factor_polynomial),
                getString(R.string.push_analyze_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(FactorExpressionActivity.this);
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
                new FactorExpressionTask().execute(new FactorExpressionItem((data)));
            }
        }
    }


    /**
     * convert expression to english
     */
    @Override
    public void doEval() {
        String inp = mInputDisplay.getCleanText();
        if (inp.isEmpty()) {
//            showDialog(getString(R.string.input_expression));
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }

        new FactorExpressionTask().execute(new FactorExpressionItem(mInputDisplay.getCleanText()));

    }


    protected class FactorExpressionTask extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... iExprInputs) {
            FactorExpressionItem item = (FactorExpressionItem) iExprInputs[0];
            //check error
            if (BigEvaluator.getInstance(getApplicationContext()).isSyntaxError(item.getExpr())) {
                return new ItemResult(item.getExpr(), BigEvaluator.getInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.getInstance(getApplicationContext())
                    .factorPolynomial(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
//            //task 2
//            try {
//                SolveItem solveItem = new SolveItem(item.getInput(), Constants.ZERO);
//                IExpr iExpr = BigEvaluator.getInstance(getApplicationContext()).getEvalUtilities().evaluate(solveItem.getInput());
//                String s1 = iExpr.toString();
//                Log.d(TAG, "doInBackground: " + s1);
//                if (s1.contains(Constants.SOLVE)) {
//                    throw new Exception("Can not find root of equation " + solveItem.toString());
//                }
//                //{{x -> 1}, {x -> 2}}
//                s1 = s1.trim();
//                s1 = s1.substring(1);
//                s1 = s1.substring(0, s1.length() - 1);
//                String[] roots = s1.split(",");
//                StringBuilder mExpression = new StringBuilder();
//                for (int i = 0; i < roots.length; i++) {
//                    String r = roots[i];
//                    r = r.trim();
//                    r = r.replace("{", "(").replace("}", ")").replace("x->", "");
//                    roots[i] = r;
//
//                    Log.d(TAG, "r = " + r);
//
//                    mExpression.append("(").append("x").append("-");
//                    mExpression.append(r).append(")");
//                    if (i == roots.length - 1) {
////                        mExpression.append("*");
//                    } else {
//                        mExpression.append("*");
//                    }
//                }
//                Log.i(TAG, "doInBackground: " + mExpression.toString());
//
//                boolean last = BigEvaluator.getInstance(getApplicationContext()).isFraction();
//                BigEvaluator.getInstance(getApplicationContext()).setFraction(true);
//                BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(mExpression.toString(), new LogicEvaluator.EvaluateCallback() {
//                    @Override
//                    public void onEvaluate(String mExpression, String mResult, int errorResourceId) {
//                        res[0] += Constants.WEB_SEPARATOR;
//                        res[0] += mResult;
//                    }
//                });
//
//                BigEvaluator.getInstance(getApplicationContext()).setFraction(false);
//                BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(mExpression.toString(), new LogicEvaluator.EvaluateCallback() {
//                    @Override
//                    public void onEvaluate(String mExpression, String mResult, int errorResourceId) {
//                        res[0] += Constants.WEB_SEPARATOR;
//                        res[0] += mResult;
//                    }
//                });
//                BigEvaluator.getInstance(getApplicationContext()).setFraction(last);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return res[0];
        }
    }
}

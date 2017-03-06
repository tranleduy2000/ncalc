package com.example.duy.calculator.version_old.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.SolveItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import static com.example.duy.calculator.R.string.solve;


public class SolveEquationActivity extends AbstractEvaluatorActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String STARTED = SolveEquationActivity.class.getName() + "started";

    SharedPreferences preferences;
    private boolean isDataNull = true;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.solve_equation);
        mHint1.setHint(getString(R.string.input_equation));
        btnSolve.setText(solve);
        getIntentData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) mInputDisplay.setText("2x^2 + 3x + 1");
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();
        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.input_equation),
                getString(R.string.input_equation_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(btnSolve,
                getString(R.string.solve_equation),
                getString(R.string.push_solve_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(SolveEquationActivity.this);
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
                data = new Tokenizer(this).getNormalExpression(data);
                isDataNull = false;
                if (!data.isEmpty()) {
                    new TaskSolve().execute(new SolveItem(data));
                } else {
                    //
                }
            }
        }
    }


    @Override
    public int getIdStringHelp() {
        return R.string.help_solve_equation;
    }


    @Override
    public void doEval() {
        String inp = mInputDisplay.getCleanText();
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }

        SolveItem item = new SolveItem(inp);
        TaskSolve solve = new TaskSolve();
        solve.execute(item);
    }


    /**
     * clickSolveEquation equation with activity
     */
    public class TaskSolve extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... iExprInputs) {
            SolveItem item = (SolveItem) iExprInputs[0];
            if (ConfigApp.DEBUG) Log.d(TAG, "doInBackground: " + item.getInput());
            if (!item.getExpr().contains("x")) {
                return new ItemResult(item.toString(),
                        getString(R.string.not_variable), LogicEvaluator.RESULT_FAILED);
            }
            //check error
            if (BigEvaluator.getInstance(getApplicationContext()).isSyntaxError(item.getExpr())) {
                return new ItemResult(item.getExpr(), BigEvaluator.getInstance(getApplicationContext()).getError(item.getExpr()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.getInstance(getApplicationContext()).solveEquation(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }

    }
}

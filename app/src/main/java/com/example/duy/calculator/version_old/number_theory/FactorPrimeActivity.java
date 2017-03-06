package com.example.duy.calculator.version_old.number_theory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.NumberIntegerItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by DUy on 06-Jan-17.
 */

public class FactorPrimeActivity extends AbstractEvaluatorActivity {
    private static final String STARTED = FactorPrimeActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.factor_prime));

        btnSolve.setText(R.string.factor);
        mHint1.setHint(getString(R.string.input_number));
        mInputDisplay.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted) || ConfigApp.DEBUG) {
            if (isDataNull) {
                mInputDisplay.setText("102013124");
            }
            showHelp();
        }

       setAutoEval();
    }

    private void setAutoEval() {
        //auto eval
      /*  mInputDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 10
                        && editable.toString().length() >= 1) {
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex("FactorInteger(" +
                            mInputDisplay.getCleanText() + ")", new LogicEvaluator.EvaluateCallback() {
                        @Override
                        public void onEvaluate(String mExpression, String mResult, int errorResourceId) {
                            if (errorResourceId == LogicEvaluator.RESULT_OK) {
//                                mMathView.setText(mResult);
                            }
                        }
                    });
                }
//                else mMathView.setText("");
            }
        });
*/
    }

    @Override
    public int getIdStringHelp() {
        return R.string.nullable;
    }

    @Override
    public void showHelp() {
        final SharedPreferences.Editor editor = mPreferences.edit();
        TapTarget target0 = TapTarget.forView(mInputDisplay,
                getString(R.string.enter_number),
                getString(R.string.input_integrate_here));
        target0.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);


        TapTarget target3 = TapTarget.forView(btnSolve,
                getString(R.string.factor),
                getString(R.string.push_integrate_button));
        target3.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTargetSequence sequence = new TapTargetSequence(FactorPrimeActivity.this);
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
    public void doEval() {
        String inp = mInputDisplay.getCleanText();

        //check empty input
        if (inp.isEmpty()) {
            mInputDisplay.requestFocus();
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }
        new FactorTask().execute(new NumberIntegerItem(mInputDisplay.getCleanText()));
    }

    protected class FactorTask extends ATaskEval {

        @Override
        protected ItemResult doInBackground(IExprInput... iExprInputs) {
            NumberIntegerItem item = (NumberIntegerItem) iExprInputs[0];
            //check error
            if (BigEvaluator.getInstance(getApplicationContext()).isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), BigEvaluator.getInstance(getApplicationContext()).getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            BigEvaluator.getInstance(getApplicationContext()).factorPrime(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }
    }

}

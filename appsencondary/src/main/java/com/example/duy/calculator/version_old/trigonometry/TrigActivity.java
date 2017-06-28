package com.example.duy.calculator.version_old.trigonometry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.duy.calculator.R;
import com.example.duy.calculator.data.SampleData;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.item_math_type.TrigItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractEvaluatorActivity;

import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.EXPAND;
import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.EXPONENT;
import static com.example.duy.calculator.version_old.trigonometry.TRIG_TYPE.REDUCE;


/**
 * Trigonometric Activity
 * Created by DUy on 31-Jan-17.
 */

public class TrigActivity extends AbstractEvaluatorActivity {
    public static final String DATA = "TrigActivity";
    public static final String TAG = "TrigActivity";
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        mType = intent.getIntExtra(DATA, -1);
        Log.d(TAG, "onCreate: " + mType);
        if (mType == -1) {
            Toast.makeText(this, "Bundle nullable!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        init();
    }

    /**
     * - set title for activity
     * - Set text for button solve
     * - if this is first start activity or debug enable, give example data
     */
    private void init() {
        btnSolve.setText(R.string.eval);
        boolean started;
        switch (mType) {
            case EXPAND:
                setTitle(R.string.tit_trig_expand);
                started = mPreferences.getBoolean(TAG + "expand", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputDisplay.setText(SampleData.TRIG_EXPAND_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "expand", true).apply();
                }
                break;
            case REDUCE:
                setTitle(R.string.tit_trig_reduce);
                started = mPreferences.getBoolean(TAG + "reduce", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputDisplay.setText(SampleData.TRIG_REDUCE_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "reduce", true).apply();
                }
                break;
            case EXPONENT:
                setTitle(R.string.tit_trig_to_exp);
                started = mPreferences.getBoolean(TAG + "exponent", false);
                if (!started || ConfigApp.DEBUG) {
                    mInputDisplay.setText(SampleData.TRIG_EXPONENT_DATA[0]);
                    mPreferences.edit().putBoolean(TAG + "exponent", true).apply();
                }
                break;

        }
    }

    @Override
    public void doEval() {
        if (mInputDisplay.getText().toString().isEmpty()) {
//            Drawable drawable = getApplicationContext().getResources().getDrawable(
//                    R.drawable.ic_mode_edit_white_24dp);
//            mInputDisplay.setError(getString(R.string.enter_expression), drawable);
            mInputDisplay.setError(getString(R.string.enter_expression));
            return;
        }
        Log.d(TAG, "doEval: ");
        TrigItem item = new TrigItem(mInputDisplay.getCleanText());
        switch (mType) {
            case EXPAND:
                item.setType(EXPAND);
                break;
            case REDUCE:
                item.setType(REDUCE);
                break;
            case EXPONENT:
                item.setType(EXPONENT);
                break;
        }
        new TrigTask().execute(item);
    }


    @Override
    public int getIdStringHelp() {
        return 0;
    }

    @Override
    public void showHelp() {

    }

    /**
     * class for trig evaluate
     */
    private class TrigTask extends ATaskEval {
        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            Log.d(TAG, "doInBackground: trig task");
            BigEvaluator.newInstance(getApplicationContext()).setFraction(true);
            return super.doInBackground(params);
        }
    }
}

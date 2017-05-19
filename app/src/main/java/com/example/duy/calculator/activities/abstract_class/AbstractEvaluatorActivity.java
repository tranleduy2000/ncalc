/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duy.calculator.activities.abstract_class;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.duy.calculator.R;
import com.example.duy.calculator.adapters.ResultAdapter;
import com.example.duy.calculator.history.ResultEntry;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.view.AnimationFinishedListener;
import com.example.duy.calculator.view.ResizingEditText;
import com.example.duy.calculator.view.RevealView;


/**
 * Abstract for eval equation, trig to exp,...
 * <p>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractEvaluatorActivity extends AbstractNavDrawerActionBarActivity
        implements View.OnClickListener {
    private final View.OnKeyListener mFormulaOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        doEval();
                    }
                    return true;
            }
            return false;
        }
    };
    protected String TAG = AbstractEvaluatorActivity.class.getName();

    protected EditText editFrom, editTo;
    protected LinearLayout mLayoutLimit;
    protected SharedPreferences mPreferences;


    protected Handler handler = new Handler();
    protected Button btnSolve;
    protected ResizingEditText mInputFormula;
    protected ViewGroup mDisplayForeground;
    protected ContentLoadingProgressBar mProgress;
    protected AppCompatSpinner mSpinner;
    protected Button btnClear;
    protected EditText editParam;
    protected ResizingEditText mInputDisplay2;
    protected TextInputLayout mHint1;
    protected TextInputLayout mHint2;
    protected RecyclerView rcResult;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);
        initView();
        createData();
    }

    private void initView() {
        btnSolve = (Button) findViewById(R.id.btn_solve);
        mInputFormula = (ResizingEditText) findViewById(R.id.edit_input);

/*
        FloatingKeyboardView mCustomKeyboard = (FloatingKeyboardView) findViewById(R.id.keyboard_view);
        mCustomKeyboard.setKeyboard(new Keyboard(this, R.xml.symbols));
        mCustomKeyboard.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mCustomKeyboard.registerEditText(mInputFormula);
        mCustomKeyboard.setAllignBottomCenter(true);
*/

        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);
        mProgress = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mSpinner = (AppCompatSpinner) findViewById(R.id.spinner);
        btnClear = (Button) findViewById(R.id.btn_clear);
        editParam = (EditText) findViewById(R.id.edit_params);
        mInputDisplay2 = (ResizingEditText) findViewById(R.id.edit_input_2);
        mHint1 = (TextInputLayout) findViewById(R.id.hint_1);
        mHint2 = (TextInputLayout) findViewById(R.id.hint_2);

        btnClear.setOnClickListener(this);
        btnSolve.setOnClickListener(this);
        mProgress.hide();
        findViewById(R.id.fab_help).setOnClickListener(this);
        editFrom = (EditText) findViewById(R.id.edit_lower);
        editTo = (EditText) findViewById(R.id.edit_upper);
        mLayoutLimit = (LinearLayout) findViewById(R.id.layout_limit);
        mLayoutLimit.setVisibility(View.GONE);
        mInputFormula.setOnKeyListener(mFormulaOnKeyListener);

        rcResult = (RecyclerView) findViewById(R.id.rc_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        rcResult.setHasFixedSize(true);
        rcResult.setLayoutManager(linearLayoutManager);
        resultAdapter = new ResultAdapter(this);
        rcResult.setAdapter(resultAdapter);
    }

    /**
     * create new object share preferences
     */
    private void createData() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    /**
     * animate ripple, only support lollipop device
     *
     * @param sourceView - parent of {@link RevealView}, if sourceView is null
     *                   it will be get with and height of RevealView
     * @param color      - color of animation
     * @param listener   - listener for end animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRipple(final ViewGroup foreground, View sourceView,
                               int color,
                               final Animator.AnimatorListener listener,
                               boolean out) {
        final RevealView revealView = new RevealView(this);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        revealView.setRevealColor(color);
        if (foreground == null) {
            Log.d(TAG, "animateRipple:  foreground null");
            return;
        }
        foreground.addView(revealView);
        final Animator revealAnimator;
        final int[] clearLocation = new int[2];
        if (sourceView != null) {
            sourceView.getLocationInWindow(clearLocation);
            clearLocation[0] += sourceView.getWidth() / 2;
            clearLocation[1] += sourceView.getHeight() / 2;
        } else {
            clearLocation[0] = foreground.getWidth() / 2;
            clearLocation[1] = foreground.getHeight() / 2;
        }
        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();
        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        if (out)
            revealAnimator = ViewAnimationUtils.createCircularReveal(revealView, revealCenterX, revealCenterY, 0.0f, revealRadius);
        else
            revealAnimator = ViewAnimationUtils.createCircularReveal(revealView, revealCenterX, revealCenterY, revealRadius, 0f);

        revealAnimator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        revealAnimator.addListener(listener);

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        alphaAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                foreground.removeView(revealView);
            }
        });

        revealAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                playAnimatior(alphaAnimator);
            }
        });
        playAnimatior(revealAnimator);
    }


    /**
     * show ripple animate when user click button eval
     */
    protected void onAnimate() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;
        animateRipple(mDisplayForeground, btnSolve, color, new AnimationFinishedListener() {

            @Override
            public void onAnimationFinished() {
            }
        }, true);
    }


    /**
     * show dialog with title and messenger
     */
    protected void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton(this.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: " + v.getId());
        switch (v.getId()) {
            case R.id.btn_clear:
                onClear();
                break;
            case R.id.btn_solve:
                doEval();
                break;
            case R.id.fab_help:
                showHelp();
                break;

        }
    }

    /**
     * clear text
     */
    public void onClear() {
        /*onAnimate();*/
        mInputFormula.setText("");
//        mMathView.setText(getString(getIdStringHelp()));

        if (editFrom.isShown() && editFrom.isEnabled()) editFrom.setText("");
        if (editTo.isShown()) editTo.setText("");
        mInputDisplay2.setText("");
    }


    /***
     * method for evalute input
     */
    public abstract void doEval();

    /**
     * insert text to display, text on recycler view
     *
     * @param text
     */
    public void insertTextDisplay(String text) {
        mInputFormula.insert(text);
    }

    /**
     * get id from resources for help string.
     *
     * @return id - int
     */
    public abstract int getIdStringHelp();

    /**
     * show target helper
     */
    public abstract void showHelp();

    protected void onChangeModeFraction() {
        doEval();
    }


    /**
     * class for eval extend AsyncTask
     * <p>
     * onPreExecute: hide keyboard, set math view empty text, show process bar
     * <p>
     * onPostExecute: hide process bar, set mResult to math view
     */
    public class ATaskEval extends AsyncTask<IExprInput, Void, ItemResult> {
        protected BigEvaluator mEvaluator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEvaluator = BigEvaluator.newInstance(getApplicationContext());
            mProgress.show();
//            mMathView.setText("");
            btnSolve.setEnabled(false);
            btnClear.setEnabled(false);
            hideKeyboard(mInputFormula);
            hideKeyboard(editFrom);
            hideKeyboard(editTo);
            resultAdapter.clear();
        }

        @Override
        protected ItemResult doInBackground(IExprInput... params) {
            IExprInput item = params[0];
            Log.d(TAG, "doInBackground: " + item.getInput());

            //check error
            if (mEvaluator.isSyntaxError(item.getInput())) {
                return new ItemResult(item.getInput(), mEvaluator.getError(item.getInput()),
                        LogicEvaluator.RESULT_ERROR);
            }

            final ItemResult[] res = new ItemResult[1];
            mEvaluator.evaluateWithResultAsTex(item.getInput(), new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluated(String expr, String result, int errorResourceId) {
                    res[0] = new ItemResult(expr, result, errorResourceId);
                }
            });
            return res[0];
        }

        @Override
        protected void onPostExecute(final ItemResult s) {
            super.onPostExecute(s);
//            mMathView.setText(s);
            Log.d(TAG, "onPostExecute: " + s.toString());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgress.hide();
                    btnSolve.setEnabled(true);
                    btnClear.setEnabled(true);

//                    resultAdapter.addItem(new HistoryEntry("$$" + s.mExpression + "$$", s.mResult));
                    resultAdapter.addItem(new ResultEntry("", s.mResult));
                    if (resultAdapter.getItemCount() > 0)
                        rcResult.scrollToPosition(0);
                }
            }, 300);
        }

    }


}

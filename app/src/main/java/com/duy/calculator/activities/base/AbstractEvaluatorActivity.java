/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.activities.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
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
import android.widget.Toast;

import com.duy.calculator.CalculatorPresenter;
import com.duy.calculator.R;
import com.duy.calculator.adapters.ResultAdapter;
import com.duy.calculator.document.fragment.DialogFragmentHelpFunction;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.exceptions.ExpressionChecker;
import com.duy.calculator.evaluator.exceptions.ParsingException;
import com.duy.calculator.evaluator.thread.BaseThread;
import com.duy.calculator.evaluator.thread.CalculateThread;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.history.ResultEntry;
import com.duy.calculator.keyboard.NaturalKeyboardAPI;
import com.duy.calculator.view.AnimationFinishedListener;
import com.duy.calculator.view.ResizingEditText;
import com.duy.calculator.view.RevealView;
import com.duy.calculator.view.editor.SuggestAdapter;

import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.util.ArrayList;


/**
 * Abstract for eval equation, trig to exp,...
 * <p>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractEvaluatorActivity extends AbstractNavDrawerActionBarActivity
        implements View.OnClickListener, SuggestAdapter.OnSuggestionListener {
    protected String TAG = AbstractEvaluatorActivity.class.getName();
    protected EditText editFrom, editTo;
    protected LinearLayout mLayoutLimit;
    protected SharedPreferences mPreferences;
    protected Handler mHandler = new Handler();
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
    protected RecyclerView mResultView;
    private ResultAdapter mResultAdapter;
    private CalculatorPresenter mPresenter;
    private final View.OnKeyListener mFormulaOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        clickEvaluate();
                    }
                    return true;
            }
            return false;
        }
    };
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);
        initView();
        createData();
    }

    /**
     * restore input
     */
    @Override
    protected void onResume() {
        super.onResume();
        String input = mCalculatorSetting.getString("input_key" + getClass().getSimpleName());
        mInputFormula.setText(input);
    }

    /**
     * save input
     */
    @Override
    protected void onPause() {
        super.onPause();
        mCalculatorSetting.put("input_key" + getClass().getSimpleName(),
                mInputFormula.getText().toString());
    }

    private void initView() {
        btnSolve = (Button) findViewById(R.id.btn_solve);
        mInputFormula = (ResizingEditText) findViewById(R.id.edit_input);
        mInputFormula.setOnHelpListener(this);

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

        mResultView = (RecyclerView) findViewById(R.id.rc_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        mResultView.setHasFixedSize(true);
        mResultView.setLayoutManager(linearLayoutManager);
        mResultAdapter = new ResultAdapter(this);
        mResultView.setAdapter(mResultAdapter);

        findViewById(R.id.img_natural_keyboard).setOnClickListener(this);
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
                playAnimation(alphaAnimator);
            }
        });
        playAnimation(revealAnimator);
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
        switch (v.getId()) {
            case R.id.btn_clear:
                clickClear();
                break;
            case R.id.btn_solve:
                clickEvaluate();
                break;
            case R.id.fab_help:
                clickHelp();
                break;
            case R.id.img_natural_keyboard:
                NaturalKeyboardAPI.getExpression(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NaturalKeyboardAPI.REQUEST_INPUT:
                if (resultCode == RESULT_OK) {
                    final String expr = NaturalKeyboardAPI.processResult(data);
                    if (expr.isEmpty()) return;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mInputFormula.hasFocus()) {
                                mInputFormula.setText(expr);
                            } else if (mInputDisplay2.hasFocus()) {
                                mInputDisplay2.setText(expr);
                            } else if (editFrom.hasFocus()) {
                                editFrom.setText(expr);
                            } else if (editTo.hasFocus()) {
                                editTo.setText(expr);
                            } else {
                                mInputFormula.setText(expr);
                            }
                        }
                    }, 100);
                }
                break;
        }
    }

    public void clickClear() {
        mInputFormula.setText("");
        editFrom.setText("");
        editTo.setText("");
        mInputDisplay2.setText("");
    }

    /**
     * Evaluate expression
     */
    @CallSuper
    public void clickEvaluate() {
        //if input empty, do not evaluate
        if (mInputFormula.getText().toString().isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }

        try {
            ExpressionChecker.checkExpression(mInputFormula.getCleanText());
        } catch (Exception e) {
            hideKeyboard();
            handleExceptions(mInputFormula, e);
            return;
        }

        String expr = getExpression();
        if (expr == null) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return;
        }
        Command<ArrayList<String>, String> command = getCommand();

        mProgress.show();
        btnSolve.setEnabled(false);
        btnClear.setEnabled(false);
        hideKeyboard();
        mResultAdapter.clear();

        CalculateThread calculateThread = new CalculateThread(mPresenter,
                EvaluateConfig.loadFromSetting(this), new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                Log.d(TAG, "onSuccess() called with: result = [" + result + "]");

                hideKeyboard();
                mProgress.hide();
                btnSolve.setEnabled(true);
                btnClear.setEnabled(true);

                for (String entry : result) {
                    mResultAdapter.addItem(new ResultEntry("", entry));
                }

                if (mResultAdapter.getItemCount() > 0) {
                    mResultView.scrollToPosition(0);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");

                handleExceptions(mInputFormula, e);
                mProgress.hide();
                btnSolve.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
        calculateThread.execute(command, expr);
    }

    protected void handleExceptions(EditText editText, Exception e) {
        if (e instanceof SyntaxError) {
            int start = Math.min(editText.length(), ((SyntaxError) e).getColumnIndex() - 1);
            int end = Math.min(editText.length(), ((SyntaxError) e).getColumnIndex());
            editText.setSelection(start, end);
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("SYNTAX ERROR", e.getMessage()));
        } else if (e instanceof MathException) {
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("MATH ERROR", e.getMessage()));
        } else if (e instanceof ParsingException) {
            int start = Math.min(editText.length(), ((ParsingException) e).getIndex());
            int end = Math.min(editText.length(), ((ParsingException) e).getIndex() + 1);
            editText.setSelection(start, end);
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("SYNTAX ERROR", e.getMessage()));
        } else {
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("Unknown error", e.getMessage()));
        }
        editText.setError("Error!");
    }

    protected String getExpression() {
        return mInputFormula.getCleanText();
    }

    /**
     * show target helper
     */
    public abstract void clickHelp();

    protected void onChangeModeFraction() {
        clickEvaluate();
    }

    @Override
    public void onShowInfo(String key) {
        DialogFragmentHelpFunction dialogFragmentHelp = DialogFragmentHelpFunction.newInstance(key);
        dialogFragmentHelp.show(getSupportFragmentManager(), DialogFragmentHelpFunction.TAG);
    }

    @Nullable
    public abstract Command<ArrayList<String>, String> getCommand();

    public void showDialogInstallNaturalKeyboard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.install_msg);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gotoPlayStore("com.duy.calc.casio");
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }
}

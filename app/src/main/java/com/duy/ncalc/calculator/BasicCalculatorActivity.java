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

package com.duy.ncalc.calculator;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractCalculatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.base.Evaluator;
import com.duy.calculator.evaluator.base.LogicEvaluator;
import com.duy.calculator.evaluator.thread.ResultCallback;
import com.duy.calculator.history.HistoryActivity;
import com.duy.calculator.history.ResultEntry;
import com.duy.calculator.symja.models.DerivativeItem;
import com.duy.calculator.symja.models.ExprInput;
import com.duy.calculator.symja.models.PrimeFactorItem;
import com.duy.calculator.symja.models.SolveItem;
import com.duy.ncalc.settings.CalculatorSetting;
import com.duy.ncalc.userinterface.ThemeManager;
import com.duy.ncalc.utils.ClipboardManager;
import com.duy.ncalc.utils.DLog;
import com.duy.ncalc.view.AnimationFinishedListener;
import com.duy.ncalc.view.CalculatorEditText;
import com.duy.ncalc.view.RevealView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.gx.common.collect.Lists;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;


public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements LogicEvaluator.EvaluateCallback, KeyboardListener, View.OnClickListener {
    public static final String TAG = BasicCalculatorActivity.class.getSimpleName();
    public static final String DATA = "DATA_BUNDLE";
    private static final int REQ_CODE_HISTORY = 1111;
    private static final int REQ_CODE_DEFINE_VAR = 1234;

    /**
     * Evaluate when text change
     */
    private final InstantResultWatcher mFormulaTextWatcher = new InstantResultWatcher();
    private final Handler mHandler = new Handler();
    public MathView mSecondaryResultView;
    public ContentLoadingProgressBar mProgress;
    public FrameLayout mSolveAnimView;
    private SwitchCompat mFractionSwitch;
    private FrameLayout mContainerSolve;
    private DrawerLayout mDrawerLayout;
    private CalculatorEditText mInputDisplay;
    private ViewGroup mDisplayForeground;
    private MathView mCalculatorResultView;
    private FloatingActionButton mFabClose;
    private View mCurrentButton = null;
    private CalculatorState mCurrentState = CalculatorState.INPUT;
    private MathEvaluator mEvaluator;
    private final View.OnKeyListener mFormulaOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        onEqual();
                    }
                    return true;
            }
            return false;
        }
    };
    private CalculateState mCalculateState = CalculateState.INPUT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvaluator = MathEvaluator.getInstance();
        setContentView(R.layout.activity_basic_calculator);
        bindView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInputDisplay.setShowSoftInputOnFocus(false);
        }
        mInputDisplay.addTextChangedListener(mFormulaTextWatcher);
        mInputDisplay.setOnKeyListener(mFormulaOnKeyListener);
        mInputDisplay.setAutoSuggestEnable(false);

        setInputState(CalculateState.INPUT);
        setState(CalculatorState.INPUT);
        initKeyboard();

        findViewById(R.id.img_history).setOnClickListener(this);
        mFabClose.setOnClickListener(this);

        setModeFraction();
        showHelp();
    }

    public void insertText(String text) {
        //set text display is null if not as operator
        boolean b = mInputDisplay.getSelectionStart() == mInputDisplay.getCleanText().length() + 1;
        if (mCurrentState == CalculatorState.RESULT && !Evaluator.isOperator(text) && b) {
            mInputDisplay.clear();
        }
        mInputDisplay.insert(text);
    }

    /**
     * insert text to display
     *
     * @param opt - operator
     */
    public void insertOperator(String opt) {
        if (mCurrentState == CalculatorState.INPUT) {
            //do something
        } else if (mCurrentState == CalculatorState.RESULT) {
            //do some thing
        }
        insertText(opt);
    }

    public String getTextClean() {
        return mInputDisplay.getCleanText();
    }

    @Override
    public void setTextDisplay(final String textDisplay) {
        mInputDisplay.post(new Runnable() {
            @Override
            public void run() {
                mInputDisplay.setText(mTokenizer.getLocalizedExpression(textDisplay));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_close:
                closeMathView();
                break;
            case R.id.img_history:
                Intent intent = new Intent(BasicCalculatorActivity.this, HistoryActivity.class);
                startActivityForResult(intent, REQ_CODE_HISTORY);
                break;
        }
    }

    private void bindView() {
        mFabClose = findViewById(R.id.fab_close);

        mCalculatorResultView = findViewById(R.id.math_view);
        mCalculatorResultView.setDarkTextColor(ThemeManager.isLightTheme(this));

        mDisplayForeground = findViewById(R.id.the_clear_animation);
        mInputDisplay = findViewById(R.id.txtDisplay);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mContainerSolve = findViewById(R.id.container_solve);
        mFractionSwitch = findViewById(R.id.sw_fraction);
        mSolveAnimView = findViewById(R.id.result_animation);
        mProgress = findViewById(R.id.progress_bar_main);

        mSecondaryResultView = findViewById(R.id.math_result);
//        mResultView.setDarkTextColor(ThemeManager.isLightTheme(this));

        mFractionSwitch.setChecked(mCalculatorSetting.useFraction());
        mFractionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCalculatorSetting.setFraction(isChecked);
                onChangeModeFraction();
            }
        });
    }

    private void setModeFraction() {
        mFractionSwitch.setChecked(mSetting.useFraction());
        mFractionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != mSetting.useFraction()) {
                    mSetting.setFraction(isChecked);
                }
                onChangeModeFraction();
            }
        });
        mFractionSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BasicCalculatorActivity.this, R.string.fraction_decs, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void initKeyboard() {
        KeyboardFragment keyboardFragment = KeyboardFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_keyboard, keyboardFragment, KeyboardFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void showHelp() {
        if (mCalculatorSetting.getBoolean(BasicCalculatorActivity.class.getSimpleName())) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TapTarget target = TapTarget.forView(mFractionSwitch,
                        getString(R.string.fraction_mode),
                        getString(R.string.fraction_decs))
                        .drawShadow(true)
                        .cancelable(true)
                        .targetCircleColor(R.color.colorAccent)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.colorPrimary)
                        .dimColor(R.color.colorPrimaryDark).targetRadius(70);
                TapTargetSequence sequence = new TapTargetSequence(BasicCalculatorActivity.this);
                sequence.targets(target)
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                mSetting.put(BasicCalculatorActivity.class.getSimpleName(), true);
                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {
                                mSetting.put(BasicCalculatorActivity.class.getSimpleName(), true);
                            }
                        }).start();
            }
        }, 1000);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);
        if (s.equals(getString(R.string.key_pref_fraction))) {
            setModeFraction();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_HISTORY:
                Log.d(TAG, "onActivityResult: history");
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getBundleExtra(DATA);
                    final ResultEntry history = (ResultEntry) bundle.getSerializable(DATA);
                    Log.d(TAG, "onActivityResult: " + history.getExpression() + " " + history.getResult());
                    mInputDisplay.post(new Runnable() {
                        @Override
                        public void run() {
                            mInputDisplay.setText(history.getExpression());
                        }
                    });
                }
                break;
            case REQ_CODE_DEFINE_VAR:
                mEvaluator.evaluateWithResultNormal(mInputDisplay.getCleanText(), this,
                        EvaluateConfig.loadFromSetting(this));
                //onEqual();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CalculatorSetting preferences = new CalculatorSetting(this);
        String math = mInputDisplay.getCleanText();
        preferences.put(CalculatorSetting.INPUT_MATH, math);
        mCalculatorResultView.setText("");
    }

    /**
     * set input state
     *
     * @param pad - enum {@link CalculateState}
     */
    private void setInputState(CalculateState pad) {
        mCalculateState = pad;
    }

    public void define(String var, double value) {
        mEvaluator.define(var, value);
    }

    public void define(String var, String value) {
        mEvaluator.define(var, value);
    }

    public void setTextResult(String result) {
        mCalculatorResultView.setText(result);
    }

    public void setTextError(String msg) {
        mCalculatorResultView.setText(msg);
    }

    public void onResult(final String result) {
        setTextDisplay(result.replace("\\", "").replace("\n", ""));
        setTextResult("");
    }

    public void onError(final String error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorResultError, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(CalculatorState.ERROR);
                    setTextError(error);
                }
            }, true);
        } else {
            setState(CalculatorState.ERROR);
            setTextError(error);
        }
    }

    public void onDelete() {
        mInputDisplay.backspace();
    }

    public void clickClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(CalculatorState.INPUT);
                    mInputDisplay.clear();
                    mCalculatorResultView.setText("");
                }
            }, true);
        } else {
            setState(CalculatorState.INPUT);
            mCalculatorResultView.setText("");
            mInputDisplay.clear();
        }
    }

    public void onEqual() {
        String text = mInputDisplay.getCleanText();
        setState(CalculatorState.EVALUATE);
        mEvaluator.evaluateWithResultNormal(text, BasicCalculatorActivity.this,
                EvaluateConfig.loadFromSetting(this));
    }

    /**
     * only show animate ripple on device lollipop
     *
     * @param sourceView
     * @param color
     * @param listener
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRipple(final ViewGroup foreground,
                               @Nullable View sourceView,
                               int color, final Animator.AnimatorListener listener, final boolean out) {
        if (color == -1) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            color = typedValue.data;
        } else if (color == -2) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
        }
        //create new reveal view
        final RevealView revealView = new RevealView(this);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        revealView.setRevealColor(color);
        //add to viewgroup
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
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, 0.0f, revealRadius);
        else
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, revealRadius, 0f);

        revealAnimator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        if (listener != null) revealAnimator.addListener(listener);
        revealAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                foreground.removeView(revealView);
            }
        });
        playAnimation(revealAnimator);
    }

    void setState(CalculatorState state) {
        mCurrentState = state;
    }

    @Override
    public void onEvaluated(String expr, String result, int resultId) {
        if (resultId == LogicEvaluator.RESULT_OK) {
            if (mCurrentState == CalculatorState.EVALUATE) {
                onResult(result);
                saveHistory(expr, result, true);
                mEvaluator.define("ans", result);
            } else if (mCurrentState == CalculatorState.INPUT) {
                if (result == null) {
                    setTextResult("");
                } else {
                    setTextResult(result);
                }
            }
        }
    }

    @Override
    public void onCalculateError(Exception e) {
        if (mCurrentState == CalculatorState.INPUT) {
            setTextResult(""); //clear
        } else if (mCurrentState == CalculatorState.EVALUATE) {
            onError(getResources().getString(R.string.error) + " " + e.getMessage());
        }
    }

    /**
     * error with index
     *
     * @param display
     * @param result
     */
    private void onError(final String display, final String result) {
        final int indexError = display.indexOf(LogicEvaluator.ERROR_INDEX_STRING);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorResultError, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    mInputDisplay.setText((display));
                    if (indexError >= 0) {
                        mInputDisplay.setSelection(indexError, indexError + 1);
                    }
                    setState(CalculatorState.ERROR);
                    setTextError(result);
                }
            }, true);
        } else {
            mInputDisplay.setText((display));
            if (indexError >= 0) {
                mInputDisplay.setSelection(indexError, indexError + 1);
            }
            setState(CalculatorState.ERROR);
            setTextError(result);
        }
    }

    public void copyText() {
        ClipboardManager.setClipboard(this, mInputDisplay.getCleanText());
    }

    public void pasteText() {
        mInputDisplay.setText(ClipboardManager.getClipboard(this));
    }

    protected boolean saveHistory(String expr, String result, boolean ensureResult) {
        DLog.i("Save history: " + expr + " = " + result);
        mHistoryDatabase.saveHistory(new ResultEntry(expr, result));
        return false;
    }

    protected void onChangeModeFraction() {
        Log.d(TAG, "onChangeModeFraction() called");

        mEvaluator.evaluateWithResultAsTex(mInputDisplay.getCleanText(),
                BasicCalculatorActivity.this, EvaluateConfig.loadFromSetting(BasicCalculatorActivity.this));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
                return;
            }
        }
        if (mCalculateState == CalculateState.RESULT) {
            closeMathView();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();

        CalculatorSetting preferences = new CalculatorSetting(this);
        String math = preferences.getString(CalculatorSetting.INPUT_MATH);
        mInputDisplay.setText(math);

        ///receive data from another application
        Intent intent = new Intent();
        String action = intent.getAction();
        String type = intent.getType();
        //process data
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                mInputDisplay.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }
        if (mInputDisplay != null) mInputDisplay.requestFocus();
    }

    public void clickDerivative() {
        String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
        if (inp.isEmpty()) {
            Toast.makeText(this, R.string.enter_expression, Toast.LENGTH_SHORT).show();
            return;
        }

        //check error before evaluate
        DerivativeItem item = new DerivativeItem(mInputDisplay.getCleanText(), "x", "1");
        new EvaluateTask(new ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                mSecondaryResultView.setText(result.get(0));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.hide();
                        mFabClose.show();
                        Toast.makeText(BasicCalculatorActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                }, 500);
            }

            @Override
            public void onError(Exception e) {
                onCalculateError(e);
            }
        }).execute(item);
    }

    public void clickGraph() {
//        Log.d(TAG, "clickGraph: ");
//        String function = mInputFormula.getCleanText();
//        FastGraphView graph2DView = (FastGraphView) findViewById(R.id.graph_view);
//        graph2DView.drawGraph(function);
//        mGraphView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void clickFactorPrime() {
        String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
        if (inp.isEmpty()) {
            Toast.makeText(this, R.string.enter_expression, Toast.LENGTH_SHORT).show();
            return;
        }
        PrimeFactorItem item = new PrimeFactorItem(mInputDisplay.getCleanText());
        new EvaluateTask(new ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                mSecondaryResultView.setText(result.get(0));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.hide();
                        mFabClose.show();
                        Toast.makeText(BasicCalculatorActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                }, 500);
            }

            @Override
            public void onError(Exception e) {
                onCalculateError(e);
            }
        }).execute(item);
    }

    /**
     * clickSolveEquation equation
     */
    public void clickSolveEquation() {
        String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
        if (inp.isEmpty()) {
            Toast.makeText(this, R.string.enter_expression, Toast.LENGTH_SHORT).show();
            return;
        }
        final SolveItem item = new SolveItem(inp);
        if (!item.getInput().contains("x")) {
            Toast.makeText(this, R.string.not_variable, Toast.LENGTH_SHORT).show();
            return;
        }
        new EvaluateTask(new ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                mSecondaryResultView.setText(result.get(0));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.hide();
                        mFabClose.show();
                        Toast.makeText(BasicCalculatorActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                }, 500);
            }

            @Override
            public void onError(Exception e) {
                onCalculateError(e);
            }
        }).execute(item);
    }

    /**
     * close Solve Result and animate
     */
    public void closeMathView() {
        setInputState(CalculateState.INPUT);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContainerSolve.setVisibility(View.GONE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mSolveAnimView, null, -2, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                }
            }, false);
        }
    }

    public void shareText() {
        String s = mInputDisplay.getCleanText();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.setType("text/plain");
        startActivity(intent);
    }


    public enum CalculatorState {
        INPUT,
        EVALUATE, RESULT, ERROR
    }


    protected class EvaluateTask extends AsyncTask<ExprInput, Void, String> {
        private Exception exception;
        private ResultCallback mCallback;

        public EvaluateTask(ResultCallback resultCallback) {
            this.mCallback = resultCallback;
        }

        @Override
        protected String doInBackground(ExprInput... params) {
            ExprInput item = params[0];
            String expr = item.getInput();
            try {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(getApplicationContext());
                if (item instanceof PrimeFactorItem) {
                    return MathEvaluator.getInstance().factorPrime(((PrimeFactorItem) item).getNumber());
                }
                return MathEvaluator.getInstance().evaluateWithResultAsTex(expr, config);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContainerSolve.setVisibility(View.VISIBLE);
            setInputState(CalculateState.RESULT);
            mProgress.show();
            mFabClose.hide();
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            if (exception != null) {
                mCallback.onError(exception);
            } else {
                mCallback.onSuccess(Lists.newArrayList(s));
            }

        }
    }

    private class InstantResultWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setState(CalculatorState.INPUT);
            if (mSetting.instantResult()) {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(BasicCalculatorActivity.this);
                mEvaluator.evaluateWithResultAsTex(mInputDisplay.getCleanText(), BasicCalculatorActivity.this, config);
            }
        }
    }
}

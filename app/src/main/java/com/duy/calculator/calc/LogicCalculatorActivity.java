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

package com.duy.calculator.calc;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractCalculatorActivity;
import com.duy.calculator.data.CalculatorSetting;
import com.duy.calculator.evaluator.LogicEvaluator;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.base.Base;
import com.duy.calculator.evaluator.base.NumberBaseManager;
import com.duy.calculator.view.AnimationFinishedListener;
import com.duy.calculator.view.ButtonID;
import com.duy.calculator.view.CalculatorEditText;
import com.duy.calculator.view.RevealView;


/**
 * Created by Duy on 3/7/2016
 */
public class LogicCalculatorActivity extends AbstractCalculatorActivity
        implements LogicEvaluator.EvaluateCallback, View.OnClickListener {
    private static final String TAG = LogicCalculatorActivity.class.getName();

    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private CalculatorEditText mInputDisplay;
    private TextView mTxtResult;
    private ViewGroup mDisplayForeground;
    private View mCurrentButton = null;
    private LogicCalculatorActivity.CalculatorState mCurrentState = LogicCalculatorActivity.CalculatorState.INPUT;
    private NumberBaseManager mBaseManager;
    private MathEvaluator mMathEvaluator;
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
    private final TextWatcher mFormulaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setState(LogicCalculatorActivity.CalculatorState.INPUT);
            Log.d(TAG, mInputDisplay.getCleanText());
            mMathEvaluator.evaluateBase(mInputDisplay.getCleanText(), LogicCalculatorActivity.this);
        }
    };

    protected void onChangeModeFraction() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMathEvaluator = MathEvaluator.getInstance();
        setContentView(R.layout.activity_base_calculator);
        setTitle(R.string.calculator);
        bindView();
        initPad();
        mBaseManager = new NumberBaseManager(mMathEvaluator.getBaseEvaluator().getBase());
    }

    private void bindView() {
        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInputDisplay.setShowSoftInputOnFocus(false);
        }
        mInputDisplay.addTextChangedListener(mFormulaTextWatcher);
        mInputDisplay.setOnKeyListener(mFormulaOnKeyListener);
        mInputDisplay.setAutoSuggestEnable(false);

        mTxtResult = (TextView) findViewById(R.id.txtResult);
    }

    private void initPad() {
        for (int id : ButtonID.getIDBase()) {
            try {
                View v = findViewById(id);
                if (v != null) v.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    void setState(LogicCalculatorActivity.CalculatorState state) {
        mCurrentState = state;
    }

    /**
     * Resume application
     */
    @Override
    public void onResume() {
        super.onResume();
        int base = mSetting.getInt(CalculatorSetting.BASE);
        switch (base) {
            case 2:
                setBase(Base.BINARY);
                break;
            case 8:
                setBase(Base.OCTAL);
                break;
            case 10:
                setBase(Base.DECIMAL);
                break;
            case 16:
                setBase(Base.HEXADECIMAL);
                break;
            default:
                setBase(Base.DECIMAL);
                break;
        }
        mInputDisplay.setText(mSetting.getString(CalculatorSetting.INPUT_BASE));
        mTxtResult.setText(mSetting.getString(CalculatorSetting.RESULT_BASE));
    }

    /**
     * Pause application
     */
    @Override
    public void onPause() {
        super.onPause();
        Base base = mMathEvaluator.getBaseEvaluator().getBase();
        int iBase = mMathEvaluator.getBaseEvaluator().getBaseModule().getBaseNumber(base);
        mSetting.put(CalculatorSetting.BASE, iBase);
        mSetting.put(CalculatorSetting.INPUT_BASE, mInputDisplay.getCleanText());
    }

    public void setBase(final Base base) {
        mBaseManager.setNumberBase(base);
        mMathEvaluator.setBase(mInputDisplay.getCleanText(), base, new LogicEvaluator.EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                if (errorResourceId == LogicEvaluator.RESULT_ERROR) {
                    onError(getResources().getString(R.string.error));
                } else {
                    mTxtResult.setText(result);
                    onResult(result);
                    setState(LogicCalculatorActivity.CalculatorState.INPUT);
                }
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        updateUI();
    }

    private void updateUI() {
        setSelectionButton(mMathEvaluator.getBaseEvaluator().getBase());
        for (int id : mBaseManager.getViewIds()) {
            final View view = findViewById(id);
            if (view != null) {
                final boolean isDisable = mBaseManager.isViewDisabled(id);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(!isDisable);
                    }
                });
            }
        }
    }

    private void setSelectionButton(Base base) {
        Log.d(TAG, "setSelectionButton: ");
        findViewById(R.id.hex).setSelected(base.equals(Base.HEXADECIMAL));
        findViewById(R.id.bin).setSelected(base.equals(Base.BINARY));
        findViewById(R.id.dec).setSelected(base.equals(Base.DECIMAL));
        findViewById(R.id.oct).setSelected(base.equals(Base.OCTAL));
    }

    @Override
    public void setTextDisplay(String s) {
        mInputDisplay.setText(s);
    }

    @Override
    public void insertText(String string) {
        mInputDisplay.insert(string);
    }

    @Override
    public void insertOperator(String s) {
        mInputDisplay.insert(s);
    }

    @Override
    public String getTextClean() {
        return null;
    }

    @Override
    public void onResult(final String result) {
        Log.d(TAG, "clickEvaluate " + result);
        final float resultScale =
                mInputDisplay.getTextSize() / mTxtResult.getTextSize();
        final float resultTranslationX = (1.0f - resultScale) *
                (mTxtResult.getWidth() / 2.0f - mTxtResult.getPaddingRight());

        final float formulaRealHeight = mInputDisplay.getHeight()
                - mInputDisplay.getPaddingTop()
                - mInputDisplay.getPaddingBottom();

        final float resultRealHeight = resultScale *
                (mTxtResult.getHeight()
                        - mTxtResult.getPaddingTop()
                        - mTxtResult.getPaddingBottom());
        final float resultTranslationY =
                -mInputDisplay.getHeight()
                        - resultScale * mTxtResult.getPaddingTop()
                        + mInputDisplay.getPaddingTop()
                        + (formulaRealHeight - resultRealHeight) / 2;

        final float formulaTranslationY = -mInputDisplay.getBottom();
        final int resultTextColor = mTxtResult.getCurrentTextColor();
        final int formulaTextColor = mInputDisplay.getCurrentTextColor();
        final ValueAnimator textColorAnimator =
                ValueAnimator.ofObject(new ArgbEvaluator(), resultTextColor, formulaTextColor);
        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTxtResult.setTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        mTxtResult.setText(result);
        mTxtResult.setPivotX(mTxtResult.getWidth() / 2);
        mTxtResult.setPivotY(0f);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                textColorAnimator,
                ObjectAnimator.ofFloat(mTxtResult, View.SCALE_X, resultScale),
                ObjectAnimator.ofFloat(mTxtResult, View.SCALE_Y, resultScale),
                ObjectAnimator.ofFloat(mTxtResult, View.TRANSLATION_X, resultTranslationX),
                ObjectAnimator.ofFloat(mTxtResult, View.TRANSLATION_Y, resultTranslationY),
                ObjectAnimator.ofFloat(mInputDisplay, View.TRANSLATION_Y, formulaTranslationY));
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                mTxtResult.setPivotY(mTxtResult.getHeight() / 2);
                mTxtResult.setTextColor(resultTextColor);
                mTxtResult.setScaleX(1.0f);
                mTxtResult.setScaleY(1.0f);
                mTxtResult.setTranslationX(0.0f);
                mTxtResult.setTranslationY(0.0f);
                mInputDisplay.setTranslationY(0.0f);
                mInputDisplay.setText(result);
                setState(LogicCalculatorActivity.CalculatorState.RESULT);
            }
        });

        playAnimation(animatorSet);
    }

    protected void playAnimation(Animator animator) {
        animator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
            }
        });
        animator.start();
    }

    /**
     * only use animate on device lollipop
     *
     * @param sourceView
     * @param color
     * @param listener
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRipple(View sourceView, int color, final Animator.AnimatorListener listener) {
        // Make animateRipple cover the display
        final RevealView revealView = new RevealView(this);
        revealView.setLayoutParams(mLayoutParams);
        revealView.setRevealColor(color);
        mDisplayForeground.addView(revealView);

        final Animator revealAnimator;
        final int[] clearLocation = new int[2];
        if (sourceView != null) {
            sourceView.getLocationInWindow(clearLocation);
            clearLocation[0] += sourceView.getWidth() / 2;
            clearLocation[1] += sourceView.getHeight() / 2;
        } else {
            clearLocation[0] = mDisplayForeground.getWidth() / 2;
            clearLocation[1] = mDisplayForeground.getHeight() / 2;
        }
        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();
        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        revealAnimator = ViewAnimationUtils.createCircularReveal(revealView,
                revealCenterX, revealCenterY, 0.0f, revealRadius);
        revealAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_shortAnimTime));
        revealAnimator.addListener(listener);

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(getResources()
                .getInteger(android.R.integer.config_shortAnimTime));
        alphaAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                mDisplayForeground.removeView(revealView);
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

    @Override
    public void onError(final String errorResourceId) {
        if (mCurrentState != CalculatorState.EVALUATE) {
            // Only animate error on evaluate.
            mTxtResult.setText(errorResourceId);
            return;
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorResultError, typedValue, true);
        int color = typedValue.data;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(CalculatorState.ERROR);
                    mTxtResult.setText(errorResourceId);
                }
            });
        } else {
            setState(CalculatorState.ERROR);
            mTxtResult.setText(errorResourceId);
        }
    }

    @Override
    public void onDelete() {
        mInputDisplay.backspace();
    }

    @Override
    public void clickClear() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorClearScreen, typedValue, true);
        int color = typedValue.data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(LogicCalculatorActivity.CalculatorState.INPUT);
                    mTxtResult.setText("");
                    mInputDisplay.clear();
                }
            });
        } else {
            setState(LogicCalculatorActivity.CalculatorState.INPUT);
            mTxtResult.setText("");
            mInputDisplay.clear();
        }
    }

    @Override
    public void onEqual() {
        String text = mInputDisplay.getCleanText();
        if (mCurrentState == LogicCalculatorActivity.CalculatorState.INPUT) {
            setState(LogicCalculatorActivity.CalculatorState.EVALUATE);
            mMathEvaluator.evaluateBase(text, this);
        }
    }

//    @Override
//    public void onInputVoice() {
//
//    }

    @Override
    public void onEvaluated(String expr, String result, int resultId) {
        Log.d(TAG, "onEvaluated " + expr + " = " + result + " with error " + resultId);
        if (resultId == LogicEvaluator.RESULT_ERROR) {
            if (mCurrentState == LogicCalculatorActivity.CalculatorState.INPUT) {
                mTxtResult.setText(null);
            } else if (mCurrentState == LogicCalculatorActivity.CalculatorState.EVALUATE) {
                onError(result);
            }
        } else if (resultId == LogicEvaluator.RESULT_OK) {
            if (mCurrentState == LogicCalculatorActivity.CalculatorState.EVALUATE) {
                onResult(result);
                //      saveHistory(mExpression, mResult, ItemHistory.TYPE_LOGIC);
            } else if (mCurrentState == LogicCalculatorActivity.CalculatorState.INPUT) {
                if (result == null) {
                    mTxtResult.setText(null);
                } else {
                    mTxtResult.setText(result);
                }
            }
        } else if (resultId == LogicEvaluator.INPUT_EMPTY) {

        }
    }

    @Override
    public void onCalculateError(Exception e) {

    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_delete:
                mCurrentButton = view;
                onDelete();
                break;
            case R.id.btn_clear:
                mCurrentButton = view;
                clickClear();
                break;
            case R.id.btn_equal:
                onEqual();
                break;
            case R.id.btn_sin:
            case R.id.btn_cos:
            case R.id.btn_tan:
            case R.id.btn_arcsin:
            case R.id.btn_arccos:
            case R.id.btn_arctan:
            case R.id.btn_arctanh:
            case R.id.btn_arccosh:
            case R.id.btn_arcsinh:
            case R.id.btn_log:
            case R.id.btn_ln:
            case R.id.btn_tanh:
            case R.id.btn_cosh:
            case R.id.btn_sinh:
            case R.id.btn_abs:
            case R.id.btn_floor:
            case R.id.btn_ceil:
            case R.id.btn_sign:
            case R.id.btn_max:
            case R.id.btn_min:
            case R.id.btn_sqrt:
            case R.id.btn_exp:
            case R.id.btn_gcd:
            case R.id.btn_percent:
            case R.id.btn_perm:
            case R.id.btn_combi:
            case R.id.btn_cbrt:
            case R.id.btn_mod:
            case R.id.btn_lcm:
                insertText(((Button) view).getText().toString() + getResources().getString(R.string.leftParen));
                break;
            case R.id.btn_power:
            case R.id.btn_factorial:
                insertOperator(((Button) view).getText().toString());
                break;
            case R.id.btn_plus:
            case R.id.btn_div:
            case R.id.btn_mul:
            case R.id.btn_minus:
                insertOperator("" + ((Button) view).getText().toString() + "");
                break;
            case R.id.btn_input_voice:
//                ((AbstractCalculatorFragment) mHandler).onInputVoice();
                break;


            case R.id.op_and:
            case R.id.op_or:
            case R.id.op_xor:
            case R.id.op_neg:
            case R.id.op_equal:
            case R.id.btn_leq:
            case R.id.btn_geq:
            case R.id.btn_lt:
            case R.id.btn_gt:
                insertText(" " + ((Button) view).getText().toString() + " ");
                break;
            case R.id.hex:
                setBase(Base.HEXADECIMAL);
                break;
            case R.id.dec:
                setBase(Base.DECIMAL);
                break;
            case R.id.bin:
                setBase(Base.BINARY);
                break;
            case R.id.oct:
                setBase(Base.OCTAL);
                break;
            default:
                if (view instanceof Button) {
                    insertText(((Button) view).getText().toString());
                }
                break;
        }
        Log.d("Button", "onClick: ");
    }


    private enum CalculatorState {
        INPUT, EVALUATE, RESULT, ERROR
    }
}
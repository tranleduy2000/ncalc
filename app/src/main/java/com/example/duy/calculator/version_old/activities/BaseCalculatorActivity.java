package com.example.duy.calculator.version_old.activities;

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

import com.example.duy.calculator.R;
import com.example.duy.calculator.data.CalculatorSetting;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.math_eval.base.Base;
import com.example.duy.calculator.math_eval.base.NumberBaseManager;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractCalculatorActivity;
import com.example.duy.calculator.view.AnimationFinishedListener;
import com.example.duy.calculator.view.ButtonID;
import com.example.duy.calculator.view.CalculatorEditText;
import com.example.duy.calculator.view.RevealView;


/**
 * Created by Duy on 3/7/2016
 */
public class BaseCalculatorActivity extends AbstractCalculatorActivity
        implements LogicEvaluator.EvaluateCallback, View.OnClickListener {
    private static final String TAG = BaseCalculatorActivity.class.getName();
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private CalculatorEditText mInputDisplay;
    private TextView txtResult;
    private ViewGroup mDisplayForeground;
    private View mCurrentButton = null;
    private BaseCalculatorActivity.CalculatorState mCurrentState = BaseCalculatorActivity.CalculatorState.INPUT;
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
            setState(BaseCalculatorActivity.CalculatorState.INPUT);
            Log.d(TAG, mInputDisplay.getCleanText());
            BigEvaluator.getInstance(getApplicationContext()).evaluateBase(mInputDisplay.getCleanText(), BaseCalculatorActivity.this);
        }
    };
    private NumberBaseManager mBaseManager;


    protected void onChangeModeFraction() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_calculator);
        setTitle(R.string.calculator);

        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInputDisplay.setShowSoftInputOnFocus(false);
        }
        mInputDisplay.addTextChangedListener(mFormulaTextWatcher);
        mInputDisplay.setOnKeyListener(mFormulaOnKeyListener);
        mInputDisplay.setFormatText(false);

        txtResult = (TextView) findViewById(R.id.txtResult);
        mBaseManager = new NumberBaseManager(BigEvaluator.getInstance(getApplicationContext()).getSolver().getBase());
        initPad();


        boolean dieuKien = true;
        if (dieuKien == true) {
            System.out.println("DK dung");
        } else {
            System.out.println("DK sai");
        }


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

    /**
     * @param state
     */
    void setState(BaseCalculatorActivity.CalculatorState state) {
        mCurrentState = state;
    }

    /**
     * Resume application
     */
    @Override
    public void onResume() {
        super.onResume();

        int base = mSetting.getInt(CalculatorSetting.BASE);
        mInputDisplay.setText(mSetting.getString(CalculatorSetting.INPUT_BASE));
        txtResult.setText(mSetting.getString(CalculatorSetting.RESULT_BASE));
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
        }
    }

    /**
     * Pause application
     */
    @Override
    public void onPause() {
        super.onPause();
        Base base = BigEvaluator.getInstance(getApplicationContext()).getSolver().getBase();
        int iBase = BigEvaluator.getInstance(getApplicationContext()).getSolver().getBaseModule().getBaseNumber(base);
        mSetting.put(CalculatorSetting.BASE, iBase);
        mSetting.put(CalculatorSetting.INPUT_BASE, mInputDisplay.getCleanText());
    }

    public void setBase(final Base base) {
        mBaseManager.setNumberBase(base);
        BigEvaluator.getInstance(getApplicationContext()).setBase(mInputDisplay.getCleanText(), base, new LogicEvaluator.EvaluateCallback() {
            @Override
            public void onEvaluate(String expr, String result, int errorResourceId) {
                if (errorResourceId == LogicEvaluator.RESULT_ERROR) {
                    Log.d(TAG, "onEvaluate: error on evaluate " + expr);
                    onError(getResources().getString(R.string.error));
                } else {
                    txtResult.setText(result);
                    onResult(result);
                    setState(BaseCalculatorActivity.CalculatorState.INPUT);
                }
            }
        });
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "updateUI: ");
        setSelectionButton(BigEvaluator.getInstance(getApplicationContext()).getSolver().getBase());
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
        Log.d(TAG, "doEval " + result);
        final float resultScale =
                mInputDisplay.getTextSize() / txtResult.getTextSize();
        final float resultTranslationX = (1.0f - resultScale) *
                (txtResult.getWidth() / 2.0f - txtResult.getPaddingRight());

        final float formulaRealHeight = mInputDisplay.getHeight()
                - mInputDisplay.getPaddingTop()
                - mInputDisplay.getPaddingBottom();

        final float resultRealHeight = resultScale *
                (txtResult.getHeight()
                        - txtResult.getPaddingTop()
                        - txtResult.getPaddingBottom());
        final float resultTranslationY =
                -mInputDisplay.getHeight()
                        - resultScale * txtResult.getPaddingTop()
                        + mInputDisplay.getPaddingTop()
                        + (formulaRealHeight - resultRealHeight) / 2;

        final float formulaTranslationY = -mInputDisplay.getBottom();
        final int resultTextColor = txtResult.getCurrentTextColor();
        final int formulaTextColor = mInputDisplay.getCurrentTextColor();
        final ValueAnimator textColorAnimator =
                ValueAnimator.ofObject(new ArgbEvaluator(), resultTextColor, formulaTextColor);
        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                txtResult.setTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        txtResult.setText(result);
        txtResult.setPivotX(txtResult.getWidth() / 2);
        txtResult.setPivotY(0f);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                textColorAnimator,
                ObjectAnimator.ofFloat(txtResult, View.SCALE_X, resultScale),
                ObjectAnimator.ofFloat(txtResult, View.SCALE_Y, resultScale),
                ObjectAnimator.ofFloat(txtResult, View.TRANSLATION_X, resultTranslationX),
                ObjectAnimator.ofFloat(txtResult, View.TRANSLATION_Y, resultTranslationY),
                ObjectAnimator.ofFloat(mInputDisplay, View.TRANSLATION_Y, formulaTranslationY));
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                txtResult.setPivotY(txtResult.getHeight() / 2);
                txtResult.setTextColor(resultTextColor);
                txtResult.setScaleX(1.0f);
                txtResult.setScaleY(1.0f);
                txtResult.setTranslationX(0.0f);
                txtResult.setTranslationY(0.0f);
                mInputDisplay.setTranslationY(0.0f);
                mInputDisplay.setText(result);
                setState(BaseCalculatorActivity.CalculatorState.RESULT);
            }
        });

        play(animatorSet);
    }

    protected void play(Animator animator) {
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
                play(alphaAnimator);
            }
        });
        play(revealAnimator);
    }

    @Override
    public void onError(final String errorResourceId) {
        if (mCurrentState != CalculatorState.EVALUATE) {
            // Only animate error on evaluate.
            txtResult.setText(errorResourceId);
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
                    txtResult.setText(errorResourceId);
                }
            });
        } else {
            setState(CalculatorState.ERROR);
            txtResult.setText(errorResourceId);
        }
    }

    @Override
    public void onDelete() {
        mInputDisplay.backspace();
    }

    @Override
    public void onClear() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorClearScreen, typedValue, true);
        int color = typedValue.data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(BaseCalculatorActivity.CalculatorState.INPUT);
                    txtResult.setText("");
                    mInputDisplay.clear();
                }
            });
        } else {
            setState(BaseCalculatorActivity.CalculatorState.INPUT);
            txtResult.setText("");
            mInputDisplay.clear();
        }
    }

    @Override
    public void onEqual() {
        String text = mInputDisplay.getCleanText();
        if (mCurrentState == BaseCalculatorActivity.CalculatorState.INPUT) {
            setState(BaseCalculatorActivity.CalculatorState.EVALUATE);
            BigEvaluator.getInstance(getApplicationContext()).evaluateBase(text, this);
        }
    }

//    @Override
//    public void onInputVoice() {
//
//    }

    @Override
    public void onEvaluate(String expr, String result, int resultId) {
        Log.d(TAG, "onEvaluate " + expr + " = " + result + " with error " + resultId);
        if (resultId == LogicEvaluator.RESULT_ERROR) {
            if (mCurrentState == BaseCalculatorActivity.CalculatorState.INPUT) {
                txtResult.setText(null);
            } else if (mCurrentState == BaseCalculatorActivity.CalculatorState.EVALUATE) {
                onError(result);
            }
        } else if (resultId == LogicEvaluator.RESULT_OK) {
            if (mCurrentState == BaseCalculatorActivity.CalculatorState.EVALUATE) {
                onResult(result);
                //      saveHistory(mExpression, mResult, ItemHistory.TYPE_LOGIC);
            } else if (mCurrentState == BaseCalculatorActivity.CalculatorState.INPUT) {
                if (result == null) {
                    txtResult.setText(null);
                } else {
                    txtResult.setText(result);
                }
            }
        } else if (resultId == LogicEvaluator.INPUT_EMPTY) {

        }
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
                onClear();
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


    public enum CalculatorState {
        INPUT, EVALUATE, RESULT, GRAPHING, ERROR
    }
}
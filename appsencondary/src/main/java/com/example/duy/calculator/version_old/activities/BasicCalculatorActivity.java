package com.example.duy.calculator.version_old.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.EInputState;
import com.example.duy.calculator.R;
import com.example.duy.calculator.SettingsActivity;
import com.example.duy.calculator.data.CalculatorSetting;
import com.example.duy.calculator.define.DefineVariableActivity;
import com.example.duy.calculator.helper.HelperActivity;
import com.example.duy.calculator.history.HistoryActivity;
import com.example.duy.calculator.history.HistoryAdapter;
import com.example.duy.calculator.history.HistoryEntry;
import com.example.duy.calculator.item_math_type.DerivativeItem;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.NumberIntegerItem;
import com.example.duy.calculator.item_math_type.SolveItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.math_eval.base.Evaluator;
import com.example.duy.calculator.ocr.OcrManager;
import com.example.duy.calculator.utils.ClipboardManager;
import com.example.duy.calculator.utils.VoiceUtils;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractCalculatorActivity;
import com.example.duy.calculator.view.AnimationFinishedListener;
import com.example.duy.calculator.view.ButtonID;
import com.example.duy.calculator.view.CalculatorEditText;
import com.example.duy.calculator.view.RevealView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kexanie.library.MathView;


public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements LogicEvaluator.EvaluateCallback, View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = BasicCalculatorActivity.class.getSimpleName();
    public static final String DATA = "DATA_BUNDLE";
    private static final int REQ_CODE_HISTORY = 1111;
    private static final int REQ_CODE_DEFINE_VAR = 1234;
    private final int REQ_CODE_SPEECH_INPUT = 1235;
    @BindView(R.id.math_result)
    public MathView mMathView;
    @BindView(R.id.progress_bar_main)
    public ContentLoadingProgressBar mProgress;

    public FrameLayout mAnimateSolve;
    @BindView(R.id.sw_fraction)
    SwitchCompat switchCompat;
    @BindView(R.id.progress_eval)
    ProgressBar mProgressEval;
    @BindView(R.id.container_solve)
    FrameLayout mContainerSolve;
    //    @BindView(R.id.graph_controller)
//    SlidingUpPanelLayout mGraphView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.slide)
    SlidingUpPanelLayout padAdvance;
    @BindView(R.id.fab_close)
    FloatingActionButton mFabClose;
    private CalculatorEditText mInputDisplay;
    private ViewGroup mDisplayForeground;
    private View mCurrentButton = null;
    private BasicCalculatorActivity.CalculatorState mCurrentState = BasicCalculatorActivity.CalculatorState.INPUT;
    private BigEvaluator mEvaluator;
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
            setState(BasicCalculatorActivity.CalculatorState.INPUT);
            if (mSetting.instantResult())
                mEvaluator.evaluateWithResultAsTex(mInputDisplay.getCleanText(), BasicCalculatorActivity.this);
//                new TaskEval().execute();
        }
    };
    private MathView mReview;
    private PagerState mPageState;
    private EInputState mEInputState = EInputState.PAD;
    private HistoryAdapter mHistoryAdapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        mEvaluator = BigEvaluator.newInstance(this);
        ButterKnife.bind(this);

        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInputDisplay.setShowSoftInputOnFocus(false);
        }
//        mInputDisplay.requestFocus();
//        hideKeyboard(mInputDisplay);

        mInputDisplay.addTextChangedListener(mFormulaTextWatcher);
        mInputDisplay.setOnKeyListener(mFormulaOnKeyListener);
        mReview = (MathView) findViewById(R.id.math_view);

        /**
         * for clickSolveEquation result view
         */
        mAnimateSolve = (FrameLayout) findViewById(R.id.animation_pad).findViewById(R.id.result_animation);

        setInputState(EInputState.PAD);
        setState(BasicCalculatorActivity.CalculatorState.INPUT);
        initPad();

        findViewById(R.id.img_history).setOnClickListener(this);
        findViewById(R.id.img_camera).setOnClickListener(this);
        setUpVoice();
        setModeFraction();
        showHelp();
        showDialogUpdate();
    }

    private void setModeFraction() {
        switchCompat.setChecked(mSetting.useFraction());
        mEvaluator.setFraction(mSetting.useFraction());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != mSetting.useFraction()) {
                    mSetting.setFraction(isChecked);
                    mEvaluator.setFraction(isChecked);
                }
                onChangeModeFraction();
            }
        });
        switchCompat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BasicCalculatorActivity.this, R.string.fraction_decs, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void initPad() {
        for (int id : ButtonID.getIdBasic()) {
            try {
                View v = findViewById(id);
                if (v != null) {
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                } else {
                    v = findViewById(R.id.pad_basic).findViewById(id);
                    if (v != null) {
                        v.setOnClickListener(this);
                        v.setOnLongClickListener(this);
                    } else {
                        v = findViewById(R.id.pad_advance).findViewById(id);
                        if (v != null) {
                            v.setOnClickListener(this);
                            v.setOnLongClickListener(this);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 22-Jan-17 fix bug
        /**
         * Exception java.lang.RuntimeException:
         * Unable to resume activity
         * {com.duy.calculator.free/com.example.duy.calculator.version_old.activities.BasicCalculatorActivity}: java.lang.NullPointerException:
         * Attempt to invoke virtual method
         * 'void io.github.kexanie.library.MathView.setText(java.lang.String)'
         * on a null object reference
         */
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

    @Override
    protected void onStart() {
        super.onStart();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showHelp() {
        if (mPreferences.getBoolean(BasicCalculatorActivity.class.getSimpleName(), false)) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TapTarget target = TapTarget.forView(switchCompat,
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

    /**
     * set input state
     *
     * @param pad - enum {@link EInputState}
     */
    private void setInputState(EInputState pad) {
        mEInputState = pad;
    }

    private void setPageState(PagerState state) {
        mPageState = state;
    }

    public void define(String var, double value) {
        mEvaluator.define(var, value);
    }

    public void define(String var, String value) {
        mEvaluator.define(var, value);
    }

    private void updateValueVariable(String var, String result) {
//        if (var.toLowerCase().equals("a")) {
//            ((TextView) findViewById(R.id.txt_var_a)).setText(result);
//        } else if (var.toLowerCase().equals("b")) {
//            ((TextView) findViewById(R.id.txt_var_b)).setText(result);
//        } else if (var.toLowerCase().equals("c")) {
//            ((TextView) findViewById(R.id.txt_var_c)).setText(result);
//        } else if (var.toLowerCase().equals("d")) {
//            ((TextView) findViewById(R.id.txt_var_d)).setText(result);
//        }
    }

    public void onDelete() {
        mInputDisplay.backspace();
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
                    setState(BasicCalculatorActivity.CalculatorState.ERROR);
                    setTextError(error);
                }
            }, true);
        } else {
            setState(BasicCalculatorActivity.CalculatorState.ERROR);
            setTextError(error);
        }
    }

    public void setTextResult(String result) {
        mReview.setText(result);
    }

    public void setTextError(final String result) {
        mReview.setText("<h2>" + result + "</h2>");
    }

    public void onResult(final String result) {
//        Log.d(TAG, "doEval " + result);
        setTextDisplay(result.replace("\\", "").replace("\n", ""));
        setTextResult("");
    }

    /**
     * only show animate ripple on device lollipop
     *
     * @param sourceView
     * @param color
     * @param listener
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRipple(final ViewGroup foreground, View sourceView,
                               int color, final Animator.AnimatorListener listener, final boolean out) {
        if (color == -1) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
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
        revealAnimator.addListener(listener);
        revealAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                foreground.removeView(revealView);
            }
        });
        play(revealAnimator);
    }

    public void insertText(String text) {
        //set text display is null if not as operator
        boolean b = mInputDisplay.getSelectionStart() == mInputDisplay.getCleanText().length() + 1;
        if (mCurrentState == BasicCalculatorActivity.CalculatorState.RESULT && !Evaluator.isOperator(text) && b) {
            mInputDisplay.clear();
        }
        mInputDisplay.insert(text);
    }

    public void onClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorClearScreen, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, mCurrentButton, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(BasicCalculatorActivity.CalculatorState.INPUT);
                    mInputDisplay.clear();
                    mReview.setText("");
                }
            }, true);
        } else {
            setState(BasicCalculatorActivity.CalculatorState.INPUT);
            mReview.setText("");
            mInputDisplay.clear();
        }
    }

    public void onEqual() {
        String text = mInputDisplay.getCleanText();
        setState(BasicCalculatorActivity.CalculatorState.EVALUATE);
        mEvaluator.evaluateWithResultNormal(text, BasicCalculatorActivity.this);
    }

    public void onInputVoice() {
        /**
         * Showing google speech input dialog
         */
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_expression));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * set state
     *
     * @param state - state
     */
    void setState(BasicCalculatorActivity.CalculatorState state) {
        mCurrentState = state;
    }

    /**
     * insert text to display
     *
     * @param opt - operator
     */
    public void insertOperator(String opt) {
        if (mCurrentState == BasicCalculatorActivity.CalculatorState.INPUT) {
            //do something
        } else if (mCurrentState == BasicCalculatorActivity.CalculatorState.RESULT) {
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
    public void onPause() {
        super.onPause();
        CalculatorSetting preferences = new CalculatorSetting(this);
        String math = mInputDisplay.getCleanText();
        preferences.put(CalculatorSetting.INPUT_MATH, math);
//       fix bug -> mMathView.setText(""); -> mReview
        mReview.setText("");
    }

    @Override
    public void onEvaluate(String expr, String result, int resultId) {
        if (resultId == LogicEvaluator.RESULT_ERROR) {
            if (mCurrentState == BasicCalculatorActivity.CalculatorState.INPUT) {
                setTextResult(""); //clear
            } else if (mCurrentState == BasicCalculatorActivity.CalculatorState.EVALUATE) {
                onError(getResources().getString(R.string.error)); //process error if user push equal button
                saveHistory(expr, getResources().getString(R.string.error), true); //save history
            }
        } else if (resultId == LogicEvaluator.RESULT_ERROR_WITH_INDEX) {
            if (mCurrentState == BasicCalculatorActivity.CalculatorState.INPUT) {
                setTextResult("");
            } else if (mCurrentState == BasicCalculatorActivity.CalculatorState.EVALUATE) {
                //process output
                onError(result, getResources().getString(R.string.error));
                saveHistory(expr, getResources().getString(R.string.error), true);
            }
        } else if (resultId == LogicEvaluator.RESULT_OK) {
            if (mCurrentState == BasicCalculatorActivity.CalculatorState.EVALUATE) {
                onResult(result);
                saveHistory(expr, result, true);
                mEvaluator.define("ans", result);
            } else if (mCurrentState == BasicCalculatorActivity.CalculatorState.INPUT) {
                if (result == null) {
                    setTextResult("");
                } else {
                    setTextResult(result);
                }
            }
        } else if (resultId == LogicEvaluator.INPUT_EMPTY) {

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
                    setState(BasicCalculatorActivity.CalculatorState.ERROR);
                    setTextError(result);
                }
            }, true);
        } else {
            mInputDisplay.setText((display));
            if (indexError >= 0) {
                mInputDisplay.setSelection(indexError, indexError + 1);
            }
            setState(BasicCalculatorActivity.CalculatorState.ERROR);
            setTextError(result);
        }


    }

    public void copyText() {
        ClipboardManager.setClipboard(this, mInputDisplay.getCleanText());
    }

    public void pasteText() {
        mInputDisplay.setText(ClipboardManager.getClipboard(this));

    }

    public void shareText() {
        String s = mInputDisplay.getCleanText();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * clickSolveEquation equation
     */
    public void clickSolveEquation() {
        Log.d(TAG, "clickSolveEquation: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mAnimateSolve, mCurrentButton, -1, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    mContainerSolve.setVisibility(View.VISIBLE);
                    setInputState(EInputState.RESULT_VIEW);
                    String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
                    SolveItem item = new SolveItem(inp);
                    new TaskSolve().execute(item);
                }
            }, true);
        } else {
            mContainerSolve.setVisibility(View.VISIBLE);
            setInputState(EInputState.RESULT_VIEW);
            String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
            SolveItem item = new SolveItem(inp);
            new TaskSolve().execute(item);
        }
    }

    /**
     * close Solve Result and animate
     */

    private void clickCloseMathView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mMathView.setText("");
                mContainerSolve.setVisibility(View.GONE);
            }
        });
        Log.d(TAG, "clickCloseMathView: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mAnimateSolve, mCurrentButton, -1, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {

                }
            }, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected boolean saveHistory(String expr, String result, boolean ensureResult) {
        DLog.i("Save history: " + expr + " = " + result);
        mHistoryDatabase.saveHistory(new HistoryEntry(expr, result));
        return false;
    }

    public void onDefineAndCalc() {
        //show acitivity parameter
        //click ok and show
        Intent intent = new Intent(this, DefineVariableActivity.class);
        intent.putExtra(DATA, mInputDisplay.getCleanText());
        startActivityForResult(intent, REQ_CODE_DEFINE_VAR);
    }

    public void clickFactorPrime() {
        mEvaluator.setFraction(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mAnimateSolve, mCurrentButton, -1, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    mContainerSolve.setVisibility(View.VISIBLE);
                    setInputState(EInputState.RESULT_VIEW);
                }
            }, true);
        } else {
            mContainerSolve.setVisibility(View.VISIBLE);
            setInputState(EInputState.RESULT_VIEW);
        }
        mEvaluator.factorPrime(mInputDisplay.getCleanText(),
                new LogicEvaluator.EvaluateCallback() {
                    @Override
                    public void onEvaluate(String expr, String result, int errorResourceId) {
                        mMathView.setText(result);
                    }
                });
    }

    protected void onChangeModeFraction() {
        mEvaluator.evaluateWithResultAsTex(mInputDisplay.getCleanText(), this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return;
            }
        }

        /**
         * collapse pad
         */
        if (padAdvance != null) {
            if (padAdvance.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                padAdvance.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                return;
            }
        }

//        if (mGraphView.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
//            mGraphView.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            return;
//        }
        super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (view instanceof TextView) {
            Log.d(TAG, "onClick: " + ((TextView) view).getText().toString());
        }
        switch (id) {

            case R.id.img_camera:
                OcrManager ocrManager = new OcrManager();
                ocrManager.startOcr(this);
                break;
            case R.id.img_history:
                startActivityForResult(new Intent(BasicCalculatorActivity.this, HistoryActivity.class),
                        REQ_CODE_HISTORY);
                break;
            case R.id.btn_derivative:
                clickDerivative();
                break;
            case R.id.btn_graph_main:
                clickGraph();
                break;
            case R.id.btn_ten_power:
                insertText("10^");
                break;
            case R.id.btn_power_2:
                insertText("^");
                insertText("2");
                break;
            case R.id.btn_power_3:
                insertText("^");
                insertText("3");
                break;
            case R.id.btn_calc:
                onDefineAndCalc();
                break;
            case R.id.btn_fact:
                clickFactorPrime();
                break;
            case R.id.btn_help:
                startActivity(new Intent(getApplicationContext(), HelperActivity.class));
            case R.id.btn_solve_:
                mCurrentButton = view;
                clickSolveEquation();
                break;
            case R.id.fab_close:
                mCurrentButton = view;
                closeMathView();
                break;
//            case R.id.img_copy:
//                copyText();
//                break;
//            case R.id.img_paste:
//                pasteText();
//                break;
            case R.id.img_setting:
                startActivity(new Intent(BasicCalculatorActivity.this, SettingsActivity.class));
                break;
            case R.id.img_share:
                shareText();
                break;
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
            case R.id.btn_arcsin:
            case R.id.btn_arccos:
            case R.id.btn_arctan:
            case R.id.btn_arctanh:
            case R.id.btn_arccosh:
            case R.id.btn_arcsinh:
            case R.id.btn_sin:
            case R.id.btn_cos:
            case R.id.btn_tan:
            case R.id.btn_tanh:
            case R.id.btn_cosh:
            case R.id.btn_sinh:
            case R.id.btn_log:
            case R.id.btn_ln:
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
                startVoiceInput();
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
            default:
                if (view instanceof Button) {
                    insertText(((Button) view).getText().toString());
                }
                break;
        }
    }

    /**
     * close Solve Result and animate
     */
    private void closeMathView() {
        setInputState(EInputState.PAD);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mContainerSolve.setVisibility(View.GONE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRipple(mAnimateSolve, null, -2, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                }
            }, false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                onClear();
                return true;
        }
        return false;
    }

    public void clickGraph() {
//        Log.d(TAG, "clickGraph: ");
//        String function = mInputDisplay.getCleanText();
//        FastGraphView graph2DView = (FastGraphView) findViewById(R.id.graph_view);
//        graph2DView.drawGraph(function);
//        mGraphView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    processOutputVoice(data);
                }
                break;

            case OcrManager.OCR_REQUEST_CODE:
                OcrManager ocrManager = new OcrManager();
                ocrManager.processResult(BasicCalculatorActivity.this, resultCode, data);
                break;

            case REQ_CODE_HISTORY:
                Log.d(TAG, "onActivityResult: history");
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getBundleExtra(DATA);
                    final HistoryEntry history = (HistoryEntry) bundle.getSerializable(DATA);
                    Log.d(TAG, "onActivityResult: " + history.getMath() + " " + history.getResult());
                    mInputDisplay.post(new Runnable() {
                        @Override
                        public void run() {
                            mInputDisplay.setText(history.getMath());
                        }
                    });
                }
                break;
            case REQ_CODE_DEFINE_VAR:
                mEvaluator.evaluateWithResultNormal(mInputDisplay.getCleanText(), this);
                //onEqual();
                break;
        }
    }

    /**
     * out put result text
     *
     * @param data
     */
    private void processOutputVoice(Intent data) {
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
        String res = result.get(0);
        res = VoiceUtils.replace(res);
        final String finalRes = res;
        mInputDisplay.post(new Runnable() {
            @Override
            public void run() {
                mInputDisplay.setText(finalRes);
            }
        });
    }


    private void clickDerivative() {
        String inp = mTokenizer.getNormalExpression(mInputDisplay.getCleanText());
        if (inp.isEmpty()) {
            Toast.makeText(this, R.string.enter_expression, Toast.LENGTH_SHORT).show();
            return;
        }

        //check error before evaluate
        DerivativeItem item = new DerivativeItem(mInputDisplay.getCleanText(), "x", "1");
        new ATaskEval().execute(item);
    }

    public enum CalculatorState {
        INPUT,
        EVALUATE, RESULT, ERROR
    }

    public enum PagerState {PAGE_SCIENCE, PAGE_NUMBER, PAGE_HISTORY, PAGE_CONSTANT}


    private class TaskSolve extends AsyncTask<IExprInput, Void, String> {
        @Override
        protected String doInBackground(IExprInput... iExprInputs) {
            SolveItem item = (SolveItem) iExprInputs[0];
//            Log.d(TAG, "doInBackground: " + item.getInput());
            if (!item.getLeftExpr().toLowerCase().contains("x") &&
                    !item.getRightExpr().toLowerCase().contains("x")) {
                return getString(R.string.not_variable) + "</br>";
            }
            if (mEvaluator.isSyntaxError(item.getLeftExpr())) {
                return "$$" + mEvaluator.getError(item.getLeftExpr()) + "$$";
            }

            if (mEvaluator.isSyntaxError(item.getRightExpr())) {
                return "$$" + mEvaluator.getError(item.getRightExpr()) + "$$";
            }

            String expr = item.getInput();
            final String[] res = {""};
            mEvaluator.solveEquation(expr, new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = result;
                }
            });
            return res[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
            mMathView.setText("");
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            mMathView.setText(s);
            mProgress.setVisibility(View.GONE);
        }

    }

    /**
     * class for eval extend AsyncTask
     * <p>
     * doInBackground: progress eval;
     * <p>
     * onPreExecute: hide keyboard, set math view empty text, show process bar
     * <p>
     * onPostExecute: hide process bar, set mResult to math view
     */
    protected class ATaskEval extends AsyncTask<IExprInput, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(BasicCalculatorActivity.this, "Progressing...", Toast.LENGTH_SHORT).show();
            //show math view with animate
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                animateRipple(mAnimateSolve, null, -2, new AnimationFinishedListener() {
//                    @Override
//                    public void onAnimationFinished() {
//
//                    }
//                }, true);   } else {
//                mContainerSolve.setVisibility(View.VISIBLE);
//                setInputState(EInputState.RESULT_VIEW);
//                mProgress.setVisibility(View.VISIBLE);
//                mFabClose.hide();
            }
            mContainerSolve.setVisibility(View.VISIBLE);
            setInputState(EInputState.RESULT_VIEW);
            mProgress.show();
            mFabClose.hide();
        }

        @Override
        protected String doInBackground(IExprInput... params) {
            IExprInput item = params[0];
            String expr = item.getInput();
            final String[] res = {""};
            if (params[0].getClass().equals(NumberIntegerItem.class)) {
                mEvaluator.factorPrime(expr, new LogicEvaluator.EvaluateCallback() {
                    @Override
                    public void onEvaluate(String expr, String result, int errorResourceId) {
                        res[0] = result;
                    }
                });
            } else if (params[0].getClass().equals(SolveItem.class)) {
                mEvaluator.solveEquation(expr, new LogicEvaluator.EvaluateCallback() {
                    @Override
                    public void onEvaluate(String expr, String result, int errorResourceId) {
                        res[0] = result;
                    }
                });
            } else if (params[0].getClass().equals(DerivativeItem.class)) {
                mEvaluator.derivativeFunction(expr, new LogicEvaluator.EvaluateCallback() {
                    @Override
                    public void onEvaluate(String expr, String result, int errorResourceId) {
                        res[0] = result;
                    }
                });
            } else {
                mEvaluator.evaluateWithResultAsTex(expr, new LogicEvaluator.EvaluateCallback() {
                    @Override
                    public void onEvaluate(String expr, String result, int errorResourceId) {
                        res[0] = result;
                    }
                });
            }
            return res[0];
        }


        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: done " + s);
            mMathView.setText(s);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgress.hide();
                    mFabClose.show();
                    Toast.makeText(BasicCalculatorActivity.this, "OK", Toast.LENGTH_SHORT).show();
                }
            }, 500);
        }
    }
}

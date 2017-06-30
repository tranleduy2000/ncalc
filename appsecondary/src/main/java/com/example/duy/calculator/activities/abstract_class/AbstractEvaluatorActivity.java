package com.example.duy.calculator.activities.abstract_class;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.duy.calculator.R;
import com.example.duy.calculator.adapters.ResultAdapter;
import com.example.duy.calculator.hand_write.CalcHandWriteCallback;
import com.example.duy.calculator.hand_write.HandWriteManager;
import com.example.duy.calculator.history.HistoryEntry;
import com.example.duy.calculator.item_math_type.IExprInput;
import com.example.duy.calculator.item_math_type.ItemResult;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.view.AnimationFinishedListener;
import com.example.duy.calculator.view.FunctionRecyclerView;
import com.example.duy.calculator.view.ResizingEditText;
import com.example.duy.calculator.view.RevealView;
import com.myscript.atk.math.widget.MathWidget;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.btn_solve)
    protected Button btnSolve;
    @BindView(R.id.edit_input)
    protected ResizingEditText mInputDisplay;
    //    @BindView(R.id.math_view)
//    protected MathView mMathView;
    @BindView(R.id.the_clear_animation)
    protected ViewGroup mDisplayForeground;
    @BindView(R.id.progress_bar)
    protected ContentLoadingProgressBar mProgress;
    @BindView(R.id.spinner)
    protected AppCompatSpinner mSpinner;
    @BindView(R.id.btn_clear)
    protected Button btnClear;
    @BindView(R.id.edit_params)
    protected EditText editParam;
    @BindView(R.id.fab)
    protected FloatingActionButton mFull;
    /**
     * for evaluator
     */
    protected EditText editFrom, editTo;
    protected LinearLayout mLayoutLimit;
    protected SharedPreferences mPreferences;
    @BindView(R.id.fun_rc)
    protected FunctionRecyclerView mFunctionRecyclerView;

    @BindView(R.id.edit_input_2)
    protected ResizingEditText mInputDisplay2;
    @BindView(R.id.hint_1)
    protected TextInputLayout mHint1;
    @BindView(R.id.hint_2)
    protected TextInputLayout mHint2;
    protected Handler handler = new Handler();
    @BindView(R.id.rc_result)
    RecyclerView rcResult;

    // TODO: 23-Feb-17 View for hand write
    private MathWidget mMathWidget; //math widget
    private SlidingUpPanelLayout mSlidingUpPanel; //slide for scroll mathWidget
    private FloatingActionButton btnHelp; //
    private CalcHandWriteCallback mCallback; //hand write callback

    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_equation);
        ButterKnife.bind(this);
        initView();
        createData();
        setupHandPad();
    }

    @Override
    public void onBackPressed() {
        if (mSlidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                || mSlidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING) {
            mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
    }


    private void initView() {
//        mMathView.setText(getString(getIdStringHelp()));
        btnClear.setOnClickListener(this);
        btnSolve.setOnClickListener(this);
        mProgress.hide();
        mFull.setOnClickListener(this);
        btnHelp = (FloatingActionButton) findViewById(R.id.fab_help);
        btnHelp.setOnClickListener(this);
        editFrom = (EditText) findViewById(R.id.edit_lower);
        editTo = (EditText) findViewById(R.id.edit_upper);
        mLayoutLimit = (LinearLayout) findViewById(R.id.layout_limit);
        mLayoutLimit.setVisibility(View.GONE);
        mInputDisplay.setOnKeyListener(mFormulaOnKeyListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        rcResult.setHasFixedSize(true);
        rcResult.setLayoutManager(linearLayoutManager);
        resultAdapter = new ResultAdapter(this);
        rcResult.setAdapter(resultAdapter);
    }

    private void createData() {
        //init share preferences
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

//    public void onResult(String command) {
//        this.onResult(command, true);
//    }

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
                play(alphaAnimator);
            }
        });
        play(revealAnimator);
    }


//    public void onResult(String command, boolean b) {
//        Tokenizer mTokenizer = new Tokenizer(this);
//        Log.d(TAG, "onSpeechResult: " + command);
//        /** if (b) onAnimate();*/
//        //check input empty
//        if (command.isEmpty()) {
//            mInputDisplay.setError(getString(R.string.not_input));
//            mInputDisplay.requestFocus();
//            return;
//        }
//
//        //translate to en
//        final String finalCommand = mTokenizer.getNormalExpression(command);
//        hideKeyboard(mInputDisplay);
//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgress.show();
//                            btnSolve.setEnabled(false);
//                        }
//                    });
//                    mEvaluator.getEvalUtils().evaluate(finalCommand);    //catch error
//                    mEvaluator.setFraction(true);
//
//                    mEvaluator.evaluateWithResultAsTex(finalCommand, new LogicEvaluator.EvaluateCallback() {
//                        @Override
//                        public void onEvaluate(String expr, String result, final int errorResourceId) {
//                            final String finalResult = result;
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (errorResourceId == LogicEvaluator.RESULT_OK) {
//                                        mMathView.setText(finalResult);
//                                    } else {
//                                        String res = "<h3>" + getString(R.string.error) + "</h3>"
//                                                + finalResult;
//                                        mMathView.setText(res);
//                                    }
//                                    btnSolve.setEnabled(true);
//                                    mProgress.hide();
//                                }
//                            });
//                        }
//                    });
//
//                } catch (final Exception e) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            String er = mEvaluator.getExceptionMessage(e, true);
//                            mMathView.setText("\\(" + er + "\\)");
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();
//    }


    /**
     * show dialog with title and messenger
     *
     * @param title - title
     * @param msg   - messenger
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
            case R.id.fab:
                expandResult();
                break;
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
        mInputDisplay.setText("");
        if (editFrom.isShown() && editFrom.isEnabled()) editFrom.setText("");
        if (editTo.isShown()) editTo.setText("");
        mInputDisplay2.setText("");
    }


    /**
     * show #MathViewActivity with webview
     * fix bug for android 6, 7
     */
    protected void expandResult() {
        //start activity to show mResult
        /*
        Intent intent = new Intent(this, MathViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MathViewActivity.DATA, mMathView.getText());
        intent.putExtra(MathViewActivity.DATA, bundle);
        this.startActivity(intent);
        */
        //ok
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
        mInputDisplay.insert(text);
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
     * set up math widget
     */
    private void setupHandPad() {

        mSlidingUpPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mMathWidget = (MathWidget) mSlidingUpPanel.findViewById(R.id.hand_pad);
        mCallback = new CalcHandWriteCallback();

        boolean success = HandWriteManager.initHandWrite(mMathWidget, this, mCallback, false);
        if (!success) {
            Toast.makeText(this, "Not support hand pad!", Toast.LENGTH_SHORT).show();
            //can not use hand pad
            mSlidingUpPanel.setTouchEnabled(false);
            return;
        } else {
            mSlidingUpPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {

                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    switch (newState) {
                        case EXPANDED:
                        case DRAGGING:
                            //hide fab
                            if (btnHelp.isShown()) btnHelp.hide();
                            //set background
                            mFull.setImageResource(R.drawable.ic_check_circle_white_24dp);
                            //event for click fab
                            mFull.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String res = mMathWidget.getResultAsText().
                                            replace("[", "(")
                                            .replace("]", ")")
                                            .replace("{", "(")
                                            .replace("}", ")");
                                    Log.d(TAG, "onClick: " + res);
                                    //set text for display view
                                    mInputDisplay.setText(res);
                                    //close panel
                                    mSlidingUpPanel.setPanelState(
                                            SlidingUpPanelLayout.PanelState.COLLAPSED);
                                }
                            });
                            break;
                        case COLLAPSED:
                            btnHelp.show();
                            mFull.setImageResource(R.drawable.ic_fullscreen_white_24dp);
                            mFull.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    expandResult();
                                }
                            });
                            break;
                    }
                }
            });
        }

        mSlidingUpPanel.setScrollableView(mMathWidget);
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
            hideKeyboard(mInputDisplay);
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
                public void onEvaluate(String expr, String result, int errorResourceId) {
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
                    resultAdapter.addItem(new HistoryEntry("", s.mResult));
                    if (resultAdapter.getItemCount() > 0)
                        rcResult.scrollToPosition(0);
                }
            }, 300);
        }

    }

}

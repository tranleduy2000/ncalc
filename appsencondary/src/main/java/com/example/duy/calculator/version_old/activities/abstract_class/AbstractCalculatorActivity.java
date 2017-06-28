package com.example.duy.calculator.version_old.activities.abstract_class;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.duy.calculator.ICalculator;
import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.DecimalFactory;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.voice.MathVoiceManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import net.gotev.speech.ui.SpeechProgressView;

import java.util.List;

import rx.functions.Action1;

/**
 * abstract_class app
 * <p/>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractCalculatorActivity extends AbstractNavDrawerActionBarActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, ICalculator, MathVoiceManager.MathVoiceCallback {
    public Tokenizer mTokenizer;
    protected SpeechProgressView speechProgress;
    private boolean debug = ConfigApp.DEBUG;
    private MathVoiceManager mMathVoiceManager;


    /**
     * insert text to display - not clear screen
     * <p/>
     * use only for calculator (base, science, complex)
     *
     * @param s - text
     */
    public abstract void insertText(String s);

    /**
     * insert operator to display
     * not clear display
     *
     * @param s - operator
     */
    public abstract void insertOperator(String s);

    /**
     * get text input display
     * <p/>
     * use only for calculator (base, science, complex)
     *
     * @return - string text input
     */
    public abstract String getTextClean();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //define variable
        mTokenizer = new Tokenizer(this);

        mMathVoiceManager = new MathVoiceManager(this);
        mMathVoiceManager.setCallback(this);

    }


    public abstract void setTextDisplay(String text);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MathVoiceManager.onDestroy();
    }

    protected void setUpVoice() {
        speechProgress = (SpeechProgressView) findViewById(R.id.speech_progress);
        if (speechProgress == null) {
            Log.e(TAG, "setUpVoice: can not init speech input, please add SpeedProgressView to layout");
            return;
        }
        int[] colors = {
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.purple),
                ContextCompat.getColor(this, R.color.indigo),
                ContextCompat.getColor(this, R.color.teal),
                ContextCompat.getColor(this, R.color.lime)
        };

        //set color for progress
        speechProgress.setColors(colors);
    }

    /**
     * check permission android record audio
     */
    protected void startVoiceInput() {
        if (MathVoiceManager.isRunning()) {
            MathVoiceManager.stop();
        } else {
            RxPermissions.getInstance(this)
                    .request(Manifest.permission.RECORD_AUDIO)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            if (granted) { // Always true pre-M
                                onRecordAudioPermissionGranted();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        R.string.permission_required, Toast.LENGTH_LONG);
                            }
                        }
                    });
        }
    }

    /**
     * start google voice
     */

    private void onRecordAudioPermissionGranted() {
        speechProgress = (SpeechProgressView) findViewById(R.id.speech_progress);
        if (speechProgress == null) {
            Log.e(TAG, "setUpVoice: can not init speech input, please add SpeedProgressView to layout");
            return;
        }
        speechProgress.setVisibility(View.VISIBLE);
        mMathVoiceManager.startInput(speechProgress);

    }

    @Override
    public void onSpeechResult(String result) {
        setTextDisplay(result);
        if (!BigEvaluator.newInstance(this).isSyntaxError(result)) {
            String res = processResult(result);
            MathVoiceManager.Say(res);

        }
        if (speechProgress != null)
            speechProgress.post(new Runnable() {
                @Override
                public void run() {
                    speechProgress.setVisibility(View.GONE);
                }
            });
    }

    /**
     * convert result for "speech"
     *
     * @param result
     * @return
     */
    private String processResult(String result) {
        String res = BigEvaluator.newInstance(this).evaluateWithResultNormal(result);
        try {
            if (BigEvaluator.newInstance(this).isNumber(res)) {
                Log.i(TAG, "processResult: " + DecimalFactory.round(res, 3));
                return "Câu trả lời là " + DecimalFactory.round(res, 3);
            } else {
                return "Câu trả lời là: " + res;
            }
        } catch (Exception e) {
            return "Câu trả lời là: " + res;
        }
    }


    @Override
    public void onSpeechPartialResults(List<String> results, String finalResult) {
        setTextDisplay(finalResult);
    }
}

package com.example.duy.calculator.version_new_error;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;
import com.example.duy.calculator.data.CalculatorSetting;
import com.example.duy.calculator.data.Database;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.view.AnimationFinishedListener;

import java.util.Locale;

/**
 * Abstract Fragment
 * Created by DUy on 09-Jan-17.
 */

public abstract class AbstractFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected Activity mContext;

    /**
     * translator and evaluator
     */
    protected Tokenizer mTokenizer;

    /**
     * data for com.duy.calculator
     */
    protected Database mDatabase;
    protected SharedPreferences mPreferences;
    protected CalculatorSetting mSetting;
    /**
     * layout screen
     */
    protected View mViewGroup;


    @Override
    public void onAttach(Context acontext) {
        super.onAttach(acontext);
        this.mContext = getActivity();

        mDatabase = new Database(mContext);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSetting = new CalculatorSetting(mPreferences, mContext);

        mTokenizer = new Tokenizer(mContext);


    }

    @Override
    public void onResume() {
        super.onResume();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        setModeFraction();
    }

    /**
     * set mode fraction for evaluate and handle on click view change fraction
     */
    protected void setModeFraction() {
        try {
            BigEvaluator.getInstance(getActivity()).setFraction(mSetting.useFraction());
            SwitchCompat switchCompat = (SwitchCompat) getActivity().findViewById(R.id.sw_fraction);
            switchCompat.setChecked(mSetting.useFraction());
            BigEvaluator.getInstance(getActivity()).setFraction(mSetting.useFraction());
            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked != mSetting.useFraction()) {
                        mSetting.setFraction(isChecked);
                        BigEvaluator.getInstance(getActivity()).setFraction(isChecked);
                    }
                    onChangeModeFraction();
                }
            });
            switchCompat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(mContext, R.string.fraction_decs, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewGroup = getView(inflater, container);
        return mViewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * get view group
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View getView(LayoutInflater inflater, ViewGroup container);

    /**
     * handle switch fraction click
     */
    protected abstract void onChangeModeFraction();

    /**
     * hide key board view
     *
     * @param editText - EditText
     */
    protected void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * find and return view match id
     *
     * @param id - id view
     * @return - View
     */
    public View findViewById(int id) {
        return mViewGroup.findViewById(id);
    }


    /**
     * play animator
     *
     * @param animator - animator object
     */
    protected void play(Animator animator) {
        animator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
            }
        });
        animator.start();
    }

    /**
     * show dialog with title and messenger
     *
     * @param title - title
     * @param msg   - messenger
     */
    protected void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(mContext.getString(R.string.fraction_mode))) {
            setModeFraction();
        } else if (s.equals(getResources().getString(R.string.key_pref_theme))) {
            DLog.i("Fragment: set theme ");
            Toast.makeText(mContext, "Restart app to apply theme!", Toast.LENGTH_SHORT).show();
        } else if (s.equals(getString(R.string.key_pref_lang))) {
            setLocale(true);
            DLog.i("Fragment: set language ");

            Toast.makeText(mContext, "Restart app to apply language!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * set language
     *
     * @param create
     */
    private void setLocale(boolean create) {
        Locale locale;
        String code = mPreferences.getString(getString(R.string.key_pref_lang), "default_lang");
        if (code.equals("default_lang")) {
            DLog.i("setLocale: default");
//            locale = Locale.getDefault();
            return;
        } else {
            locale = new Locale(code);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
//        if (create) this.recreate();
    }
}

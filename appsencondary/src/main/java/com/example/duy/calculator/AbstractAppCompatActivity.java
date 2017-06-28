package com.example.duy.calculator;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duy.calculator.data.CalculatorSetting;
import com.example.duy.calculator.data.Database;
import com.example.duy.calculator.userinterface.ThemeEngine;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.view.AnimationFinishedListener;

import java.util.Locale;


/**
 * abstract_class theme for app
 * <p>
 * auto set theme when user changed theme
 * <p>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractAppCompatActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected SharedPreferences mPreferences;
    protected String TAG = AbstractAppCompatActivity.class.getName();
    protected Database mHistoryDatabase;
    protected CalculatorSetting mSetting;


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
     * get mSetting
     *
     * @return - preference
     */
    public CalculatorSetting getSetting() {
        return mSetting;
    }

    /**
     * set theme and init mHistoryDatabase for history
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setLocale(false);

        //set theme for app
        setTheme(false);

        mHistoryDatabase = new Database(this);
        mSetting = new CalculatorSetting(this);
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
            Log.d(TAG, "setLocale: default");
            locale = Locale.getDefault();
        } else {
            locale = new Locale(code);

        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        if (create) recreate();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPreferences != null)
            mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * set theme for app
     *
     * @param recreate -call method onCreate
     */
    protected void setTheme(boolean recreate) {
        String name = mPreferences.getString(getResources().getString(R.string.key_pref_theme), "");
        ThemeEngine themeEngine = new ThemeEngine(getApplicationContext());
        int themeId = themeEngine.getTheme(name);
        if (themeId != ThemeEngine.THEME_NOT_FOUND) {
            super.setTheme(themeId);
            if (recreate) recreate();
            Log.d(TAG, "Set theme ok");
        } else {
            Log.d(TAG, "Theme not found");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getResources().getString(R.string.key_pref_theme))) {
            setTheme(true);
            DLog.i("Main: set theme ");
        } else if (s.equals(getString(R.string.key_pref_lang))) {
            setLocale(true);
            DLog.i("Main: set language ");
            Toast.makeText(this, getString(R.string.change_lang_msg), Toast.LENGTH_SHORT).show();
        } else if (s.equals(getString(R.string.key_pref_font))) {
            recreate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreferences != null)
            mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * show dialog choose email client
     * send mail
     */
    protected void sendMail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.dev_mail)});
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    /**
     * share app
     */
    protected void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.duy.calculator.free");
        intent.setType("text/plain");
        startActivity(intent);
    }

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

    /**
     * show dialog with title and messenger
     *
     * @param msg - messenger
     */
    protected void showDialog(String msg) {
        this.showDialog("", msg);
    }


    public void showDialogUpdate() {
        boolean b = mSetting.isUpdate(this);
        if (b || ConfigApp.DEBUG) {
            showDialog(getString(R.string.what_new), getString(R.string.update_new));
            mSetting.setUpdate(true);
        }
    }

    public void rateApp() {
        Uri uri = Uri.parse("market://details?id=com.duy.calculator.free");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |

                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.duy.calculator.free")));
        }
    }

    protected void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}

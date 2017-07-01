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

package com.duy.calculator.activities.abstract_class;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.calculator.BuildConfig;
import com.duy.calculator.R;
import com.duy.calculator.data.CalculatorSetting;
import com.duy.calculator.data.Database;
import com.duy.calculator.notify.CheckUpdateTask;
import com.duy.calculator.userinterface.FontManager;
import com.duy.calculator.userinterface.ThemeEngine;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.view.AnimationFinishedListener;
import com.kobakei.ratethisapp.RateThisApp;

import java.util.Locale;


/**
 * abstract theme for app
 * <p>
 * auto set theme when user changed theme
 * <p>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractAppCompatActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "MainActivity";
    protected CalculatorSetting mCalculatorSetting;
    protected Database mHistoryDatabase;
    protected CalculatorSetting mSetting;


    /**
     * playAnimatior animator
     *
     * @param animator - animator object
     */
    protected void playAnimatior(Animator animator) {
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
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCalculatorSetting = new CalculatorSetting(this);
        mHistoryDatabase = new Database(this);
        mSetting = new CalculatorSetting(this);

        setLocale(false);
        setTheme(false);  //set theme for app
        setFullScreen();

    }

    private void setFullScreen() {
        if (mSetting.useFullScreen()) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
    }

    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void hideStatusBar() {
        // TODO: 30-Mar-17
        if (android.os.Build.VERSION.SDK_INT < 30) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * set language
     */
    private void setLocale(boolean create) {
        Locale locale;
        String code = mCalculatorSetting.getString(getString(R.string.key_pref_lang), "default_lang");
        if (code.equals("default_lang")) {
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
        if (mCalculatorSetting != null)
            mCalculatorSetting.registerOnSharedPreferenceChangeListener(this);
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                rateApp();
            }

            @Override
            public void onNoClicked() {
            }

            @Override
            public void onCancelClicked() {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * set theme for app
     *
     * @param recreate - call method onCreate
     */
    protected void setTheme(boolean recreate) {
        String name = mCalculatorSetting.getString(getResources().getString(R.string.key_pref_theme), "");
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
        Log.d(TAG, "onSharedPreferenceChanged: " + s);
        if (s.equalsIgnoreCase(getResources().getString(R.string.key_pref_theme))) {
            setTheme(true);
        } else if (s.equalsIgnoreCase(getString(R.string.key_pref_lang))) {
            setLocale(true);
            Toast.makeText(this, getString(R.string.change_lang_msg), Toast.LENGTH_SHORT).show();
        } else if (s.equalsIgnoreCase(getString(R.string.key_pref_font))) {
            //reload type face
            FontManager.loadTypefaceFromAsset(this);
            recreate();
        } else if (s.equalsIgnoreCase(getString(R.string.key_hide_status_bar))) {
            setFullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCalculatorSetting != null)
            mCalculatorSetting.unregisterOnSharedPreferenceChangeListener(this);
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
        intent.putExtra(Intent.EXTRA_TEXT, "http://playAnimatior.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
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
//            showDialog(getString(R.string.what_new), getString(R.string.update_new));
//            mSetting.setUpdate(true);
        }
    }

    public void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://playAnimatior.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    protected void hideKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } else {
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected void hideKeyboard() {

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void checkUpdate() {
        boolean update;
        update = mCalculatorSetting.getBoolean(CalculatorSetting.NOTIFY_UPDATE, false);
        if (update) {
            new CheckUpdateTask(this).execute();
        }
    }
}

package com.example.duy.calculator.version_old.activities.abstract_class;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.duy.calculator.ICalculator;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.utils.ConfigApp;

/**
 * abstract app
 * <p/>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractCalculatorActivity extends AbstractNavDrawerActionBarActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, ICalculator {
    public Tokenizer mTokenizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTokenizer = new Tokenizer(this);
    }

    private boolean debug = ConfigApp.DEBUG;

    /**
     * insert text to display - not clear screen
     * <p/>
     * use only for org.solovyev.android.com.duy.calculator (base, science, complex)
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
     * use only for org.solovyev.android.com.duy.calculator (base, science, complex)
     *
     * @return - string text input
     */
    public abstract String getTextClean();


    /**
     * clear screen
     * <p/>
     * set text display - use only for org.solovyev.android.com.duy.calculator (base, science, complex)
     * <p/>
     * set text for input edit text (eval)
     * <p/>
     * set text for matrix (matrix cal)
     * <p/>
     * set text for linear eval
     *
     * @param text - string input
     */
    public abstract void setTextDisplay(String text);


}

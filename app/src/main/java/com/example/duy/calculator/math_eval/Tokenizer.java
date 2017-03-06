package com.example.duy.calculator.math_eval;

import android.content.Context;

import com.example.duy.calculator.R;

import java.util.ArrayList;

/**
 * class used translate language
 * <p>
 * calculated -> english -> translate -> language -> show view
 * <p>
 * view -> translate -> english -> calculate
 */
public class Tokenizer {
    private final Context mContext;
    private final ArrayList<Localize> mReplacements;
    private String TAG = Tokenizer.class.getName();

    public Tokenizer(Context context) {
        mContext = context;
        mReplacements = new ArrayList<>();
    }

    private void generateReplacements(Context context) {
        mReplacements.clear();
        mReplacements.add(new Localize("/", context.getString(R.string.div)));
        mReplacements.add(new Localize("*", context.getString(R.string.mul)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus2)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus3)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus4)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus5)));
        mReplacements.add(new Localize("-", context.getString(R.string.minus)));

        mReplacements.add(new Localize("cbrt", context.getString(R.string.op_cbrt)));

        mReplacements.add(new Localize("sin", context.getString(R.string.sin)));
        mReplacements.add(new Localize("cos", context.getString(R.string.cos)));
        mReplacements.add(new Localize("tan", context.getString(R.string.tan)));
        mReplacements.add(new Localize("arcsin", context.getString(R.string.arcsin)));
        mReplacements.add(new Localize("arccos", context.getString(R.string.arccos)));
        mReplacements.add(new Localize("arctan", context.getString(R.string.arctan)));
        mReplacements.add(new Localize("arcsinh", context.getString(R.string.arcsinh)));
        mReplacements.add(new Localize("arccosh", context.getString(R.string.arccosh)));
        mReplacements.add(new Localize("arctanh", context.getString(R.string.arctanh)));
        mReplacements.add(new Localize("ln", context.getString(R.string.fun_ln)));
        mReplacements.add(new Localize("log", context.getString(R.string.lg)));
        mReplacements.add(new Localize("exp", context.getString(R.string.exp)));
        mReplacements.add(new Localize("infinity", Character.toString(Constants.INFINITY_UNICODE)));
        mReplacements.add(new Localize("sqrt", context.getString(R.string.sqrt_sym)));
        mReplacements.add(new Localize("ceiling", context.getString(R.string.ceil)));
        mReplacements.add(new Localize("floor", context.getString(R.string.floor)));

        mReplacements.add(new Localize("<=", context.getString(R.string.leq)));
        mReplacements.add(new Localize(">=", context.getString(R.string.geq)));
        mReplacements.add(new Localize("!=", context.getString(R.string.neq)));

        mReplacements.add(new Localize("(pi)", context.getString(R.string.pi_symbol)));
        mReplacements.add(new Localize("(degree)", context.getString(R.string.degree_sym)));

        //re translate
        mReplacements.add(new Localize("pi", context.getString(R.string.pi_symbol)));
        mReplacements.add(new Localize("degree", context.getString(R.string.degree_sym)));

    }

    /**
     * translate to english
     * @param expr - local language
     * @return - english
     */
    public String getNormalExpression(String expr) {
        generateReplacements(mContext);
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.local, replacement.english);
        }
        return expr;
    }

    /**
     * translate to local language
     * @param expr - english
     * @return - local language
     */
    public String getLocalizedExpression(String expr) {
        generateReplacements(mContext);
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.english, replacement.local);
        }
        return expr;
    }

    public Context getContext() {
        return mContext;
    }

    private class Localize {
        String english;
        String local;

        Localize(String english, String local) {
            this.english = english;
            this.local = local;
        }
    }
}
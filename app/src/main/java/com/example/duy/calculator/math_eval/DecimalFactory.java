package com.example.duy.calculator.math_eval;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by DUy on 21-Jan-17.
 */

public class DecimalFactory {

    public static DecimalFormat format;

    static {
        format = new DecimalFormat("#.#####");
        format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
    }

    private final BigEvaluator mEvaluator;

    /**
     * Use format decimal separator
     */

    public DecimalFactory(BigEvaluator mEvaluator) {
        this.mEvaluator = mEvaluator;
    }

    /**
     * round decimal value
     *
     * @param val - input
     * @param i   - number of decimal
     * @return - String
     */
    public static String round(double val, int i) {
        java.math.BigDecimal bigDecimal = new java.math.BigDecimal(val);
        String res = bigDecimal.round(new MathContext(i + 1)).toString();
        return res;
    }

    /**
     * round decimal value
     *
     * @param val - input
     * @param i   - number of decimal
     * @return - String
     */
    public static String round(String val, int i) {
        try {
//            String p = "#.";
//            for (int j = 0; j < i; j++) {
//                p += "#";
//            }
//            format = new DecimalFormat(p);
            java.math.BigDecimal bigDecimal = new java.math.BigDecimal(val);
            String res = bigDecimal.round(new MathContext(i)).toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return val;
        }
    }

    /**
     * true if is a number
     *
     * @param value
     * @return
     */
    public boolean isNumber(String value) {
        try {
            boolean res = Boolean.valueOf(String.valueOf(mEvaluator.evaluateWithResultNormal("NumberQ(" + value + ")")));
            //     Log.d(TAG, "isNumber: " + value + "  - " + res);
            return res;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * convert fraction to real number
     *
     * @param result1
     * @return
     */
    private String convertToReal(String result1) {
        try {
            result1 = "N(" + result1 + ")";
            final String[] res = {""};
            mEvaluator.evaluateWithResultNormal(result1, new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    res[0] = result;
                }
            });
            return res[0];
        } catch (Exception e) {
            return "";
        }

    }

}

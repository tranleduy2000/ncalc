/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.evaluator;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Duy on 21-Jan-17.
 */

public class DecimalFactory {

    public static DecimalFormat format;

    static {
        format = new DecimalFormat("#.#####");
        format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
    }

    private final MathEvaluator mEvaluator;

    /**
     * Use format decimal separator
     */

    public DecimalFactory(MathEvaluator mEvaluator) {
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
            java.math.BigDecimal bigDecimal = new java.math.BigDecimal(val);
            String res = bigDecimal.round(new MathContext(i)).toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return val;
        }
    }



}

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

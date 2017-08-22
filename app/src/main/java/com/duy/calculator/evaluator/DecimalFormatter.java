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


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Duy on 14-Jul-17.
 */

public class DecimalFormatter {

    public static String round(String value, int places) {
        try {
            String pattern = "0.";
            if (places == 0) pattern += "#";
            else {
                for (int i = 0; i < places; i++) {
                    pattern += "#";
                }
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
            return decimalFormat.format(new BigDecimal(value));
        } catch (Exception e) { //complex number
            return value;
        }
    }
}

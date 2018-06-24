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

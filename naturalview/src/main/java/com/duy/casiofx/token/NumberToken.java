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

package com.duy.casiofx.token;

import com.duy.casiofx.util.Utility;

import java.io.Serializable;

/**
 * This is the class for the main activity (entry-point to the app). It will simply configure
 * the setting then go to the basic activity.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class NumberToken extends Token implements Serializable {

    private static final long serialVersionUID = 752647223;
    public static int roundTo = 9;
    private double value;

    /**
     * Only to be used during expression simplification for the purpose of efficiency. Stores
     * multiple digits into a single piece with the total value of the digits stored.
     */
    public NumberToken(double value) {
        super(null); //No image for this; it should never be shown on screen
        this.value = value;
        this.type = -1;
    }

    /**
     * @return The value of the number
     */
    public double getValue() {
        return value;
    }

    public String getSymbol() {
        if (Double.isInfinite(value) || Double.isNaN(value)){
            return ("A Really Big Number");
        }
        //Rounds
        value = Utility.round(value, roundTo);
        String s = Double.toString(value);
        s = !s.contains(".") ? s : (s.indexOf("E") > 0 ? s.substring(0, s.indexOf("E")).replaceAll("0*$", "")
                .replaceAll("\\.$", "").concat(s.substring(s.indexOf("E"))) : s.replaceAll("0*$", "")
                .replaceAll("\\.$", "")); //Removes trailing zeroes
        return s;
    }

    public String toLaTeX() {
        return "$" + getSymbol() + "$";
    }
}
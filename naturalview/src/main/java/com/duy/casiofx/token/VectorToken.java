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
 * Represents a Vector for Vector Mode.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class VectorToken extends Token implements Serializable {
    private double[] values;

    public VectorToken(double[] values) {
        super(null);
        this.values = values;
    }

    public double[] getValues() {
        return values;
    }

    public int getDimensions() {
        return values.length;
    }

    /**
     * Determines whats gets displayed onto the Screen.
     *
     * @return The visual representation of the Vector
     */
    public String getSymbol() {
        String s = "[";
        for (int i = 0; i < values.length; i++) {
            if (i != 0) {
                s += ", ";
            }
            double value = Utility.round(values[i], NumberToken.roundTo);
            String string = Double.toString(value);
            string = !string.contains(".") ? string : (string.indexOf("E") > 0 ? string.substring(0, string.indexOf("E")).replaceAll("0*$", "")
                    .replaceAll("\\.$", "").concat(string.substring(string.indexOf("E"))) : string.replaceAll("0*$", "")
                    .replaceAll("\\.$", "")); //Removes trailing zeroes
            s += string;
        }
        s += "]";
        return s;
    }

    public String getLaTeX() {
        String output = "$" + "\\begin{bmatrix}";
        for (int i = 0; i < values.length; i++) {
            output += (new Double(values[i])).toString();
            output += "\\\\";
        }
        output += "\\end{bmatrix}" + "$";
        return output;
    }

}
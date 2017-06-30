/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.token;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * This is the class for the main activity (entry-point to the app). It will simply configure
 * the setting then go to the basic activity.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class NumberToken extends Token implements Serializable {

    private String value;

    /**
     * Only to be used during expression simplification for the purpose of efficiency. Stores
     * multiple digits into a single piece with the total value of the digits stored.
     */
    public NumberToken(@NonNull Object value) {
        super(null); //No image for this; it should never be shown on screen
        this.value = value.toString();
        this.type = -1;
        this.precedence = NUMBER_PRECEDENCE;
    }

    /**
     * @return The value of the number
     */
    public double getDoubleValue() {
        return Double.parseDouble(value);
    }

    public String getValue() {
        return value;
    }

    public String getSymbol() {
        return value;
    }

    public String toLaTeX() {
        return "$" + getSymbol() + "$";
    }

    @Override
    public int getPrecedence() {
        return NUMBER_PRECEDENCE;
    }

    @Override
    public String toString() {
        return value;
    }
}
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

import java.io.Serializable;

/**
 * Represents a single Digit of a number.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class DigitToken extends Token implements Serializable {
    private static final long serialVersionUID = 752647221;
    private int value;

    /**
     * Should not be used outside of a factory; to create a type of digit,
     * see class DigitFactory.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     * @param value  The value of the Digit (0 - 9) or as defined by DigitFactory class constants
     */
    public DigitToken(String symbol, int value) {
        super(symbol);
        this.value = value;
    }

    /**
     * @return The value of the Digit (0 - 9) or as defined by DigitFactory class constants
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}

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

package com.duy.casiofx.factory;

import com.duy.casiofx.token.DigitToken;

/**
 * Contains static methods that will create Digit pieces.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class DigitFactory {

    public static int DECIMAL = -1, NEGATIVE = -10;

    public static DigitToken makeOne() {
        return new DigitToken("1", 1);
    }

    public static DigitToken makeTwo() {
        return new DigitToken("2", 2);
    }

    public static DigitToken makeThree() {
        return new DigitToken("3", 3);
    }

    public static DigitToken makeFour() {
        return new DigitToken("4", 4);
    }

    public static DigitToken makeFive() {
        return new DigitToken("5", 5);
    }

    public static DigitToken makeSix() {
        return new DigitToken("6", 6);
    }

    public static DigitToken makeSeven() {
        return new DigitToken("7", 7);
    }

    public static DigitToken makeEight() {
        return new DigitToken("8", 8);
    }

    public static DigitToken makeNine() {
        return new DigitToken("9", 9);
    }

    public static DigitToken makeZero() {
        return new DigitToken("0", 0);
    }

    public static DigitToken makeDecimal() {
        return new DigitToken(".", DECIMAL);
    }

    public static DigitToken makeNegative() {
        return new DigitToken("-", NEGATIVE);
    }
}

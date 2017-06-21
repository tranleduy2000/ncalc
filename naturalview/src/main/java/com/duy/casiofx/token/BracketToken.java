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

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * When an expression is processed, the pieces within the bracket will be evaluated first. They
 * can be used alone or in conjunction with a function.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class BracketToken extends Token implements Serializable {
    //Constants
    public static final int OPEN = 1;
    public static final int CLOSE = 2;

    public static final int SQUARE_OPEN = 3;
    public static final int SQUARE_CLOSED = 4;

    public static final int SUPERSCRIPT_OPEN = 6;
    public static final int SUPERSCRIPT_CLOSE = 7;

    public static final int NUMERATOR_OPEN = 8;
    public static final int NUMERATOR_CLOSE = 9;

    public static final int DENOMINATOR_OPEN = 10;
    public static final int DENOMINATOR_CLOSE = 11;

    public static final int MAGNITUDE_OPEN = 5;
    public static final int MAGNITUDE_CLOSE = 12;

    public static final int FRACTION_OPEN = 13;
    public static final int FRACTION_CLOSE = 14;

    public static final int SQRT_OPEN = 15;
    public static final int SQRT_CLOSE = 16;

    /**
     * Should not be used outside of a factory; to create a type of bracket,
     * see class BracketFactory.
     *
     * @param symbol The symbol of the bracket.
     * @param type   The type of bracket it is
     */
    public BracketToken(String symbol, int type) {
        super(symbol);
        this.type = type;
    }

    public static boolean isClose(@NonNull BracketToken bracket) {
        return bracket.getType() == CLOSE ||
                bracket.getType() == SQUARE_CLOSED ||
                bracket.getType() == SUPERSCRIPT_CLOSE ||
                bracket.getType() == NUMERATOR_CLOSE ||
                bracket.getType() == DENOMINATOR_CLOSE ||
                bracket.getType() == FRACTION_CLOSE ||
                bracket.getType() == SQRT_CLOSE;
    }

    public static boolean isOpen(BracketToken bracket) {
        return !isClose(bracket);
    }

    public boolean isClose() {
        return isClose(this);
    }

    public boolean isOpen() {
        return isOpen(this);
    }

    /**
     * @return The type of Bracket (open or closed) this is (see class constants).
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return isOpen() ? "(" : ")";
    }
}
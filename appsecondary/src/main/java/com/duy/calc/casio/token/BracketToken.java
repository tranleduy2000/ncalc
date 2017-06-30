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
 * When an expression is processed, the pieces within the bracket will be evaluated first. They
 * can be used alone or in conjunction with a function.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class BracketToken extends Token implements Serializable {
    //Constants
    public static final int PARENTHESES_OPEN = 100;
    public static final int PARENTHESES_CLOSE = 200;

    public static final int SQUARE_OPEN = 300;
    public static final int SQUARE_CLOSED = 400;

    public static final int SUPERSCRIPT_OPEN = 600;
    public static final int SUPERSCRIPT_CLOSE = 700;

    public static final int NUMERATOR_OPEN = 800;
    public static final int NUMERATOR_CLOSE = 900;

    public static final int DENOMINATOR_OPEN = 1000;
    public static final int DENOMINATOR_CLOSE = 1100;

    public static final int MAGNITUDE_OPEN = 500;
    public static final int MAGNITUDE_CLOSE = 1200;

    public static final int FRACTION_OPEN = 1300;
    public static final int FRACTION_CLOSE = 1400;

    public static final int SQRT_OPEN = 1500;
    public static final int SQRT_CLOSE = 1600;

    public static final int ABS_OPEN = 1700;
    public static final int ABS_CLOSE = 1800;

    public static final int COMP_OPEN = 1900;
    public static final int COMP_CLOSE = 2000;

    public static final int PERM_OPEN = 2100;
    public static final int PERM_CLOSE = 2200;

    public static final int SUBSCRIPT_OPEN = 2300;
    public static final int SUBSCRIPT_CLOSE = 2400;

    public static final int DERIVATIVE_OPEN = 2500;
    public static final int DERIVATIVE_CLOSE = 2600;

    public static final int LOGN_OPEN = 2700;
    public static final int LOGN_CLOSE = 2800;

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
        int type = bracket.getType();
        return type == PARENTHESES_CLOSE || type == SQUARE_CLOSED || type == SUPERSCRIPT_CLOSE ||
                type == NUMERATOR_CLOSE || type == DENOMINATOR_CLOSE || type == FRACTION_CLOSE ||
                type == SQRT_CLOSE || type == ABS_CLOSE || type == COMP_CLOSE ||
                type == PERM_CLOSE || type == SUBSCRIPT_CLOSE || type == DERIVATIVE_CLOSE ||
                type == LOGN_CLOSE;
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
    public int getPrecedence() {
        if (isOpen()) {
            return LEFT_PARENT_PRECEDENCE;
        } else {
            return RIGHT_PARENT_PRECEDENCE;
        }
    }

    @Override
    public int getAssociative() {
        if (isOpen()) {
            return PREFIX;
        } else {
            return 0;
        }
    }

    public boolean hasRelation(BracketToken closeBracket) {
        if (closeBracket.getType() == BracketToken.PARENTHESES_CLOSE) {
            if (this.getType() != BracketToken.PARENTHESES_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.SQUARE_CLOSED) {
            if (this.getType() != BracketToken.SQUARE_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
            if (this.getType() != BracketToken.SUPERSCRIPT_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.NUMERATOR_CLOSE) {
            if (this.getType() != BracketToken.NUMERATOR_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.DENOMINATOR_CLOSE) {
            if (this.getType() != BracketToken.DENOMINATOR_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.MAGNITUDE_CLOSE) {
            if (this.getType() != BracketToken.MAGNITUDE_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.FRACTION_CLOSE) {
            if (this.getType() != BracketToken.FRACTION_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.SQRT_CLOSE) {
            if (this.getType() != BracketToken.SQRT_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.ABS_CLOSE) {
            if (this.getType() != BracketToken.ABS_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.COMP_CLOSE) {
            if (this.getType() != BracketToken.COMP_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.PERM_CLOSE) {
            if (this.getType() != BracketToken.PERM_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.SUBSCRIPT_CLOSE) {
            if (this.getType() != BracketToken.SUBSCRIPT_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.DERIVATIVE_CLOSE) {
            if (this.getType() != BracketToken.DERIVATIVE_OPEN) {
                return false;
            }
        } else if (closeBracket.getType() == BracketToken.LOGN_CLOSE) {
            if (this.getType() != BracketToken.LOGN_OPEN) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return isOpen() ? "(" : ")";
    }
}
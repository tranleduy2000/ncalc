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

package com.duy.calc.casio.token.factory;

import com.duy.calc.casio.token.BracketToken;

/**
 * Contains static methods that will create Brackets.
 *
 * @author Alston Lin, Ejaaz Merali, Jason Fok
 * @version 3.0
 */
public class BracketFactory {

    public static BracketToken makeOpenParentheses() {
        return new BracketToken("(", BracketToken.PARENTHESES_OPEN);
    }

    public static BracketToken makeCloseParentheses() {
        return new BracketToken(")", BracketToken.PARENTHESES_CLOSE);
    }

    public static BracketToken makeOpenSquareBracket() {
        return new BracketToken("[", BracketToken.SQUARE_OPEN);
    }

    public static BracketToken makeCloseSquareBracket() {
        return new BracketToken("]", BracketToken.SQUARE_CLOSED);
    }

    public static BracketToken makeMagnitudeOpen() {
        return new BracketToken("‖(", BracketToken.MAGNITUDE_OPEN);
    }

    public static BracketToken makeMagnitudeClose() {
        return new BracketToken(")‖", BracketToken.MAGNITUDE_CLOSE);
    }

    public static BracketToken makeSuperscriptOpen() {
        return new BracketToken("", BracketToken.SUPERSCRIPT_OPEN);
    }

    public static BracketToken makeSuperscriptClose() {
        return new BracketToken("", BracketToken.SUPERSCRIPT_CLOSE);
    }

    public static BracketToken makeNumeratorOpen() {
        return new BracketToken("", BracketToken.NUMERATOR_OPEN);
    }

    public static BracketToken makeNumeratorClose() {
        return new BracketToken("", BracketToken.NUMERATOR_CLOSE);
    }

    public static BracketToken makeDenominatorOpen() {
        return new BracketToken("", BracketToken.DENOMINATOR_OPEN);
    }

    public static BracketToken makeDenominatorClose() {
        return new BracketToken("", BracketToken.DENOMINATOR_CLOSE);
    }

    public static BracketToken makeFractionOpen() {
        return new BracketToken("", BracketToken.FRACTION_OPEN);
    }

    public static BracketToken makeFractionClose() {
        return new BracketToken("", BracketToken.FRACTION_CLOSE);
    }

    public static BracketToken makeSqrtOpen() {
        return new BracketToken("", BracketToken.SQRT_OPEN);
    }

    public static BracketToken makeSqrtClose() {
        return new BracketToken("", BracketToken.SQRT_CLOSE);
    }

    public static BracketToken makeAbsOpen() {
        return new BracketToken("", BracketToken.ABS_OPEN);
    }

    public static BracketToken makeAbsClose() {
        return new BracketToken("", BracketToken.ABS_CLOSE);
    }

    public static BracketToken makeCompOpen() {
        return new BracketToken("", BracketToken.COMP_OPEN);
    }

    public static BracketToken makeCompClose() {
        return new BracketToken("", BracketToken.COMP_CLOSE);
    }

    public static BracketToken makePermOpen() {
        return new BracketToken("", BracketToken.PERM_OPEN);
    }

    public static BracketToken makePermClose() {
        return new BracketToken("", BracketToken.PERM_CLOSE);
    }

    public static BracketToken makeSubscriptClose() {
        return new BracketToken("", BracketToken.SUBSCRIPT_CLOSE);
    }

    public static BracketToken makeSubscriptOpen() {
        return new BracketToken("", BracketToken.SUBSCRIPT_OPEN);
    }

    public static BracketToken makeDerivativeOpen() {
        return new BracketToken("", BracketToken.DERIVATIVE_OPEN);
    }

    public static BracketToken makeDerivativeClose() {
        return new BracketToken("", BracketToken.DERIVATIVE_CLOSE);
    }

    public static BracketToken makeLognOpen() {
        return new BracketToken("", BracketToken.LOGN_OPEN);
    }

    public static BracketToken makeLognClose() {
        return new BracketToken("", BracketToken.LOGN_CLOSE);
    }
}

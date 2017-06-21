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

import com.duy.casiofx.token.BracketToken;

/**
 * Contains static methods that will create Brackets.
 *
 * @author Alston Lin, Ejaaz Merali, Jason Fok
 * @version 3.0
 */
public class BracketFactory {

    public static BracketToken makeOpenParentheses() {
        return new BracketToken("(", BracketToken.OPEN);
    }

    public static BracketToken makeCloseParentheses() {
        return new BracketToken(")", BracketToken.CLOSE);
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

    public static BracketToken makeNumClose() {
        return new BracketToken("", BracketToken.NUMERATOR_CLOSE);
    }

    public static BracketToken makeDenominatorOpen() {
        return new BracketToken("", BracketToken.DENOMINATOR_OPEN);
    }

    public static BracketToken makeDenominatorClose() {
        return new BracketToken("", BracketToken.DENOMINATOR_CLOSE);
    }

    public static BracketToken makeFracOpen() {
        return new BracketToken("", BracketToken.FRACTION_OPEN);
    }

    public static BracketToken makeFracClose() {
        return new BracketToken("", BracketToken.FRACTION_CLOSE);
    }

    public static BracketToken makeSqrtOpen() {
        return new BracketToken("", BracketToken.SQRT_OPEN);
    }

    public static BracketToken makeSqrtClose() {
        return new BracketToken("", BracketToken.SQRT_CLOSE);
    }

}

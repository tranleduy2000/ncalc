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

import java.io.Serializable;

/**
 * Tokens that have no function at all, simply used to mark a point and add a visual.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class PlaceholderToken extends Token implements Serializable {

    public static final int SUPERSCRIPT_BLOCK = 111;
    public static final int COMMA = 211;
    public static final int BASE_BLOCK = 311;
    public static final int SUBSCRIPT_BLOCK = 411;

    /**
     * Creates a new Placeholder to be shown on the calculator screen.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     */
    public PlaceholderToken(String symbol, int type) {
        super(symbol);
        this.type = type;
    }

    /**
     * @return The type of Placeholder this is
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return symbol + "-" + type;
    }
}
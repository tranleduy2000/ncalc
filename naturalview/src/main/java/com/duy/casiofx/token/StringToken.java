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
 * A Token that holds a String for display purposes.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class StringToken extends Token implements Serializable {
    /**
     * Creates a new Token to be shown on the calculator screen.
     *
     * @param s The string to display
     */
    public StringToken(String s) {
        super(s);
    }
}

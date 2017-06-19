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

package com.duy.calculator.math_eval;

import org.matheclipse.core.eval.TeXUtilities;

import java.io.StringWriter;

/**
 * Created by DUy on 21-Jan-17.
 */

public class LaTexFactory {
    private TeXUtilities mTexEngine;

    public LaTexFactory(TeXUtilities teXUtilities) {
        this.mTexEngine = teXUtilities;
    }

    /**
     * convert expression to latex, the result is inline
     * 2x/2 -> \\( \\frac{2x}{2} \))
     * <p>
     * set text to #MathView Object to show latex
     *
     * @return String Object
     */
    public String convertToTexInline(String result) {
        StringWriter writer = new StringWriter();
        try {
            mTexEngine.toTeX(result, writer);
        } catch (Exception e) {
            return "\\(" + result + "\\)";
        }
        return "\\(" + writer.toString() + "\\)";
    }
}

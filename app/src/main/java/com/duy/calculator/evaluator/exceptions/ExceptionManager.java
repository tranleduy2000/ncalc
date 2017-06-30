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

package com.duy.calculator.evaluator.exceptions;

import android.content.Context;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.LogicEvaluator;

import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

/**
 * Created by Duy on 30-Jun-17.
 */

public class ExceptionManager {


    /**
     * get exception message
     *
     * @param e          - Exception
     * @param isFraction - mode is fraction or real,
     *                   if is mode fraction input look like 2/3 + 1/2 + error ...
     *                   else input look like N(...)
     * @return String Object
     */
    public static String getExceptionMessage(Context context, Exception e, boolean isFraction) {
        if (e instanceof SyntaxError) {
            SyntaxError syntaxError = (SyntaxError) e;
            String res = syntaxError.getCurrentLine();
            int index = syntaxError.getStartOffset();
            try {
                //set selected handler error
                if (res.length() > 0) {
                    res = res.substring(0, index) + // text before error
                            LogicEvaluator.ERROR_INDEX_STRING + "" +
                            res.substring(index, res.length()); //text after error
                } else res = "";
                //if not use fraction, remove N()
                if (!isFraction) {
                    res = res.substring(2, res.length() - 1);
                }
                return res;
            } catch (Exception ex) {
                return "";
            }
        } else if (e instanceof MathException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h3>").append(context.getString(R.string.math_error))
                    .append("</h3>").append(context.getString(R.string.reason))
                    .append("</br>").append(e.getMessage());

            return stringBuilder.toString();
        }
        return e.getMessage();
    }
}

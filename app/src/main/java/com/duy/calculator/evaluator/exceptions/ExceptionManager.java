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

import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

/**
 * Created by Duy on 30-Jun-17.
 */
public class ExceptionManager {


    /**
     * get exception message
     */
    public static String getExceptionMessage(Context context, Exception e) {
        // TODO: 30-Jun-17  uses eval config
        if (e instanceof SyntaxError) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h3>").append(context.getString(R.string.syntax_error))
                    .append("</h3>").append(context.getString(R.string.reason))
                    .append("</br>").append(e.getMessage());

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

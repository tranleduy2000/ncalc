/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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

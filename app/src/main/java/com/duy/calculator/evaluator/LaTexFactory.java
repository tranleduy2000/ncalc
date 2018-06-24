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

package com.duy.calculator.evaluator;

import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;

/**
 * Created by Duy on 01-Jul-17.
 */

public class LaTexFactory {
    /**
     * Convert result to latex to display on {@link io.github.kexanie.library.MathView}
     */
    public static String toLaTeX(IExpr result) {
        StringWriter stringWriter = new StringWriter();
        TeXUtilities texEngine = MathEvaluator.getInstance().getTexEngine();
        texEngine.toTeX(result, stringWriter);
        return "$$" + stringWriter + "$$";
    }
}

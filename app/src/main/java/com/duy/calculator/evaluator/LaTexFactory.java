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

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.form.tex.TeXFormFactory;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.parser.ExprParser;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Duy on 01-Jul-17.
 */

public class LaTexFactory {

    protected EvalEngine fEvalEngine;

    protected TeXFormFactory fTeXFactory;

    ExprParser fParser;

    public LaTexFactory(final EvalEngine evalEngine, final boolean relaxedSyntax) {
        fEvalEngine = evalEngine;
        // set the thread local instance
        EvalEngine.set(evalEngine);
        DecimalFormatSymbols usSymbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.0####", usSymbols);
        fTeXFactory = new TeXFormFactory(5,7);
        fParser = new ExprParser(evalEngine, relaxedSyntax);
    }

    public void toTeX(final IExpr objectExpression, final Writer out) {
        final StringBuilder buf = new StringBuilder();

        if (objectExpression != null) {
            fTeXFactory.convert(buf, objectExpression, 0);
            try {
                out.write(buf.toString());
            } catch (final Throwable e) {
                // parsedExpression == null ==> fError occured
            }
        }
    }

    /**
     * Convert result to latex to display on {@link io.github.kexanie.library.MathView}
     */
    public static String toLaTeX(IExpr result) {
        StringWriter stringWriter = new StringWriter();
        LaTexFactory latex = new LaTexFactory(EvalEngine.get(), true);
        latex.toTeX(result, stringWriter);
        return "$$" + stringWriter + "$$";
    }
}

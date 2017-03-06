package com.example.duy.calculator.math_eval;

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

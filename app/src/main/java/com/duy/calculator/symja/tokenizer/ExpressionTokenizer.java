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

package com.duy.calculator.symja.tokenizer;

import com.duy.calculator.evaluator.Constants;

import java.util.ArrayList;

/**
 * class used translate language
 * <p>
 * calculated -> english -> translate -> language -> show view
 * <p>
 * view -> translate -> english -> calculate
 */
public class ExpressionTokenizer {
    private final ArrayList<Localize> mReplacements;

    public ExpressionTokenizer() {
        mReplacements = new ArrayList<>();
    }

    private void generateReplacements() {
        mReplacements.clear();
        mReplacements.add(new Localize("/", "÷"));
        mReplacements.add(new Localize("*", "×"));
        mReplacements.add(new Localize("-", "-"));
        mReplacements.add(new Localize("-", "\u2010"));
        mReplacements.add(new Localize("-", "\u2012"));
        mReplacements.add(new Localize("-", "\u2212"));
        mReplacements.add(new Localize("-", "\u2796"));

        mReplacements.add(new Localize("cbrt", "³√"));
        mReplacements.add(new Localize("infinity", Character.toString(Constants.INFINITY_UNICODE)));
        mReplacements.add(new Localize("sqrt", "√"));
        mReplacements.add(new Localize("<=", "≤"));
        mReplacements.add(new Localize(">=", "≥"));
        mReplacements.add(new Localize("!=", "≠"));

        mReplacements.add(new Localize("(pi)", "π"));
        mReplacements.add(new Localize("(degree)", "°"));

        //re translate
        mReplacements.add(new Localize("pi", "π"));
        mReplacements.add(new Localize("degree", "°"));
    }

    /**
     * translate to english
     *
     * @param expr - local language
     * @return - english
     */
    public String getNormalExpression(String expr) {
        generateReplacements();
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.local, replacement.english);
        }
        return expr;
    }

    /**
     * translate to local language
     *
     * @param expr - english
     * @return - local language
     */
    public String getLocalizedExpression(String expr) {
        generateReplacements();
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.english, replacement.local);
        }
        return expr;
    }


    private class Localize {
        String english;
        String local;

        Localize(String english, String local) {
            this.english = english;
            this.local = local;
        }
    }
}
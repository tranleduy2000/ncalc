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

package com.duy.calculator.tokenizer;

import com.duy.calculator.evaluator.Constants;

import java.util.ArrayList;

/**
 * class used translate language
 * <p>
 * calculated -> english -> translate -> language -> show view
 * <p>
 * view -> translate -> english -> calculate
 */
public class Tokenizer {
    private final ArrayList<Localize> mReplacements;
    private String TAG = Tokenizer.class.getName();

    public Tokenizer() {
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
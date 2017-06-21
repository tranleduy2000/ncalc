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
import java.util.ArrayList;

/**
 * Represents a digit, operation, brackets, or a function on the calculator screen; cannot be used by itself
 * (must use a subclass of this).
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class Token implements Serializable {

    protected int type;
    private ArrayList<Token> dependencies = new ArrayList<>(); //Tokens that are dependent with this token
    private String symbol;

    /**
     * Creates a new Token to be shown on the calculator screen.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     */
    public Token(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return The symbol for this Token to be shown on the display
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Adds a dependent token to the list; if this token is removed so would the dependencies.
     *
     * @param t The token that is dependent on this one
     */
    public void addDependency(Token t) {
        dependencies.add(t);
    }

    /**
     * @return The Tokens that are dependent on this one
     */
    public ArrayList<Token> getDependencies() {
        return dependencies;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + symbol + " - " + type + ")";
    }
}


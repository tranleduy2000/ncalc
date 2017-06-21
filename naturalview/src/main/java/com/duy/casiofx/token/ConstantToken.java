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

import java.util.ArrayList;

/**
 * An Object that represents a mathematical / physical Constant.
 *
 * @author Jason Fok
 * @version 3.0
 */
public class ConstantToken extends VariableToken {

    private String name;
    private String HTML; //The HTML encoded symbol
    private double value;
    private String units;

    public ConstantToken(String name, String HTML, String symbol, double value, String units) {
        super(VariableToken.CONSTANT, symbol);
        this.name = name;
        this.value = value;
        this.units = units;
        this.HTML = HTML;
    }

    public double getNumericValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getUnits() {
        return units;
    }

    public ArrayList<Token> getValue() {
        ArrayList<Token> tokens = new ArrayList<>();
        Token tempToken = new NumberToken(value);
        tokens.add(tempToken);
        return tokens;
    }

    public String getHTML() {
        return HTML;
    }

}
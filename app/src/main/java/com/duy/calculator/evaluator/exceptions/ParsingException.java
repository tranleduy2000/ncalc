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

/**
 * Created by Duy on 01-Jul-17.
 */

public class ParsingException extends RuntimeException {

    private String expr;
    private int index;
    private String msg;

    public ParsingException(String expr, int index) {

        this.expr = expr;
        this.index = index;
    }

    public ParsingException(String expr, int index, String msg) {

        this.expr = expr;
        this.index = index;
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public String getExpr() {
        return expr;
    }
}

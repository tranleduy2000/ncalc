/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.token;

/**
 * Object representation of a Function used with Vectors.
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class VectorFunction extends Token {
    public static final int MAGNITUDE = 1, UNIT = 2, PROJ = 3;
    private int type;

    public VectorFunction(String symbol, int type) {
        super(symbol);
        this.type = type;
    }

    public abstract Token perform(VectorToken v);
}

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

package com.example.duy.calculator.item_math_type;

/**
 * Created by DUy on 29-Dec-16.
 */

public class InputException extends Exception {

    public static final int ERROR_INPUT_FROM = 1;
    public static final int ERROR_INPUT_TO = 2;
    public static final int ERROR_INPUT_FUNCTION = 3;
    public static final int ERROR_INPUT_EXPRESSION = 4;
    public static final int ERROR_INPUT_LIMIT = 5;
    public static final int ERROR_MATH = 6;

    private int errorId;
    private String msg;

    public InputException(int id, String e) {
        this.errorId = id;
        this.msg = e;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return this.errorId + " " + msg;
    }
}

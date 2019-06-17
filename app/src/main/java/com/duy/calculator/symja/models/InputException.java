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

package com.duy.calculator.symja.models;

/**
 * Created by Duy on 29-Dec-16.
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

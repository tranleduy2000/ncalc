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

package com.duy.calc.casio.tokenizer;

import java.security.InvalidParameterException;

/**
 * Created by Duy on 27-Jun-17.
 */

public class StringUtil {
    public static int getGroupBracketFromStart(String str, int charIndex) {
        int count = 1;
        if (str.charAt(charIndex) != '(') {
            throw new InvalidParameterException("the index " + charIndex + " not given '(' character");
        }
        int i = charIndex + 1;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }
            if (count == 0) {
                return i;
            }
            i++;
        }
        return i;
    }
}

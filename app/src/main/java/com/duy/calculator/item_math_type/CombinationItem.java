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

package com.duy.calculator.item_math_type;

import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.Constants;

/**
 * Created by Duy on 07-Jan-17.
 */
public class CombinationItem extends PermutationItem {
    public CombinationItem(String n, String k) {
        super(n, k);
    }

    @Override
    public boolean isError(MathEvaluator evaluator) {
        return super.isError(evaluator);
    }

    @Override
    public String toString() {
        return Constants.C + Constants.LEFT_PAREN + numberN + "," + numberK
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getInput() {
        return Constants.C + Constants.LEFT_PAREN + numberN + "," + numberK
                + Constants.RIGHT_PAREN;
    }
}

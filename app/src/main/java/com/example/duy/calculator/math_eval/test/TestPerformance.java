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

package com.example.duy.calculator.math_eval.test;

import android.util.Log;

import com.example.duy.calculator.DLog;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import static org.matheclipse.core.expression.F.CI;
import static org.matheclipse.core.expression.F.CInfinity;
import static org.matheclipse.core.expression.F.Sinc;
import static org.matheclipse.core.expression.F.Times;

/**
 * Created by DUy on 21-Dec-16.
 */

public class TestPerformance {
    private final String TAG = TestPerformance.class.getSimpleName();

    public static void main(String[] args) {
    }

    public void execute() {
        // don't distinguish between lower- and uppercase identifiers
        Config.PARSER_USE_LOWERCASE_SYMBOLS = true;
        EvalUtilities util = new EvalUtilities(false, true);

        IAST function = Sinc(Times(CI, CInfinity));

        IExpr result = PatternMatcher.evalLeftHandSide(function);
        assertEquals(result.internalFormString(true, -1), "Sinc(DirectedInfinity(CI))");

        result = util.evaluate(function);
        assertEquals(result.internalFormString(true, -1), "oo");
    }


    public void assertEquals(String input, String resut) {
        DLog.d(input + " = " + resut + " | " + Boolean.parseBoolean(String.valueOf(input.equalsIgnoreCase(resut))));
    }

    private void testDefineFunction(ExprEvaluator util) {
        try {
            // define a function with a recursive factorial function definition.
            // Note: fac(0) is the stop condition which must be defined first.
            IExpr result;
            util.evaluate("fac(0)=1;fac(x_IntegerQ):=x*fac(x-1)");
            // now calculate factorial of 10:
            result = util.evaluate("fac(10)");
            DLog.d("mResult = " + result.toString());

            util.evaluate("cnk(n_IntegerQ, k_IntegerQ):=(factorial(n) / (factorial(k) * factorial(n - k)))");
            util.evaluate("pnk(n_IntegerQ, k_IntegerQ):=(factorial(n) / (factorial(n - k)))");
            // now calculate factorial of 10:
            result = util.evaluate("cnk(3, 2)");
            DLog.d("mResult = " + result.toString());
            result = util.evaluate("pnk(3, 2)");
            DLog.d("mResult = " + result.toString());
        } catch (SyntaxError e) {
            Log.d(TAG, "testDefineFunction: " + e.getMessage());
        } catch (MathException e) {
            Log.d(TAG, "testDefineFunction: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

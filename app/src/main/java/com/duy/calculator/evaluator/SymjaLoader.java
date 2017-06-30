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

package com.duy.calculator.evaluator;

import android.os.AsyncTask;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Class that loads the Symja library in a separate thread
 */
public class SymjaLoader extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... args) {
        // Simply do a simple (yet beautiful :D) calculation to make the system load Symja
        EvalEngine.get().evaluate(F.Plus(F.ZZ(1), F.Power(F.E, F.Times(F.Pi, F.I))));

        // Solve an integral to load that module
        EvalEngine.get().evaluate(F.Integrate(F.x, F.x));

        // Solve an equation to load that module
        try {
            IExpr iExpr = EvalEngine.get().parse("Solve(2x+1==0,x)");
            EvalEngine.get().evaluate(iExpr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return null (return value won't be used)
        return null;
    }
}

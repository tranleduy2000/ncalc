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

package com.duy.calculator.evaluator;

import android.os.AsyncTask;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Class that loads the Symja library in a separate thread
 */
public class SystemLoader extends AsyncTask<Void, Void, Void> {
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

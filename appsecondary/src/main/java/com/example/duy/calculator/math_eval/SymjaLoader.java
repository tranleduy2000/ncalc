package com.example.duy.calculator.math_eval;

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

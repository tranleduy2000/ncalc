package com.example.duy.calculator.test;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.AbstractEvalStepListener;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

import java.util.Scanner;

public class StepListenerTest {
    private static final String TAG = StepListenerTest.class.getName();

    public StepListenerTest() {

    }

    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            eval(input);
            if (input.equals("exit")) break;
        }
    }

    public static void eval(String input) {
        try {
            ExprEvaluator util = new ExprEvaluator();
            EvalEngine engine = util.getEvalEngine();
            engine.setStepListener(new StepListener());
            IExpr result = util.evaluate(input);
            System.out.println("Result: " + result.toString());
            // disable trace mode if the step listener isn't necessary anymore
            engine.setTraceMode(false);
        } catch (MathException me) {
            // catch Symja math errors here
            System.out.println(me.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class StepListener extends AbstractEvalStepListener {
        private String TAG = StepListener.class.getName();

        /**
         * Listens to the evaluation step in the evaluation engine.
         */
        @Override
        public void add(IExpr inputExpr, IExpr resultExpr, int recursionDepth, long iterationCounter, String hint) {
//            Log.d(TAG, "Depth " + recursionDepth + " Iteration " + iterationCounter + ": " + inputExpr.toString() + " ==> "
//                    + resultExpr.toString());
            System.out.println("Depth " + recursionDepth + " Iteration " + iterationCounter + ": " + inputExpr.toString() + " ==> "
                    + resultExpr.toString());
        }
    }
}
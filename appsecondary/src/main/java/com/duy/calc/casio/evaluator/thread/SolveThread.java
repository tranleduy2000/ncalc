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

package com.duy.calc.casio.evaluator.thread;

import android.os.AsyncTask;

import com.duy.calc.casio.calculator.CalculatorPresenter;
import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.evaluator.MathEvaluator;
import com.duy.calc.casio.evaluator.Utility;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.tokenizer.TokenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.duy.calc.casio.evaluator.Utility.setupExpression;
import static com.duy.calc.casio.tokenizer.MachineExpressionConverter.convertTokensToString;

/**
 * Created by Duy on 24-Jun-17.
 */

public class SolveThread extends BaseThread {

    public SolveThread(CalculatorPresenter presenter,
                       EvaluateConfig config, ResultCallback resultCallback) {
        super(presenter, config, resultCallback);
    }

    public static String createSolveCommand(List<Token> expression, boolean isFraction) {
        if (TokenUtil.hasEqualToken(expression)) {
            if (isFraction) {
                return "Solve(" + convertTokensToString(setupExpression(expression)) + ",x)";
            } else {
                return "N(Solve(" + convertTokensToString(setupExpression(expression)) + ",x))";
            }
        } else {
            if (isFraction) {
                return "Solve(" + convertTokensToString(setupExpression(expression)) + "==0,x)";
            } else {
                return "N(Solve(" + convertTokensToString(setupExpression(expression)) + "==0,x))";
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(List<Token> tokens, EvaluateConfig config) {
        //Passes the rest onto the Thread
        Thread thread = new Thread(resultCallback, config);
        thread.executeOnExecutor(EXECUTOR, tokens);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(List<Token> tokens) {
        //Passes the rest onto the Thread
        Thread thread = new Thread(resultCallback, mConfig);
        thread.executeOnExecutor(EXECUTOR, tokens);
    }

    private class Thread extends AsyncTask<List<Token>, Void, ArrayList<ArrayList<Token>>> {
        Exception error;
        private ResultCallback resultCallback;

        public Thread(ResultCallback resultCallback, EvaluateConfig config) {
            this.resultCallback = resultCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final ArrayList<ArrayList<Token>> doInBackground(List<Token>... params) {
            try {
                ArrayList<Token> tokens = Utility.condenseDigits(params[0]);
                tokens = setupExpression(tokens);
                return MathEvaluator.findRoots(subAns(tokens));
            } catch (Exception e) {
                e.printStackTrace();
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Token>> roots) {
            super.onPostExecute(roots);
            if (!isCancelled()) {
                if (roots == null) {
                    if (error != null) {
                        resultCallback.onError(error);
                    }
                } else {
                    ArrayList<Token> toOutput = new ArrayList<>();
                    if (roots.size() == 0) {
                        toOutput.add(new StringToken("Can't Solve"));
                        resultCallback.onSuccess(toOutput);
                        return;
                    }
                    int counter = 0;
                    toOutput.add(new StringToken("X="));
                    while (counter < roots.size()) {
                        ArrayList<Token> root = roots.get(counter);
                        if (counter != 0) {
                            toOutput.add(new StringToken(" ; X="));
                        }
                        toOutput.addAll(root);
                        counter++;
                    }
                    if (counter == 0) { //No roots
                        toOutput.add(new StringToken("Can't Solve"));
                    }
                    resultCallback.onSuccess(toOutput);
                }
            }
        }
    }

}

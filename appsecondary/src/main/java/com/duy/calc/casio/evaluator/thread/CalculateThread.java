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

import com.duy.calc.casio.Command;
import com.duy.calc.casio.calculator.CalculatorPresenter;
import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.evaluator.MathEvaluator;
import com.duy.calc.casio.evaluator.Utility;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 24-Jun-17.
 */

public class CalculateThread extends BaseThread {

    public CalculateThread(CalculatorPresenter presenter, EvaluateConfig config,
                           ResultCallback resultCallback) {
        super(presenter, config, resultCallback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(List<Token> tokens) {
        Command<ArrayList<Token>, ArrayList<Token>> task = new Command<ArrayList<Token>, ArrayList<Token>>() {
            @Override
            public ArrayList<Token> execute(ArrayList<Token> tokens) {
                //Does a quick checkAll to see if the result would be infinite
                tokens = Utility.multiplyConstants(tokens);
                tokens = Utility.subVariables(tokens);
                return MathEvaluator.evaluateSimple(tokens, mConfig);
            }
        };

        //Passes the rest onto the Thread
        Thread thread = new Thread(task, resultCallback);
        thread.executeOnExecutor(EXECUTOR, new ArrayList<>(tokens));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(List<Token> tokens, final EvaluateConfig config) {
        Command<ArrayList<Token>, ArrayList<Token>> task = new Command<ArrayList<Token>, ArrayList<Token>>() {
            @Override
            public ArrayList<Token> execute(ArrayList<Token> tokens) {
                //Does a quick checkAll to see if the result would be infinite
                tokens = Utility.multiplyConstants(tokens);
                tokens = Utility.subVariables(tokens);
                return MathEvaluator.evaluateSimple(tokens, config);
            }
        };

        //Passes the rest onto the Thread
        Thread thread = new Thread(task, resultCallback);
        thread.executeOnExecutor(EXECUTOR, new ArrayList<>(tokens));
    }


    /**
     * A generalization of the Thread that all the heavy worload calculus functions will use.
     */
    private class Thread extends AsyncTask<ArrayList<Token>, Void, ArrayList<Token>> {
        public Exception error; //If any Exception were to occur
        private Command<ArrayList<Token>, ArrayList<Token>> task;
        private ResultCallback resultCallback;


        public Thread(Command<ArrayList<Token>, ArrayList<Token>> task,
                      ResultCallback resultCallback) {
            this.task = task;
            this.resultCallback = resultCallback;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public final ArrayList<Token> doInBackground(ArrayList<Token>[] params) {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE); //Higher priority
                return task.execute(params[0]);
            } catch (Exception e) {
                error = e;
            } catch (StackOverflowError e) {
                error = new IllegalArgumentException(e.getMessage());
            }
            return null;
        }

        @Override
        public void onPostExecute(ArrayList<Token> result) {
            if (result == null) {
                resultCallback.onError(error);
            } else {
                resultCallback.onSuccess(result);
            }
        }
    }


}

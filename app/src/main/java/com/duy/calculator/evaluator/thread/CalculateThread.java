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

package com.duy.calculator.evaluator.thread;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.duy.calculator.CalculatorPresenter;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;
import static android.os.Process.setThreadPriority;

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
    public void execute(String expr) {
        execute(expr, mConfig);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(String expr, final EvaluateConfig config) {
        Command<ArrayList<String>, String> task = new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                return Lists.newArrayList(MathEvaluator.newInstance().evaluateWithResultAsTex(input, config));
            }
        };

        //Passes the rest onto the Thread
        Thread thread = new Thread(task, resultCallback);
        thread.executeOnExecutor(EXECUTOR, expr);
    }

    @Override
    public void execute(@Nullable Command<ArrayList<String>, String> command, String expr) {

        //Passes the rest onto the Thread
        Thread thread = new Thread(command, resultCallback);
        thread.executeOnExecutor(EXECUTOR, expr);
    }


    /**
     * A generalization of the Thread that all the heavy worload calculus functions will use.
     */
    private static class Thread extends AsyncTask<String, Void, ArrayList<String>> {
        public Exception error; //If any Exception were to occur
        private Command<ArrayList<String>, String> task;
        private ResultCallback resultCallback;


        public Thread(Command<ArrayList<String>, String> task,
                      ResultCallback resultCallback) {
            this.task = task;
            this.resultCallback = resultCallback;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public final ArrayList<String> doInBackground(String... params) {
            try {
                setThreadPriority(THREAD_PRIORITY_MORE_FAVORABLE); //Higher priority
                return task.execute(params[0]);
            } catch (Exception e) {
                error = e;
            } catch (StackOverflowError e) {
                error = new IllegalArgumentException(e.getMessage());
            }
            return null;
        }

        @Override
        public void onPostExecute(ArrayList<String> result) {
            if (result == null) {
                resultCallback.onError(error);
            } else {
                resultCallback.onSuccess(result);
            }
        }
    }


}

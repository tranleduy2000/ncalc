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

import android.support.annotation.Nullable;

import com.duy.calculator.CalculatorPresenter;
import com.duy.calculator.evaluator.EvaluateConfig;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Duy on 30-Jun-17.
 */

public abstract class BaseThread {
    //Some Threading stuff
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final int KEEP_ALIVE = 1;
    public static final int STACK_SIZE = 500000; //1MB STACK SIZE
    public static final ThreadFactory threadFactory = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            ThreadGroup group = new ThreadGroup("threadGroup");
            return new Thread(group, r, "Calculus Thread", STACK_SIZE);
        }
    };
    public static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(128);
    public static final Executor EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            threadFactory);
    protected CalculatorPresenter presenter;
    protected EvaluateConfig mConfig;
    protected ResultCallback resultCallback;

    public BaseThread(CalculatorPresenter presenter, EvaluateConfig config, ResultCallback resultCallback) {
        this.presenter = presenter;
        this.mConfig = config;
        this.resultCallback = resultCallback;
    }

    public abstract void execute(String expr);

    public abstract void execute(String expr, EvaluateConfig config);

    public abstract void execute(@Nullable Command<ArrayList<String>, String> command, String expr);


    public interface ResultCallback {
        void onSuccess(ArrayList<String> result);

        void onError(Exception e);
    }

}

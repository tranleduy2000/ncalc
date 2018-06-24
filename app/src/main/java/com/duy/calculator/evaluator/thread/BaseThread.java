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

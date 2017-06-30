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

package com.duy.calc.casio.calculator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.calc.casio.activities.HistoryActivity;
import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.evaluator.thread.CalculateThread;
import com.duy.calc.casio.evaluator.thread.SolveThread;
import com.duy.calc.casio.exception.DMathException;
import com.duy.calc.casio.exception.EmptyInputException;
import com.duy.calc.casio.exception.NonBalanceBracketException;
import com.duy.calc.casio.exception.NumberTooLargeException;
import com.duy.calc.casio.exception.PlaceholderException;
import com.duy.calc.casio.exception.SyntaxException;
import com.duy.calc.casio.settings.SettingsActivity;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.factory.VariableFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 24-Jun-17.
 */
public class CalculatorPresenter implements CalculatorContract.Presenter {

    @NonNull
    private CalculatorActivity activity;
    @NonNull
    private CalculatorContract.Display display;
    @NonNull
    private CalculatorContract.Keyboard keyboard;
    @Nullable
    private BaseThread.ResultCallback constantSelectListener;
    @NonNull
    private EvaluateConfig evaluateConfig;

    public CalculatorPresenter(@NonNull CalculatorActivity activity,
                               @NonNull CalculatorContract.Display display,
                               @NonNull CalculatorContract.Keyboard keyboard) {
        this.activity = activity;
        this.display = display;
        this.keyboard = keyboard;
        evaluateConfig = EvaluateConfig.loadFromSetting(activity);

        display.setPresenter(this);
        keyboard.setPresenter(this);
    }

    @Override
    public void doCalculate(List<Token> expr, @Nullable BaseThread.ResultCallback resultCallback) {
        display.showProcessBar();
        try {
            new CalculateThread(this, evaluateConfig, resultCallback).execute(expr);
        } catch (Exception e) { //User did a mistake
            if (resultCallback != null) {
                resultCallback.onError(e);
            }
        }
    }

    @Override
    public void findRoots(List<Token> expr) {
        display.showProcessBar();
        try {
            new SolveThread(this, evaluateConfig, new BaseThread.ResultCallback() {
                @Override
                public void onSuccess(ArrayList<Token> result) {
                    display.displayOutput(result);
                    display.scrollDown();
                    display.hideProcessBar();

                    VariableFactory.setAnsValue(result);
                }

                @Override
                public void onError(Exception e) {
                    handleExceptions(e);
                    display.hideProcessBar();
                }
            }).execute(expr);
        } catch (Exception e) { //User did a mistake
            handleExceptions(e);
        }
    }

    /**
     * Called when an exception occurs anywhere during processing.
     *
     * @param e The exception that was thrown
     */
    public void handleExceptions(Exception e) {
        String message = "";
        if (e instanceof NumberTooLargeException) {
            message = "The calculation is to large to perform";
        } else if (e instanceof ArithmeticException) {
            message = "Math Error";
        } else if (e instanceof IllegalArgumentException) {
            message = e.getMessage();
        } else if (e instanceof DMathException) {
            message = "Math Error";
        } else if (e instanceof SyntaxException) {
            message = "Syntax Error";
        } else if (e instanceof PlaceholderException) {
            message = "Syntax Error, some position has empty input";
        } else if (e instanceof NonBalanceBracketException) {
            message = "Syntax Error, non balance bracket";
        } else if (e instanceof EmptyInputException) {
            message = "Input is empty";
        } else {
            if (e.getMessage() == null || e.getMessage().equals("")) {
                message = "Invalid input";
            } else {
                message = "Unknown Error : " + e.getMessage();
            }
        }
        if (message != null && !message.isEmpty()) {
            display.showToast(message);
        }
    }

    @NonNull
    @Override
    public CalculatorActivity getActivity() {
        return activity;
    }

    @Override
    public void openHistory() {
        Intent intent = new Intent(activity, HistoryActivity.class);
        activity.startActivityForResult(intent, CalculatorActivity.REQUEST_CODE_HISTORY);
    }

    @NonNull
    @Override
    public CalculatorContract.Keyboard getKeyboard() {
        return keyboard;
    }

    @NonNull
    @Override
    public CalculatorContract.Display getDisplay() {
        return display;
    }

    @Override
    public void openSetting() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivityForResult(intent, CalculatorActivity.REQUEST_CODE_SETTING);
    }

    @Override
    public void getConstant(BaseThread.ResultCallback constantSelectListener) {
        this.constantSelectListener = constantSelectListener;
        activity.showDialogConstant();
    }

    @NonNull
    @Override
    public EvaluateConfig getEvaluateConfig() {
        return evaluateConfig;
    }

    @Override
    public void onConstantSelected(ConstantToken constantToken) {
        if (constantSelectListener != null) {
            ArrayList<Token> result = new ArrayList<>();
            result.add(constantToken);
            constantSelectListener.onSuccess(result);
        }
    }

    @Override
    public void updateSettings() {
        evaluateConfig = EvaluateConfig.loadFromSetting(activity);
        display.updateFromSettings();
        keyboard.updateFromSettings();
    }
}

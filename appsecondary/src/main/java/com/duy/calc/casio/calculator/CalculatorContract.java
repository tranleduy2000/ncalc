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

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.keyboardlistener.KeyboardListener;
import com.duy.calc.casio.mutilbutton.ButtonMode;
import com.duy.calc.casio.mutilbutton.MultiButton;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.Token;

import java.util.List;

/**
 * Created by Duy on 24-Jun-17.
 */

public class CalculatorContract {

    public interface Display {
        void showToast(String msg);

        void showToast(@StringRes int id);

        void showProcessDialog(String title, String msg);

        void dismissDialog();

        void setPresenter(Presenter presenter);

        void displayInput(List<Token> tokens);

        void displayOutput(List<Token> tokens);

        int getCursorIndex();

        void setCursorIndex(int pos);

        int getRealCursorIndex();

        void setRealCursorIndex(int mRealCursorIndex);

        void appendToken(Token token);

        void scrollDown();

        void scrollLeft();

        void scrollRight();

        void scrollUp();

        void clear();

        void showProcessBar();

        void showProcessBar(int value);

        void hideProcessBar();

        void setShowCursor(boolean show);

        boolean isActive();

        void reset();

        void updateFromSettings();
    }

    public interface Keyboard {
        void setPresenter(Presenter presenter);

        void updateMultiButtons(ButtonMode buttonMode);

        void addMultiButton(MultiButton multiButton);

        void updateFromSettings();

        void setKeyboardListener(KeyboardListener listener);
    }

    public interface Presenter {

        void doCalculate(List<Token> expr, BaseThread.ResultCallback resultCallback);

        void findRoots(List<Token> expr);

        void getConstant(BaseThread.ResultCallback resultCallback);

        void onConstantSelected(ConstantToken constantToken);

        @NonNull
        EvaluateConfig getEvaluateConfig();

        void updateSettings();

        Display getDisplay();

        void openSetting();

        void handleExceptions(Exception e);

        CalculatorActivity getActivity();

        void openHistory();

        Keyboard getKeyboard();

    }
}

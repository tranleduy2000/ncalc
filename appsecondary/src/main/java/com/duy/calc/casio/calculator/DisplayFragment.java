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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duy.calculator.R;
import com.duy.calc.casio.naturalview.DisplayView;
import com.duy.calc.casio.settings.CalculatorSetting;
import com.duy.calc.casio.token.Token;

import java.util.List;

/**
 * Created by Duy on 24-Jun-17.
 */

public class DisplayFragment extends Fragment implements CalculatorContract.Display {
    private static final String TAG = "DisplayFragment";
    private DisplayView mDisplayView;
    private DisplayView mOutputView;
    private ScrollView mScrollView;
    private ProgressBar mProgressBar;
    private int fontSize;
    private CalculatorContract.Presenter mPresenter;
    private TextView mAngleMode;

    public static DisplayFragment newInstance() {
        Bundle args = new Bundle();
        DisplayFragment fragment = new DisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int id) {
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProcessDialog(String title, String msg) {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void setPresenter(CalculatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void displayInput(List<Token> tokens) {
        mDisplayView.display(tokens);
    }

    @Override
    public void displayOutput(List<Token> tokens) {
        mOutputView.display(tokens);
    }

    @Override
    public int getCursorIndex() {
        return mDisplayView.getCursorIndex();
    }

    @Override
    public void setCursorIndex(int pos) {
        mDisplayView.setCursorIndex(pos);
    }

    @Override
    public int getRealCursorIndex() {
        return mDisplayView.getRealCursorIndex();
    }

    @Override
    public void setRealCursorIndex(int mRealCursorIndex) {
        mDisplayView.setRealCursorIndex(mRealCursorIndex);
    }

    @Override
    public void appendToken(Token token) {
        mDisplayView.getExpression().add(token);
        mDisplayView.invalidate();
    }

    /**
     * Scrolls down the mDisplay (if possible).
     */
    @Override
    public void scrollDown() {
        mScrollView.pageScroll(ScrollView.FOCUS_DOWN);
    }

    /**
     * Moves the cursor on the mDisplay left if possible.
     */
    @Override
    public void scrollLeft() {
        mDisplayView.scrollLeft();
        mDisplayView.invalidate();
    }

    /**
     * Moves the cursor on the mDisplay right if possible.
     */
    @Override
    public void scrollRight() {
        mDisplayView.scrollRight();
        mDisplayView.invalidate();
    }

    @Override
    public void scrollUp() {

    }

    @Override
    public void clear() {
        mDisplayView.clear();
        mOutputView.clear();
    }

    @Override
    public void showProcessBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
    }

    @Override
    public void showProcessBar(int value) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.setProgress(value);
    }

    @Override
    public void hideProcessBar() {
        mProgressBar.setIndeterminate(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setShowCursor(boolean show) {
        mDisplayView.getCursor().setVisible(show);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void reset() {
        mDisplayView.resetPosition();
    }

    @Override
    public void updateFromSettings() {
        CalculatorSetting setting = CalculatorSetting.newInstance(getContext());
        fontSize = setting.getFontSize();

        //Sets the font sizes
        mDisplayView.setFontSize(fontSize);
        mOutputView.setFontSize(fontSize);

        mAngleMode.setText(setting.useRadian() ? "RAD" : "DEG");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisplayView = view.findViewById(R.id.display);

        mOutputView = view.findViewById(R.id.output);
        mOutputView.getCursor().setVisible(false);

        mScrollView = view.findViewById(R.id.display_scroll);
        mProgressBar = view.findViewById(R.id.progress_eval);
        mProgressBar.setVisibility(View.GONE);

        view.findViewById(R.id.btn_open_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getActivity().openDrawer();
            }
        });
        view.findViewById(R.id.btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openHistory();
            }
        });

        mAngleMode = view.findViewById(R.id.txt_angle_mode);
        mAngleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openSetting();
            }
        });

        updateFromSettings();
    }


}

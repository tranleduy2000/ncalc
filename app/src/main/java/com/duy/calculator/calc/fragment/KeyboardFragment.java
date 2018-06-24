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

package com.duy.calculator.calc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.duy.calculator.R;
import com.duy.calculator.calc.KeyboardListener;
import com.duy.calculator.document.activities.DocumentActivity;
import com.duy.calculator.settings.SettingsActivity;
import com.duy.calculator.view.ButtonID;
import com.duy.calculator.view.CalcButton;
import com.duy.calculator.view.CalculatorEditText;

/**
 * Created by Duy on 9/21/2017.
 */

public class KeyboardFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = "KeyboardFragment";
    @Nullable
    private KeyboardListener mCalculatorListener;

    public static KeyboardFragment newInstance() {

        Bundle args = new Bundle();

        KeyboardFragment fragment = new KeyboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCalculatorListener = (KeyboardListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvent(view);
    }

    private void addEvent(View view) {
        for (int id : ButtonID.getIdBasic()) {
            try {
                View v = view.findViewById(id);
                if (v != null) {
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                } else {
                    View padBasic = view.findViewById(R.id.pad_basic);
                    v = padBasic.findViewById(id);
                    if (v != null) {
                        v.setOnClickListener(this);
                        v.setOnLongClickListener(this);
                    } else {
                        View padAdvance = view.findViewById(R.id.pad_advance);
                        v = padAdvance.findViewById(id);
                        if (v != null) {
                            v.setOnClickListener(this);
                            v.setOnLongClickListener(this);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mCalculatorListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_derivative:
                mCalculatorListener.clickDerivative();
                break;
            case R.id.btn_graph_main:
                mCalculatorListener.clickGraph();
                break;
            case R.id.btn_ten_power:
                mCalculatorListener.insertText("10^");
                break;
            case R.id.btn_power_2:
                mCalculatorListener.insertText("^");
                mCalculatorListener.insertText("2");
                break;
            case R.id.btn_power_3:
                mCalculatorListener.insertText("^");
                mCalculatorListener.insertText("3");
                break;
            case R.id.btn_calc:
                mCalculatorListener.onDefineAndCalc();
                break;
            case R.id.btn_fact:
                mCalculatorListener.clickFactorPrime();
                break;
            case R.id.btn_help:
                startActivity(new Intent(getContext(), DocumentActivity.class));
            case R.id.btn_solve_:
                mCalculatorListener.clickSolveEquation();
                break;

            case R.id.img_setting:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            case R.id.img_share:
                mCalculatorListener.shareText();
                break;
            case R.id.btn_delete:
                mCalculatorListener.onDelete();
                break;
            case R.id.btn_clear:
                mCalculatorListener.clickClear();
                break;
            case R.id.btn_equal:
                mCalculatorListener.onEqual();
                break;
            case R.id.btn_ans:
                mCalculatorListener.insertText("Ans");
                break;
            case R.id.btn_sqrt:
                mCalculatorListener.insertText("Sqrt(" + CalculatorEditText.CURSOR + ")");
                break;
            default:
                if (view instanceof CalcButton) {
                    CalcButton calcButton = (CalcButton) view;
                    String text = calcButton.getText().toString();
                    if (text.length() >= 2) {
                        mCalculatorListener.insertText(text + "(" + CalculatorEditText.CURSOR + ")");
                    } else {
                        mCalculatorListener.insertText(((Button) view).getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mCalculatorListener == null) {
            return false;
        }
        switch (view.getId()) {
            case R.id.btn_delete:
                mCalculatorListener.clickClear();
                return true;
        }
        return false;
    }
}

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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calc.casio.Command;
import com.example.duy.calculator.R;
import com.duy.calc.casio.SpannedHelper;
import com.duy.calc.casio.calculator.listener.AbstractFragment;
import com.duy.calc.casio.calculator.listener.KeyboardMode;
import com.duy.calc.casio.calculator.listener.compute.ComputeListener;
import com.duy.calc.casio.keyboardlistener.KeyboardListener;
import com.duy.calc.casio.keyboardlistener.base.BaseKeyboardListener;
import com.duy.calc.casio.mutilbutton.ButtonMode;
import com.duy.calc.casio.mutilbutton.CommandDescriptor;
import com.duy.calc.casio.mutilbutton.MultiButton;
import com.duy.calc.casio.mutilbutton.MultiButtonDescriptor;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.view.CalcButton;
import com.duy.calc.casio.view.CalcImageButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class KeyboardFragment extends AbstractFragment implements CalculatorContract.Keyboard, View.OnClickListener {
    public static final String TOKENS_FILENAME = "expression";
    private static final int VIBRATE_DURATION = 50;
    protected ArrayList<MultiButton> multiButtons = new ArrayList<>();
    private HashMap<KeyboardMode, KeyboardListener> mKeyboardModes = new HashMap<>();
    private View mRoot;
    private CalculatorContract.Presenter mPresenter;
    private Vibrator mVibrator;
    private boolean vibrate, instantResult;


    private KeyboardListener mKeyboardListener;

    public static KeyboardFragment newInstance() {
        Bundle args = new Bundle();
        KeyboardFragment fragment = new KeyboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void changeKeyboardMode(KeyboardMode keyboardMode) {
        this.mKeyboardListener = mKeyboardModes.get(keyboardMode);
        this.mKeyboardListener.setPresenter(mPresenter);
    }

    @Override
    public int getRootId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (CalculatorActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        KeyboardListener computeListener = new ComputeListener();
        KeyboardListener baseListener = new BaseKeyboardListener();

        mKeyboardModes.put(KeyboardMode.COMPUTE, computeListener);
        mKeyboardModes.put(KeyboardMode.BASE, baseListener);

        //set default mode
        changeKeyboardMode(KeyboardMode.COMPUTE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRoot = view;
        updateFromSettings();
        setupButtons(view);
        loadFromPrevious();
    }

    /**
     * Sets up the buttons.
     *
     * @param v The view of the Fragment
     */
    private void setupButtons(View v) {
        //Sets up the MultiButtons
        setupCalcButton();
        setupReciprocalButton();
        setupFractionButton();
        setupSqrtButton();
        setupSquareButton();
        setupPowerButton();
        setupLogButton();
        setupLnButton();
        setupNegButton();
        setupDegreeButton();
        setupHypButton();
        setupSinButton();
        setupCosButton();
        setupTanButton();
        setupRclButton();
        setupEngButton();
        setupOpenParenthesesButton();
        setupCloseParenthesesButton();
        setupMPlusButton();
        setupShiftButton();
        setupAlphaButton();
        setupModeButton();
        setupOneButton();
        setupTwoButton();
        setupThreeButton();
        setupFourButton();
        setupFiveButton();
        setupSixButton();
        setupSevenButton();
        setupEightButton();
        setupNineButton();
        setupZeroButton();
        setupDotButton();
        setupDivButton();
        setupAddButton();
        setupSubButton();
        setupMulButton();
        setupAnsButton();
        setupEqualButton();
        setupDeleteButton();
        setupClearButton();
        setupMulPowerTenButton();
        setupDecimalFracButton();
        setupLognButton();
        setupIntegrateButton();
        setupSettingButton();
        setupControlButton();

        updateMultiButtons(ButtonMode.NORMAL);
    }

    private void setupControlButton() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        findViewById(R.id.btn_up).setOnClickListener(this);
        findViewById(R.id.btn_down).setOnClickListener(this);
    }

    private void setupSettingButton() {
        View view = findViewById(R.id.btn_setting);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openSetting();
            }
        });
    }

    private void setupLognButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.logn)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickLogN();
                return false;
            }
        }));

//        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
//                SpannedHelper.fromHtml(getString(R.string.sum)), new Command<Boolean, Object ({
//            @Override
//            public Boolean execute(Object o) {
//                getKeyboardListener().clickDerivative();
//               return false;
//            }
//        }));
//        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, ":", new Command<Boolean, Object ({
//            @Override
//            public Boolean execute(Object o) {
//                 TODO: 23-Jun-17
//               return false;
//            }
//        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_log_n);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupIntegrateButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, ""/*
                SpannedHelper.fromHtml(getString(R.string.integrate))*/, new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
//                getKeyboardListener().clickIntegrate();
                return false;
            }
        }));
        // TODO: 26-Jun-17  integrate

        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.casio_derivative)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDerivative();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, ":", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSemicolon();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_integrate);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupDecimalFracButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.s_d), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDecFracMode();
                return false;
            }
        }));

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "Y", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickY();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "Y", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickY();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_s_to_d);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupModeButton() {
        View view = findViewById(R.id.btn_mode);
        view.setOnClickListener(this);
    }

    private void setupNegButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "(−)", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickNegative();
                return false;
            }
        }));

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "A", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickA();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "A", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickA();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_neg);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupPowerButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.x_power)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPower();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.surd)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSurd();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.BASE, "HEX", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().click_e();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_power);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupSqrtButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.sqrt)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSqrt();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.cbrt)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCbrt();
                return false;
            }
        }));

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                getString(R.string.divide) + "R", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickRedundancy();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.sqrt_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupFractionButton() {
        CalcImageButton button = (CalcImageButton) findViewById(R.id.btn_frac);
        button.setOnClickListener(this);
    }

    private void setupMulPowerTenButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.mul_pow_of_ten)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickMulPowerOfTen();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                getString(R.string.pi), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPi();
                return false;
            }
        }));

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                SpannedHelper.fromHtml(getString(R.string.e)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().click_e();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.mul_pow_ten);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private View findViewById(int id) {
        return mRoot.findViewById(id);
    }

    private void setupClearButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, getString(R.string.ac), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickClear();
                return false;
            }
        }));
       /* descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, getString(R.string.off), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickExit();
                return false;
            }
        }));*/

        MultiButton button = (MultiButton) findViewById(R.id.btn_clear);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupDeleteButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, getString(R.string.del), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickBack();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_del);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupEqualButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "=", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickEquals();
                return false;
            }
        }));
        CalcButton button = (CalcButton) findViewById(R.id.btn_equals);
        button.setDescriptor(descriptor);
        button.setOnClickListener(this);
    }

    private void setupAnsButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "Ans", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickAns();
                return false;
            }
        }));
       /* descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, getString(R.string.drg), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDRG();
                return false;
            }
        }));*/
        // TODO: 28-Jun-17

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "PreAns", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPreAns();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_ans);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupSubButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.minus), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSubtract();
                return false;
            }
        }));
     /*   descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "Rec", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickRec();
                return false;
            }
        }));*/
        // TODO: 26-Jun-17  rec function
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                "Intg", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickFloor();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.subtract_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupAddButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.add), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickAdd();
                return false;
            }
        }));
      /*  descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "Pol", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPol();
                return false;
            }
        }));*/
        // TODO: 26-Jun-17  pol function
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                "Int", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickInt();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_add);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupMulButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.multiply), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickMultiply();
                return false;
            }
        }));
       /* descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.permutation)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPermutation();
                return false;

            }
        }));*/
        // TODO: 26-Jun-17  permuatation
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                "GCD", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickGCD();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_multiply);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupDivButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.divide), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDivide();
                return false;
            }
        }));
     /*   descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.combination)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCombination();
                return false;
            }
        }));*/
        // TODO: 26-Jun-17  combination

        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                "LCM", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickLCM();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_divied);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupOneButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "1", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickOne();
                return false;
            }
        }));
       /* descriptor.addCommand(new CommandDescriptor(ButtonMode.STAT,
                "STAT/DIST", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                // TODO: 23-Jun-17
                return false;
            }
        }));*/
        MultiButton button = (MultiButton) findViewById(R.id.btn_one);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupTwoButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "2", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickTwo();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.COMPLEX, "CMPLX", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickComplex();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_two);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupThreeButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "3", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickThree();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.BASE, "BASE", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickBase();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_three);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupFourButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "4", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickFour();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.MATRIX,
                "MATRIX", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickMatrix();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_four);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupFiveButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "5", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickFive();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.VECTOR,
                "VECTOR", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickVector();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_five);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupSixButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "6", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSix();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_six);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupSevenButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "7", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSeven();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                "CONST", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickConst();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_seven);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupEightButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "8", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickEight();
                return false;
            }
        }));
        /*descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                "CONV", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickConv();
                return false;

            }
        }));*/
        // TODO: 26-Jun-17  conversation
        MultiButton button = (MultiButton) findViewById(R.id.btn_eight);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupNineButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "9", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickNine();
                return false;
            }
        }));
        /*descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                "CLR", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickClr();
                return false;
            }
        }));*/
        // TODO: 26-Jun-17 clr
        MultiButton button = (MultiButton) findViewById(R.id.btn_nine);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupZeroButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "0", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickZero();
                return false;
            }
        }));

        MultiButton button = (MultiButton) findViewById(R.id.btn_zero);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupDotButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, ".", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDecimal();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                "Ran#", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickRandomReal();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA,
                "RanInt", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickRandomInt();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.dot_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupEngButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "ENG", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickENG();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                getString(R.string.arrow_left), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                // TODO: 23-Jun-17
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.COMPLEX,
                SpannedHelper.fromHtml(getString(R.string.implicit)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickImplicit();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.eng_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupReciprocalButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.reciprocal)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickReciprocal();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.casio_factorial)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickFactorial();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.reciprocal);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupShiftButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "SHIFT", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickShift();
                return false;
            }
        }));
        CalcButton button = (CalcButton) findViewById(R.id.shift_button);
        button.setDescriptor(descriptor);
        button.setOnClickListener(this);
    }

    private void setupAlphaButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "ALPHA", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickAlpha();
                return false;
            }
        }));
        CalcButton button = (CalcButton) findViewById(R.id.alpha_button);
        button.setDescriptor(descriptor);
        button.setOnClickListener(this);
    }

    private void setupLnButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "ln", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickLn();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.pow_of_e)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPowerOfE();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.BASE, "OCT", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickOctal();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.ln_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupLogButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "log", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickLog_10();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.pow_of_ten)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPowerOfTen();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.BASE, "BIN", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickBin();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.log_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupSquareButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                SpannedHelper.fromHtml(getString(R.string.square)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSquare();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.cube)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCube();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.BASE, "DEC", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDecimal();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.square_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupRclButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "RCL", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickRcl();
                return true;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "STO", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickStore();
                return true;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_rcl);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupDegreeButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL,
                getString(R.string.degree_button), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickDegree();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "FACT", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPrimeFactor();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "B", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickB();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "B", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickB();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.degree_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupHypButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "hyp", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickHyp();
                return true;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "Abs", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickAbs();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "C", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickC();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "C", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickC();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_hyp);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupCloseParenthesesButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, ")", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCloseParentheses();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "，", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCommaSign();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "X", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickX();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "X", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickX();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.closed_parentheses_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupOpenParenthesesButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "(", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickOpenParentheses();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "%", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickPercent();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.open_parentheses_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupMPlusButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "M+", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickMPlus();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "M-", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickMMinus();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "M", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickM();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "M", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickM();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.m_plus_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupTanButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "tan", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickTan();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.arctan)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickATan();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "F", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickF();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "F", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickF();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.tan_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);

    }

    private void setupCosButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "cos", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCos();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.arccos)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickACos();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "E", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickE();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "E", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickE();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.cos_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupSinButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "sin", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickSin();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT,
                SpannedHelper.fromHtml(getString(R.string.arcsin)), new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickASin();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "D", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickD();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.STORE, "D", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickD();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.sin_button);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    private void setupCalcButton() {
        MultiButtonDescriptor descriptor = new MultiButtonDescriptor();
        descriptor.addCommand(new CommandDescriptor(ButtonMode.NORMAL, "CALC", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickCalc();
                return true;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.SHIFT, "SOLVE", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickFindRoots();
                return false;
            }
        }));
        descriptor.addCommand(new CommandDescriptor(ButtonMode.ALPHA, "=", new Command<Boolean, Object>() {
            @Override
            public Boolean execute(Object o) {
                getKeyboardListener().clickEqualSymbol();
                return false;
            }
        }));
        MultiButton button = (MultiButton) findViewById(R.id.btn_calc);
        button.setDescriptor(descriptor);
        addMultiButton(button);
        button.setOnClickListener(this);
    }

    @Override
    public void setPresenter(CalculatorContract.Presenter presenter) {
        this.mPresenter = presenter;
        if (mPresenter.getDisplay().isActive()) {
            mPresenter.getDisplay().displayInput(getKeyboardListener().getExpression());
        }
    }

    @Override
    public void updateMultiButtons(ButtonMode buttonMode) {
        for (MultiButton b : multiButtons) {
            b.changeMode(buttonMode);
        }
    }

    public void addMultiButton(MultiButton multiButton) {
        multiButtons.add(multiButton);
    }


    /**
     * Sets up the settings from preferences.
     */
    @Override
    public void updateFromSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Retrieves the default values from Preferences
        vibrate = pref.getBoolean(getString(R.string.key_vibrarte), false);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        instantResult = pref.getBoolean(getString(R.string.key_instant_result), false);
    }


    /**
     * Loads previously saved modes and expression
     */
    private void loadFromPrevious() {
        //Mode
        //Tokens
        try {
            FileInputStream stream = getContext().openFileInput(TOKENS_FILENAME);
            ObjectInputStream objectStream = new ObjectInputStream(stream);
            ArrayList<Token> tokens = (ArrayList<Token>) objectStream.readObject();
            objectStream.close();
            stream.close();
            getKeyboardListener().setExpression(tokens);
            //Now sets the expression
        } catch (ClassNotFoundException | IOException ignored) {
        }
    }

    protected KeyboardListener getKeyboardListener() {
        return mKeyboardListener;
    }

    @Override
    public void setKeyboardListener(KeyboardListener listener) {
        this.mKeyboardListener = listener;
    }

    public void onPause() {
        //Saves expression
        try {
            FileOutputStream outStream = getContext().openFileOutput(TOKENS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectStreamOut = new ObjectOutputStream(outStream);
            objectStreamOut.writeObject(getKeyboardListener().getExpression());
            objectStreamOut.flush();
            objectStreamOut.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        getKeyboardListener().onClick(view);
        if (vibrate) {
            mVibrator.vibrate(VIBRATE_DURATION);
        }
    }
}

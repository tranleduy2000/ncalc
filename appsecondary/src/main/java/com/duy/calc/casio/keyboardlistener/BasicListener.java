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

package com.duy.calc.casio.keyboardlistener;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.duy.calc.casio.Command;
import com.example.duy.calculator.R;
import com.duy.calc.casio.calculator.CalculatorActivity;
import com.duy.calc.casio.calculator.CalculatorContract;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.mutilbutton.ButtonMode;
import com.duy.calc.casio.calculator.listener.DisplayMode;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.DigitToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.PlaceholderToken;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;
import com.duy.calc.casio.token.factory.BracketFactory;
import com.duy.calc.casio.token.factory.ConstantFactory;
import com.duy.calc.casio.token.factory.DigitFactory;
import com.duy.calc.casio.token.factory.FunctionFactory;
import com.duy.calc.casio.token.factory.OperatorFactory;
import com.duy.calc.casio.token.factory.PlaceholderFactory;
import com.duy.calc.casio.token.factory.SymbolFactory;
import com.duy.calc.casio.token.factory.VariableFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.duy.calc.casio.tokenizer.TokenUtil.getFractionStart;

/**
 * The activity for the basic calculator buttonMode. The basic buttonMode will only be able to
 * perform the four operations (add, subtract, multiply and divide) including brackets.
 * This class contains all the back-end of the Basic Mode.
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class BasicListener implements KeyboardListener {
    public static final int HISTORY_SIZE = 25;
    private static final String TAG = "Basic";
    public static String filename = "history_basic";
    public CalculatorContract.Display mDisplay;
    public CalculatorActivity activity;
    public ArrayList<Token> expression = new ArrayList<>(); //Tokens shown on screen
    protected boolean changedTokens = false;
    protected Fragment fragment;
    protected CalculatorContract.Presenter mPresenter;

    protected boolean memory = false;
    protected boolean recall = false;

    protected ButtonMode buttonMode = ButtonMode.NORMAL;
    protected DisplayMode mDisplayMode = DisplayMode.NORMAL;


    public void setPresenter(CalculatorContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mDisplay = presenter.getDisplay();
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * When a Button has been clicked, calls the appropriate method.
     *
     * @param v The Button that has been clicked
     */
    @Override
    public boolean onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del:
                clickBack();
                return true;
            case R.id.btn_clear:
                clickClear();
                return true;
            case R.id.btn_add:
                clickAdd();
                return true;
            case R.id.subtract_button:
                clickSubtract();
                return true;
            case R.id.btn_multiply:
                clickMultiply();
                return true;
            case R.id.btn_history:
                clickHistory();
                return true;
            case R.id.btn_divied:
                clickDivide();
                return true;
            case R.id.btn_left:
            case R.id.btn_up:
                mDisplay.scrollLeft();
                return true;
            case R.id.btn_down:
            case R.id.btn_right:
                mDisplay.scrollRight();
                return true;
            default: //Button has not been handled!
                Log.w(TAG, "onClick: A Button has not been handled! " + "R.id." + v.getId());
        }
        return false;
    }


    /**
     * When the user clicks the History button.
     */
    public void clickHistory() {
        openHistory(filename);
    }

    /**
     * Exits the history view.
     */
    public void clickExit() {
        activity.finish();
    }

    /**
     * When the user presses the 1 Button.
     */
    public void clickOne() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeOne());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 2 Button.
     */
    public void clickTwo() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeTwo());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 3 Button.
     */
    public void clickThree() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeThree());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 4 Button.
     */
    public void clickFour() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeFour());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 5 Button.
     */
    public void clickFive() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeFive());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 6 Button.
     */
    public void clickSix() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeSix());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 7 Button.
     */
    public void clickSeven() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeSeven());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 8 Button.
     */
    public void clickEight() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeEight());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 9 Button.
     */
    public void clickNine() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeNine());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 0 Button.
     */
    public void clickZero() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeZero());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the . Button.
     */
    public void clickDecimal() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeDecimal());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the x Button.
     */
    public void clickMultiply() {
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makeMultiply());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the / Button.
     */
    public void clickDivide() {
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makeDivide());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the + Button.
     */
    public void clickAdd() {
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makeAdd());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the - Button.
     */
    public void clickSubtract() {
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makeSubtract());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }


    /**
     * When the user presses the clear Button.
     */
    public void clickClear() {
        expression.clear();
        updateInput();
        changedTokens = true; //used to know if the button has been used
        mDisplay.displayOutput(new ArrayList<Token>());
        mDisplay.reset();
    }

    /**
     * When the user presses the back Button.
     */
    @Override
    public void clickBack() {
        if (expression.isEmpty() || mDisplay.getRealCursorIndex() - 1 < 0) {
            return; //Prevents a bug
        }

        Token toRemove = expression.get(mDisplay.getRealCursorIndex() - 1);
        Log.d(TAG, "clickBack: " + toRemove);

        //Can not remove superscript close Brackets
        boolean isBracket = toRemove instanceof BracketToken;
        if (isBracket && toRemove.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.SUPERSCRIPT_OPEN) { //Removes whatever was before it instead
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 2);
        } else if (isBracket && toRemove.getType() == BracketToken.NUMERATOR_OPEN) {
            if (mDisplay.getRealCursorIndex() - 3 < 0) {
                return;
            }
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 3);
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
        } else if (isBracket && toRemove.getType() == BracketToken.FRACTION_OPEN) {
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 2);
        } else if (isBracket && toRemove.getType() == BracketToken.DENOMINATOR_OPEN) {
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 2);
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
        } else if (isBracket && toRemove.getType() == BracketToken.FRACTION_CLOSE) {
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.SQRT_CLOSE) {
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1); //don't remove sqrt
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.SQRT_OPEN) {
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 2);

        } else if (isBracket && toRemove.getType() == BracketToken.ABS_CLOSE) {
            // |[]|
            //don't remove abs,
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.LOGN_CLOSE) {
            //don't remove close bracket of log_n([]), move to between open and close parentheses
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.ABS_OPEN) {
            //remove abs function, include open and close bracket
            toRemove = expression.get(mDisplay.getRealCursorIndex() - 2);

        } else if (isBracket && toRemove.getType() == BracketToken.LOGN_OPEN) {
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
            updateInput();
            return;
        } else if (isBracket && toRemove.getType() == BracketToken.SUBSCRIPT_OPEN) {
            if (mDisplay.getRealCursorIndex() >= 2) {
                Token last = expression.get(mDisplay.getRealCursorIndex() - 2);
                if (last instanceof FunctionToken && last.getType() == FunctionToken.LOG_N) {
                    toRemove = last;
                }
            }
        }

        expression.remove(toRemove);

        //Removes any dependencies
        for (Token t : toRemove.getDependencies()) {
            expression.remove(t);
        }

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() - 1);
        changedTokens = true; //used to know if the button has been used
        updateInput();
    }

    /**
     * When the user presses the negative Button.
     */
    public void clickNegative() {
        expression.add(mDisplay.getRealCursorIndex(), DigitFactory.makeNegative());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }


    /**
     * Called when an exception occurs anywhere during processing.
     *
     * @param e The exception that was thrown
     */
    public void handleExceptions(Exception e) {
        mPresenter.handleExceptions(e);
    }

    /**
     * Saves the equation into the calculation history.
     *
     * @param input  The expression that the user inputted into the calculator
     * @param output The result of the calculation
     */
    public void saveEquation(ArrayList<Token> input, ArrayList<Token> output, String filepath)
            throws IOException, ClassNotFoundException {
        ArrayList<Object[]> history = new ArrayList<>();
        try {
            FileInputStream inStream = fragment.getContext().openFileInput(filepath);
            ObjectInputStream objectStreamIn = new ObjectInputStream(inStream);
            history = (ArrayList<Object[]>) objectStreamIn.readObject();
        } catch (Exception e) {
        }

        FileOutputStream outStream = fragment.getContext().openFileOutput(filepath, Context.MODE_PRIVATE);
        Object[] toWrite = new Object[2];
        toWrite[0] = input;
        toWrite[1] = output;
        history.add(toWrite);

        while (history.size() > HISTORY_SIZE) {
            history.remove(0);
        }

        ObjectOutputStream objectStreamOut = new ObjectOutputStream(outStream);
        objectStreamOut.writeObject(history);
        objectStreamOut.flush();
        objectStreamOut.close();
        outStream.close();
    }

    /**
     * Opens the calculation history.
     *
     * @param filename The file name of the history file
     */
    public void openHistory(String filename) {
     /*   Intent intent = new Intent(activity, HistoryActivity.class);
        intent.putExtra(HistoryActivity.FILENAME, filename);
        activity.startActivity(intent);*/
        // TODO: 24-Jun-17
    }

    /**
     * Updates the text on the input screen.
     */
    public void updateInput() {
        updatePlaceHolders();
        mDisplay.displayOutput(new ArrayList<Token>()); //Clears output
        mDisplay.displayInput(expression);
    }


    /**
     * Removes any placeholders that are no longer necessary, or adds them
     * if they are.
     */
    public void updatePlaceHolders() {

        int i = 0;
        while (i < expression.size()) {
            Token current = expression.get(i);
            Token previous = i - 1 < 0 ? null : expression.get(i - 1);
            if (current instanceof BracketToken && previous instanceof BracketToken) {
                if (((current.getType() == BracketToken.NUMERATOR_CLOSE) && (previous.getType() == BracketToken.NUMERATOR_OPEN))
                        || ((current.getType() == BracketToken.DENOMINATOR_CLOSE) && (previous.getType() == BracketToken.DENOMINATOR_OPEN))
                        || ((current.getType() == BracketToken.DERIVATIVE_CLOSE) && (previous.getType() == BracketToken.DERIVATIVE_OPEN))
                        || ((current.getType() == BracketToken.LOGN_CLOSE) && (previous.getType() == BracketToken.LOGN_OPEN))
                        || ((current.getType() == BracketToken.SQRT_CLOSE) && (previous.getType() == BracketToken.SQRT_OPEN))
                        || ((current.getType() == BracketToken.ABS_CLOSE) && (previous.getType() == BracketToken.ABS_OPEN))
                        || ((current.getType() == BracketToken.SUPERSCRIPT_CLOSE) && (previous.getType() == BracketToken.SUPERSCRIPT_OPEN))
                        || ((current.getType() == BracketToken.SUBSCRIPT_CLOSE) && (previous.getType() == BracketToken.SUBSCRIPT_OPEN))) {
                    expression.add(i, PlaceholderFactory.makeBaseBlock());
                    continue;
                }
            }
            if (current instanceof OperatorToken && current.getType() == OperatorToken.POWER) {
                if (!(previous instanceof PlaceholderToken)
                        && (previous == null || !(previous instanceof DigitToken || previous instanceof VariableToken || previous instanceof BracketToken
                        && (previous.getType() == BracketToken.PARENTHESES_CLOSE
                        || previous.getType() == BracketToken.DENOMINATOR_CLOSE ||
                        previous.getType() == BracketToken.SQRT_CLOSE ||
                        previous.getType() == BracketToken.ABS_CLOSE)))) {
                    expression.add(i, PlaceholderFactory.makeBaseBlock());
                    continue;
                }
            }

            //Removes Placeholder if it is not needed
            if (current instanceof PlaceholderToken) {
                if (current.getType() == PlaceholderToken.BASE_BLOCK) {
                    if ((previous instanceof DigitToken)
                            || (previous instanceof VariableToken)
                            || ((previous instanceof BracketToken) &&
                            ((previous.getType() == BracketToken.PARENTHESES_CLOSE)
                                    || (previous.getType() == BracketToken.DENOMINATOR_CLOSE)
                                    || (previous.getType() == BracketToken.SQRT_CLOSE)
                                    || (previous.getType() == BracketToken.ABS_CLOSE)
                                    || (previous.getType() == BracketToken.LOGN_CLOSE)
                                    || (previous.getType() == BracketToken.FRACTION_CLOSE)))
                            || ((previous instanceof PlaceholderToken) && (
                            previous.getType() == PlaceholderToken.BASE_BLOCK))) {
                        expression.remove(current);
                        continue;
                    }
                    if (i + 1 < expression.size()) {
                        Token next = expression.get(i + 1);
                        if (next instanceof FunctionToken
                                || next instanceof DigitToken
                                || next instanceof VariableToken) {
                            expression.remove(current);
                            continue;
                        }
                    }
                    if (i == expression.size() - 1) {
                        expression.remove(current);
                        continue;
                    }
                }
            }
            i++;
        }
    }

    public ArrayList<Token> getExpression() {
        return expression;
    }

    public void setExpression(ArrayList<Token> expression) {
        this.expression = expression;
        this.mDisplay.displayInput(expression);
    }

    /**
     * When the user presses the MEM button; toggles memory storage
     */
    public void clickMem() {
        memory = !memory;
    }

    /**
     * Stores the a variable into the memory; the assignment itself will occur in the given Command.
     *
     * @param addToOutput The String that will be shown in the output along with the value
     * @param assignment  The assignment command that would be executed
     */
    protected void storeVariable(final String addToOutput, final Command<Void, ArrayList<Token>> assignment) {
        try {
            mPresenter.doCalculate(expression, new BaseThread.ResultCallback() {
                @Override
                public void onSuccess(ArrayList<Token> result) {
                    ArrayList<Token> outputList = new ArrayList<>();
                    outputList.addAll(result);
                    outputList.add(new StringToken(addToOutput));
                    mDisplay.displayOutput(outputList);
                    assignment.execute(result);
                }

                @Override
                public void onError(Exception e) {
                    handleExceptions(e);
                }
            });
            memory = false;
        } catch (Exception e) { //User did a mistake
            handleExceptions(e);
        }
    }

    /**
     * When the user presses the ANS button
     */
    public void clickAns() {
        expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeAnsCompute());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the A button
     */
    public void clickA() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickA();
                clickEquals();
            } else {
                clickA();
            }
        } else if (memory) {
            storeVariable("→A", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.A_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeA());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    /**
     * When the user presses the B button
     */
    public void clickB() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickB();
                clickEquals();
            } else {
                clickB();
            }
        } else if (memory) {
            storeVariable("→B", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.B_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeB());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    /**
     * When the user presses the C button
     */
    public void clickC() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickC();
                clickEquals();
            } else {
                clickC();
            }
        } else if (memory) {
            storeVariable("→C", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.C_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeC());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    public void clickD() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickD();
                clickEquals();
            } else {
                clickD();
            }
        } else if (memory) {
            storeVariable("→D", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.D_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeD());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    public void clickE() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickE();
                clickEquals();
            } else {
                clickE();
            }
        } else if (memory) {
            storeVariable("→E", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.E_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeE());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    public void clickF() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickF();
                clickEquals();
            } else {
                clickF();
            }
        } else if (memory) {
            storeVariable("→F", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.F_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeF());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    public void clickM() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickM();
                clickEquals();
            } else {
                clickM();
            }
        } else if (memory) {
            storeVariable("→M", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.M_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeM());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    public void clickX() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickX();
                clickEquals();
            } else {
                clickX();
            }
        } else if (memory) {
            storeVariable("→X", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.X_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeX());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    /**
     * When the user presses the Y button
     */
    public void clickY() {
        if (recall) {
            recall = false;
            if (expression.size() == 0) {
                clickY();
                clickEquals();
            } else {
                clickY();
            }
        } else if (memory) {
            storeVariable("→Y", new Command<Void, ArrayList<Token>>() {
                @Override
                public Void execute(ArrayList<Token> val) {
                    VariableFactory.Y_Value = val;
                    return null;
                }
            });
        } else {
            expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makeY());
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        }
    }

    /**
     * When the user presses the ( Button.
     */
    public void clickOpenParentheses() {
        expression.add(mDisplay.getRealCursorIndex(), BracketFactory.makeOpenParentheses());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the ) Button.
     */
    public void clickCloseParentheses() {
        expression.add(mDisplay.getRealCursorIndex(), BracketFactory.makeCloseParentheses());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the 10^x Button.
     */
    public void clickPowerOfTen() {
        Token one = DigitFactory.makeOne();
        Token zero = DigitFactory.makeZero();
        Token exp = OperatorFactory.makePower();
        Token openBracket = BracketFactory.makeSuperscriptOpen();
        Token closeBracket = BracketFactory.makeSuperscriptClose();

        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, one);
        expression.add(realCursorIndex + 1, zero);
        expression.add(realCursorIndex + 2, exp);
        expression.add(realCursorIndex + 3, openBracket);
        expression.add(realCursorIndex + 4, PlaceholderFactory.makeBaseBlock());
        expression.add(realCursorIndex + 5, closeBracket);

        exp.addDependency(openBracket);
        exp.addDependency(closeBracket);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 3);
        updateInput();
    }


    /**
     * When the user presses the ln(x) Button.
     */
    public void clickLn() {
        FunctionToken ln = FunctionFactory.makeLn();
        BracketToken b = BracketFactory.makeOpenParentheses();

        ln.addDependency(b);
        b.addDependency(ln);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, ln);
        expression.add(cursorIndex + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the log(x) or log_10(x)Button.
     */
    public void clickLog_10() {
        Token t = FunctionFactory.makeLog_10();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, t);
        expression.add(cursorIndex + 1, b);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);

        updateInput();
    }

    /**
     * when user press the ^ button
     */
    public void clickPower() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(PlaceholderFactory.makeBaseBlock());
        addTokenInExponent(tokens);
    }

    public void clickSurd() {
        Token superscriptOpen = BracketFactory.makeSuperscriptOpen();
        Token superscriptClose = BracketFactory.makeSuperscriptClose();

        BracketToken sqrtOpen = BracketFactory.makeSqrtOpen();
        BracketToken sqrtClose = BracketFactory.makeSqrtClose();

        Token surd = OperatorFactory.makeSurd();
        surd.addDependency(superscriptOpen);
        surd.addDependency(superscriptClose);
        surd.addDependency(sqrtOpen);
        surd.addDependency(sqrtClose);

        int realCursorIndex = mDisplay.getRealCursorIndex();
        if (realCursorIndex != 0) {
            //Whats on the numerator depends on the token before
            Token before = expression.get(realCursorIndex - 1);
            if (before instanceof DigitToken) {
                LinkedList<DigitToken> digits = new LinkedList<>();
                int i = realCursorIndex - 1;
                while (i >= 0 && expression.get(i) instanceof DigitToken) {
                    Token t = expression.get(i);
                    digits.addFirst((DigitToken) t);
                    expression.remove(t);
                    i--;
                }

                expression.add(realCursorIndex - digits.size(), superscriptOpen);
                expression.addAll(realCursorIndex - digits.size() + 1, digits);
                expression.add(realCursorIndex + 1, superscriptClose);
                expression.add(realCursorIndex + 2, surd);
                expression.add(realCursorIndex + 3, sqrtOpen);
                expression.add(realCursorIndex + 4, PlaceholderFactory.makeBaseBlock());
                expression.add(realCursorIndex + 4, sqrtClose);

                mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
                updateInput();
            } else if (before instanceof BracketToken && before.getType() == BracketToken.PARENTHESES_CLOSE) {
                LinkedList<Token> expr = new LinkedList<Token>();
                int i = realCursorIndex - 2;
                int bracketCount = 1;
                expr.add(expression.remove(realCursorIndex - 1));
                while (i >= 0 && bracketCount != 0) {
                    Token t = this.expression.remove(i);
                    if (t instanceof BracketToken) {
                        BracketToken bracket = (BracketToken) t;
                        if (bracket.getType() == BracketToken.PARENTHESES_OPEN) {
                            bracketCount--;
                        } else if (bracket.getType() == BracketToken.PARENTHESES_CLOSE) {
                            bracketCount++;
                        }
                    }
                    expr.addFirst(t);
                    i--;
                }

                expression.add(i + 1, superscriptOpen);
                expression.addAll(i + 2, expr);
                expression.add(realCursorIndex + 1, superscriptClose);
                expression.add(realCursorIndex + 2, surd);
                expression.add(realCursorIndex + 3, sqrtOpen);
                expression.add(realCursorIndex + 4, PlaceholderFactory.makeBaseBlock());
                expression.add(realCursorIndex + 4, sqrtClose);
                mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
                updateInput();
            }
        } else {
            expression.add(realCursorIndex, superscriptOpen);
            expression.add(realCursorIndex + 1, PlaceholderFactory.makeBaseBlock());
            expression.add(realCursorIndex + 2, superscriptClose);
            expression.add(realCursorIndex + 3, surd);
            expression.add(realCursorIndex + 4, sqrtOpen);
            expression.add(realCursorIndex + 5, PlaceholderFactory.makeBaseBlock());
            expression.add(realCursorIndex + 6, sqrtClose);
            mDisplay.setCursorIndex(mDisplay.getCursorIndex());
            updateInput();
        }
    }


    /**
     * When the user presses the x^2 Button.
     */
    public void clickSquare() {
        ArrayList<Token> list = new ArrayList<>();
        list.add(DigitFactory.makeTwo());
        addTokenInExponent(list);
    }

    /**
     * When the user presses the x^3 Button.
     */
    public void clickCube() {
        ArrayList<Token> list = new ArrayList<>();
        list.add(DigitFactory.makeThree());
        addTokenInExponent(list);
    }

    /**
     * When the user presses the sqrt Button.
     */
    @Override
    public void clickSqrt() {
        Token root = FunctionFactory.makeSqrt();

        BracketToken sqrtOpen = BracketFactory.makeSqrtOpen();
        BracketToken sqrtClose = BracketFactory.makeSqrtClose();

        PlaceholderToken baseBlock = PlaceholderFactory.makeBaseBlock();
        root.addDependency(sqrtOpen);
        root.addDependency(baseBlock);
        root.addDependency(sqrtClose);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, root);
        expression.add(cursorIndex + 1, sqrtOpen);
        expression.add(cursorIndex + 2, baseBlock);
        expression.add(cursorIndex + 3, sqrtClose);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the cuberoot Button.
     */
    public void clickCbrt() {
        Token root = OperatorFactory.makeSurd();

        Token superscriptOpen = BracketFactory.makeSuperscriptOpen();
        Token superscriptClose = BracketFactory.makeSuperscriptClose();

        BracketToken sqrtOpen = BracketFactory.makeSqrtOpen();
        BracketToken sqrtClose = BracketFactory.makeSqrtClose();

        root.addDependency(superscriptOpen);
        root.addDependency(superscriptClose);
        root.addDependency(sqrtOpen);
        root.addDependency(sqrtClose);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, superscriptOpen);
        expression.add(cursorIndex + 1, DigitFactory.makeThree());
        expression.add(cursorIndex + 2, superscriptClose);
        expression.add(cursorIndex + 3, root);
        expression.add(cursorIndex + 4, sqrtOpen);
        expression.add(cursorIndex + 5, PlaceholderFactory.makeBaseBlock());
        expression.add(cursorIndex + 6, sqrtClose);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }


    /**
     * Gets the index where the fraction end.
     *
     * @param expression The expression to look at
     * @param current    The index where the closing bracket would be placed
     * @return The index where the NUMERATOR_OPEN bracket should go
     */
    private int getFracEnd(List<Token> expression, int current) {
        Token tokenBefore = expression.get(current + 1);
        if (tokenBefore instanceof DigitToken || tokenBefore instanceof VariableToken) {
            int i = current + 1;
            if (tokenBefore instanceof VariableToken) {
                while (i < expression.size() && expression.get(i) instanceof VariableToken) {
                    i++;
                }
            } else { //Digit
                while (i < expression.size() && expression.get(i) instanceof DigitToken) {
                    i++;
                }
            }
            return i + 1;
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.PARENTHESES_CLOSE) {
            int i = current - 2;
            int bracketCount = 1;
            while (i >= 0 && bracketCount != 0) {
                Token t = expression.get(i);
                if (t instanceof BracketToken) {
                    BracketToken b = (BracketToken) t;
                    if (b.getType() == BracketToken.PARENTHESES_OPEN) {
                        bracketCount--;
                    } else if (b.getType() == BracketToken.PARENTHESES_CLOSE) {
                        bracketCount++;
                    }
                }
                i--;
            }
            //Includes the function if there is one
            if (i >= 0 && expression.get(i) instanceof FunctionToken) {
                i--;
            }
            return i + 1;
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
            //Goes to the token before the start of the superscript
            int i = current - 2;
            int bracketCount = 1;
            while (i >= 0 && bracketCount != 0) {
                Token t = expression.get(i);
                if (t instanceof BracketToken) {
                    BracketToken b = (BracketToken) t;
                    if (b.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                        bracketCount--;
                    } else if (b.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        bracketCount++;
                    }
                }
                i--;
            }
            //Frac will start at whatever it wouldve been at what the exponent is over
            return getFractionStart(expression, i);
        } else {
            return current;
        }
    }

    /**
     * When the user presses the FRACTION Button.
     */
    @Override
    public void clickFraction() {
        Token fractionOpen = BracketFactory.makeFractionOpen();
        Token fractionClose = BracketFactory.makeFractionClose();
        Token fraction = OperatorFactory.makeFraction();
        Token numeratorOpen = BracketFactory.makeNumeratorOpen();
        Token numeratorClose = BracketFactory.makeNumeratorClose();
        Token denominatorOpen = BracketFactory.makeDenominatorOpen();
        Token denominatorClose = BracketFactory.makeDenominatorClose();

        fraction.addDependency(numeratorOpen);
        fraction.addDependency(numeratorClose);
        fraction.addDependency(denominatorOpen);
        fraction.addDependency(denominatorClose);
        fraction.addDependency(fractionOpen);
        fraction.addDependency(fractionClose);

        PlaceholderToken p = PlaceholderFactory.makeBaseBlock();
        fraction.addDependency(p);

        int realCursorIndex = mDisplay.getRealCursorIndex();
        if (realCursorIndex == 0) { //Empty Expression
            expression.add(realCursorIndex, fractionOpen);
            expression.add(realCursorIndex + 1, numeratorOpen);
            expression.add(realCursorIndex + 2, PlaceholderFactory.makeBaseBlock());
            expression.add(realCursorIndex + 3, numeratorClose);
            expression.add(realCursorIndex + 4, fraction);
            expression.add(realCursorIndex + 5, denominatorOpen);
            expression.add(realCursorIndex + 6, p);
            expression.add(realCursorIndex + 7, denominatorClose);
            expression.add(realCursorIndex + 8, fractionClose);

            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
            updateInput();
        } else {
            int startIndex = getFractionStart(expression, realCursorIndex);
            //Removes the numerator from the expression
            ArrayList<Token> inside = new ArrayList<>();
            for (int i = 0; i < realCursorIndex - startIndex; i++) {
                inside.add(expression.remove(startIndex));
            }
            expression.add(startIndex, fractionOpen);
            expression.add(startIndex + 1, numeratorOpen);
            expression.addAll(startIndex + 2, inside);
            expression.add(realCursorIndex + 2, numeratorClose);
            expression.add(realCursorIndex + 3, fraction);
            expression.add(realCursorIndex + 4, denominatorOpen);
            expression.add(realCursorIndex + 5, p);
            expression.add(realCursorIndex + 6, denominatorClose);
            expression.add(realCursorIndex + 7, fractionClose);

            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
            updateInput();
        }
    }

    /**
     * When the user presses the x^-1 button.
     */
    public void clickReciprocal() {
        ArrayList<Token> list = new ArrayList<>();
        list.add(DigitFactory.makeNegative());
        list.add(DigitFactory.makeOne());
        addTokenInExponent(list);
    }

    /**
     * When the user presses the nCk Button.
     */
    public void clickCombination() {
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makeCombination());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the nPk Button.
     */
    public void clickPermutation() {
      /*  BracketToken open1 = BracketFactory.makePermOpen();
        BracketToken open2 = BracketFactory.makePermOpen();
        BracketToken close1 = BracketFactory.makePermClose();
        BracketToken close2 = BracketFactory.makePermClose();

        OperatorToken permutation = OperatorFactory.makePermutation();
        permutation.addDependency(open1);
        permutation.addDependency(open2);
        permutation.addDependency(close1);
        permutation.addDependency(close2);
        int cursorIndex = mDisplay.getRealCursorIndex();
        if (expression.size() == 0) {
            expression.add(cursorIndex, open1);
            expression.add(cursorIndex + 1, close1);
            expression.add(cursorIndex + 2, permutation);
            expression.add(cursorIndex + 3, open2);
            expression.add(cursorIndex + 4, close2);
        } else {
            if (cursorIndex == 0) { //Empty Expression
                expression.add(cursorIndex, open1);
                expression.add(cursorIndex + 1, close1);
                expression.add(cursorIndex + 2, permutation);
                expression.add(cursorIndex + 3, open2);
                expression.add(cursorIndex + 4, close2);
            } else {
                int startIndex = getFractionStart(expression, cursorIndex);
                //Removes the numerator from the expression
                ArrayList<Token> inside = new ArrayList<>();
                for (int i = 0; i < cursorIndex - startIndex; i++) {
                    inside.add(expression.remove(startIndex));
                }
                expression.add(startIndex, open1);
                expression.addAll(startIndex + 1, inside);
                expression.add(cursorIndex + 1, close1);
                expression.add(cursorIndex + 2, permutation);
                expression.add(cursorIndex + 3, open2);
                expression.add(cursorIndex + 4, close2);
            }
            mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
            updateInput();
        }*/
        expression.add(mDisplay.getRealCursorIndex(), OperatorFactory.makePermutation());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the e Button.
     */
    public void click_e() {
        expression.add(mDisplay.getRealCursorIndex(), ConstantFactory.makeExponent());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the pi Button.
     */
    public void clickPi() {
        expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makePI());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    /**
     * When the user presses the sin(x) Button.
     */
    public void clickSin() {
        Token t = FunctionFactory.makeSin();
        BracketToken b = BracketFactory.makeOpenParentheses();
        if (t != null) {
            t.addDependency(b);
            b.addDependency(t);
        }
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, t);
        expression.add(realCursorIndex + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the arcsin(x) or sin^-1(x) Button.
     */
    public void clickASin() {
        Token t = FunctionFactory.makeASin();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, t);
        expression.add(realCursorIndex + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the cos(x) Button.
     */
    public void clickCos() {
        Token t = FunctionFactory.makeCos();
        BracketToken b = BracketFactory.makeOpenParentheses();
        if (t != null) {
            t.addDependency(b);
            b.addDependency(t);
        }
        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the arccos(x) or cos^-1(x) Button.
     */
    public void clickACos() {
        Token t = FunctionFactory.makeACos();
        BracketToken b = BracketFactory.makeOpenParentheses();
        t.addDependency(b);
        b.addDependency(t);
        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the tan(x) Button.
     */
    public void clickTan() {
        Token t = FunctionFactory.makeTan();
        BracketToken b = BracketFactory.makeOpenParentheses();
        t.addDependency(b);
        b.addDependency(t);
        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the arctan(x) or tan^-1(x) Button.
     */
    public void clickATan() {
        Token t = FunctionFactory.makeATan();
        BracketToken b = BracketFactory.makeOpenParentheses();
        t.addDependency(b);
        b.addDependency(t);
        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the sinh(x) Button.
     */
    public void clickSinh() {
        Token t = FunctionFactory.makeSinh();
        BracketToken b = BracketFactory.makeOpenParentheses();
        t.addDependency(b);
        b.addDependency(t);

        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, t);
        expression.add(realCursorIndex + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the arsinh(x) Button.
     */
    public void clickASinh() {
        Token t = FunctionFactory.makeASinh();
        BracketToken b = BracketFactory.makeOpenParentheses();
        t.addDependency(b);
        b.addDependency(t);
        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the cosh(x) Button.
     */
    public void clickCosh() {
        Token t = FunctionFactory.makeCosh();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the arcosh(x) Button.
     */
    public void clickACosh() {
        Token t = FunctionFactory.makeACosh();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the tanh(x) Button.
     */
    public void clickTanh() {
        Token t = FunctionFactory.makeTanh();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user presses the artanh(x) Button.
     */
    public void clickATanh() {
        Token t = FunctionFactory.makeATanh();
        BracketToken b = BracketFactory.makeOpenParentheses();

        t.addDependency(b);
        b.addDependency(t);

        expression.add(mDisplay.getRealCursorIndex(), t);
        expression.add(mDisplay.getRealCursorIndex() + 1, b);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    /**
     * When the user clicks the const button.
     */
    public void clickConst() {
        mPresenter.getConstant(new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<Token> result) {
                expression.add(mDisplay.getRealCursorIndex(), result.get(0));
                mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
                updateInput();
            }

            @Override
            public void onError(Exception e) {
                handleExceptions(e);
            }
        });
    }


    /**
     * Adds all the expression in the expression into an exponent.
     *
     * @param toAdd The expression to add
     */
    private void addTokenInExponent(ArrayList<Token> toAdd) {
        Token power = OperatorFactory.makePower();
        Token superscriptOpen = BracketFactory.makeSuperscriptOpen();
        Token superscriptClose = BracketFactory.makeSuperscriptClose();

        power.addDependency(superscriptOpen);
        power.addDependency(superscriptClose);

        //Determines if a placeholder should be placed
        Token lastToken;
        int realCursorIndex = mDisplay.getRealCursorIndex();
        if (expression.size() == 0) {
            lastToken = null;
        } else {
            if (realCursorIndex == 0) {
                lastToken = null;
            } else {
                lastToken = expression.get(realCursorIndex - 1);
            }
        }

        int cursorPos;
        if (!((lastToken instanceof NumberToken) || (lastToken instanceof VariableToken)
                || lastToken instanceof DigitToken
                || ((lastToken instanceof BracketToken) && ((lastToken.getType() == BracketToken.PARENTHESES_CLOSE)
                || (lastToken.getType() == BracketToken.DENOMINATOR_CLOSE)
                || (lastToken.getType() == BracketToken.SQRT_CLOSE))))) {
            expression.add(realCursorIndex, PlaceholderFactory.makeBaseBlock());
            realCursorIndex++;
            cursorPos = mDisplay.getCursorIndex();
        } else {
            cursorPos = mDisplay.getCursorIndex() + 1;
        }
        expression.add(realCursorIndex, power);
        expression.add(realCursorIndex + 1, superscriptOpen);
        expression.addAll(realCursorIndex + 2, toAdd);
        expression.add(realCursorIndex + 2 + toAdd.size(), superscriptClose);

        mDisplay.setCursorIndex(cursorPos);
        updateInput();

    }

    public boolean isShift() {
        return buttonMode == ButtonMode.SHIFT;
    }

    public boolean isMemory() {
        return memory;
    }

    public boolean isHyperbolic() {
        return buttonMode == ButtonMode.HYP;
    }


    public void clickMPlus() {
        // TODO: 23-Jun-17
    }

    public void clickMMinus() {
        // TODO: 23-Jun-17
    }

    public void clickPercent() {
        Token token = OperatorFactory.makePercent();
        expression.add(token);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickCommaSign() {
        expression.add(PlaceholderFactory.makeComma());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickAbs() {
        Token abs = FunctionFactory.makeAbs();
        BracketToken open = BracketFactory.makeAbsOpen();
        BracketToken close = BracketFactory.makeAbsClose();
        PlaceholderToken baseBlock = PlaceholderFactory.makeBaseBlock();

        abs.addDependency(open);
        abs.addDependency(close);
        abs.addDependency(baseBlock);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, abs);
        expression.add(cursorIndex + 1, open);
        expression.add(cursorIndex + 2, baseBlock);
        expression.add(cursorIndex + 3, close);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);

        updateInput();
    }

    public void clickDegree() {
        expression.add(SymbolFactory.makeDegree());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }


    public void clickRcl() {
        recall = !recall;
        if (recall) {
            buttonMode = ButtonMode.STORE; //or RCL
        } else {
            buttonMode = ButtonMode.NORMAL;
        }
        mPresenter.getKeyboard().updateMultiButtons(buttonMode);
        updateStatusMode();
    }

    private void updateStatusMode() {
        // TODO: 26-Jun-17
    }

    public void clickStore() {
        buttonMode = ButtonMode.STORE;
        memory = true;
        mPresenter.getKeyboard().updateMultiButtons(buttonMode);
    }

    public void clickBin() {
        // TODO: 23-Jun-17
    }

    public void clickOctal() {
        // TODO: 23-Jun-17
    }

    public void clickPowerOfE() {
        ConstantToken exponent = ConstantFactory.makeExponent();
        Token power = OperatorFactory.makePower();
        Token openBracket = BracketFactory.makeSuperscriptOpen();
        Token closeBracket = BracketFactory.makeSuperscriptClose();

        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, exponent);
        expression.add(realCursorIndex + 1, power);
        expression.add(realCursorIndex + 2, openBracket);
        expression.add(realCursorIndex + 3, PlaceholderFactory.makeBaseBlock());
        expression.add(realCursorIndex + 4, closeBracket);

        power.addDependency(openBracket);
        power.addDependency(closeBracket);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void clickAlpha() {
        if (buttonMode == ButtonMode.ALPHA) {
            buttonMode = ButtonMode.NORMAL;
        } else {
            buttonMode = ButtonMode.ALPHA;
        }
        //Changes the buttonMode for all the Buttons
        mPresenter.getKeyboard().updateMultiButtons(buttonMode);
    }

    public void clickFactorial() {
        Token token = OperatorFactory.makeFactorial();
        expression.add(token);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickENG() {
        // TODO: 23-Jun-17
    }

    public void clickImplicit() {
        // TODO: 23-Jun-17
    }

    public void clickComplex() {
        // TODO: 23-Jun-17
    }

    public void clickBase() {
        // TODO: 23-Jun-17
    }

    public void clickMatrix() {
        // TODO: 23-Jun-17
    }

    public void clickVector() {
        // TODO: 23-Jun-17
    }

    public void clickConv() {
        // TODO: 23-Jun-17
    }

    public void clickClr() {
        // TODO: 23-Jun-17
    }

    public void clickRandomReal() {
        FunctionToken func = FunctionFactory.makeRandomReal();
        func.addDependency(func);
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, func);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickRandomInt() {
        FunctionToken func = FunctionFactory.makeRandomInt();
        BracketToken openParentheses = BracketFactory.makeOpenParentheses();
        func.addDependency(openParentheses);
        openParentheses.addDependency(func);
        int realCursorIndex = mDisplay.getRealCursorIndex();

        expression.add(realCursorIndex, func);
        expression.add(realCursorIndex + 1, openParentheses);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void clickLCM() {
        Token lcm = FunctionFactory.makeLCM();
        BracketToken bracketToken = BracketFactory.makeOpenParentheses();
        lcm.addDependency(bracketToken);
        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, lcm);
        expression.add(cursorIndex + 1, bracketToken);

        mDisplay.setCursorIndex(cursorIndex + 2);
        updateInput();
    }

    public void clickGCD() {
        Token gcd = FunctionFactory.makeGCD();
        BracketToken bracketToken = BracketFactory.makeOpenParentheses();
        gcd.addDependency(bracketToken);
        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, gcd);
        expression.add(cursorIndex + 1, bracketToken);

        mDisplay.setCursorIndex(cursorIndex + 2);
        updateInput();
    }

    public void clickInt() {
        FunctionToken func = FunctionFactory.makeInt();
        BracketToken openParentheses = BracketFactory.makeOpenParentheses();
        func.addDependency(openParentheses);
        openParentheses.addDependency(func);
        int realCursorIndex = mDisplay.getRealCursorIndex();

        expression.add(realCursorIndex, func);
        expression.add(realCursorIndex + 1, openParentheses);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void clickFloor() {
        FunctionToken func = FunctionFactory.makeIntg();
        BracketToken openParentheses = BracketFactory.makeOpenParentheses();
        func.addDependency(openParentheses);
        openParentheses.addDependency(func);
        int realCursorIndex = mDisplay.getRealCursorIndex();

        expression.add(realCursorIndex, func);
        expression.add(realCursorIndex + 1, openParentheses);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }


    public void clickDRG() {
        // TODO: 23-Jun-17
    }

    public void clickPreAns() {
        expression.add(mDisplay.getRealCursorIndex(), VariableFactory.makePreAnsCompute());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickMulPowerOfTen() {
        Token multiply = OperatorFactory.makeMultiply();
        Token one = DigitFactory.makeOne();
        Token zero = DigitFactory.makeZero();
        Token openBracket = BracketFactory.makeSuperscriptOpen();
        Token closeBracket = BracketFactory.makeSuperscriptClose();

        Token powerTen = OperatorFactory.makePower();
        powerTen.addDependency(openBracket);
        powerTen.addDependency(closeBracket);

        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, multiply);
        expression.add(realCursorIndex + 1, one);
        expression.add(realCursorIndex + 2, zero);
        expression.add(realCursorIndex + 3, powerTen);
        expression.add(realCursorIndex + 4, openBracket);
        expression.add(realCursorIndex + 5, PlaceholderFactory.makeBaseBlock());
        expression.add(realCursorIndex + 6, closeBracket);


        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 4);
        updateInput();
    }

    public void clickRedundancy() {
        // TODO: 23-Jun-17
    }


    public void clickLogN() {
        Token logn = FunctionFactory.makeLogN();
        BracketToken subscriptOpen = BracketFactory.makeSubscriptOpen();
        BracketToken subscriptClose = BracketFactory.makeSubscriptClose();
        PlaceholderToken subscriptBlock = PlaceholderFactory.makeBaseBlock();
        BracketToken lognOpen = BracketFactory.makeLognOpen();
        BracketToken lognClose = BracketFactory.makeLognClose();

        logn.addDependency(subscriptOpen);
        logn.addDependency(subscriptClose);
        logn.addDependency(lognOpen);
        logn.addDependency(lognClose);

        lognOpen.addDependency(logn);
        lognClose.addDependency(logn);
        subscriptOpen.addDependency(logn);
        subscriptClose.addDependency(logn);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex++, logn);
        expression.add(cursorIndex++, subscriptOpen);
        expression.add(cursorIndex++, subscriptBlock);
        expression.add(cursorIndex++, subscriptClose);
        expression.add(cursorIndex++, lognOpen);
        expression.add(cursorIndex++, PlaceholderFactory.makeBaseBlock());
        expression.add(cursorIndex, lognClose);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickPol() {
        int realCursorIndex = mDisplay.getRealCursorIndex();
        FunctionToken pol = FunctionFactory.makePol();
        BracketToken openParentheses = BracketFactory.makeOpenParentheses();
        pol.addDependency(openParentheses);
        openParentheses.addDependency(pol);

        expression.add(realCursorIndex, pol);
        expression.add(realCursorIndex + 1, openParentheses);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void clickRec() {
        int realCursorIndex = mDisplay.getRealCursorIndex();
        FunctionToken rec = FunctionFactory.makeRec();
        BracketToken openParentheses = BracketFactory.makeOpenParentheses();
        rec.addDependency(openParentheses);
        openParentheses.addDependency(rec);

        expression.add(realCursorIndex, rec);
        expression.add(realCursorIndex + 1, openParentheses);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void clickEqualSymbol() {
        OperatorToken equal = OperatorFactory.makeEqual();
        expression.add(mDisplay.getRealCursorIndex(), equal);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    public void clickQuotient() {
        OperatorToken quotient = OperatorFactory.makeQuotient();
        expression.add(mDisplay.getRealCursorIndex(), quotient);
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }



    public void clickFindRoots() {
        mPresenter.findRoots(expression);
    }

    public void clickDerivative() {
        //((d)/(dx))([])
        Token der = FunctionFactory.makeDerivative();

        BracketToken openParentheses = BracketFactory.makeDerivativeOpen();
        BracketToken closeParentheses = BracketFactory.makeDerivativeClose();

        BracketToken subscriptOpen = BracketFactory.makeSubscriptOpen();
        StringToken x = new StringToken("x=");
        BracketToken subscriptClose = BracketFactory.makeSubscriptClose();

        der.addDependency(openParentheses);
        der.addDependency(closeParentheses);
        der.addDependency(subscriptOpen);
        der.addDependency(subscriptClose);
        der.addDependency(x);

        closeParentheses.addDependency(der);
        openParentheses.addDependency(der);

        int cursorIndex = mDisplay.getRealCursorIndex();
        expression.add(cursorIndex, der);
        expression.add(cursorIndex + 1, openParentheses);
        expression.add(cursorIndex + 2, PlaceholderFactory.makeBaseBlock());
        expression.add(cursorIndex + 3, closeParentheses);
        expression.add(cursorIndex + 4, subscriptOpen);
        expression.add(cursorIndex + 5, x);
        expression.add(cursorIndex + 6, PlaceholderFactory.makeBaseBlock());
        expression.add(cursorIndex + 7, subscriptClose);

        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 2);
        updateInput();
    }

    public void setDisplay(CalculatorContract.Display display) {
        this.mDisplay = display;
    }

    public CalculatorContract.Display getDisplay() {
        return mDisplay;
    }


    /**
     * When the user presses the shift button. Switches the state of the boolean variable shift.
     */
    public void clickShift() {
        if (buttonMode == ButtonMode.SHIFT) {
            buttonMode = ButtonMode.NORMAL;
        } else {
            buttonMode = ButtonMode.SHIFT;
        }
        //Changes the buttonMode for all the Buttons
        mPresenter.getKeyboard().updateMultiButtons(buttonMode);
    }

    public CalculatorContract.Presenter getPresenter() {
        return mPresenter;
    }
}
package com.example.duy.calculator.math_eval.base;

import android.util.Log;

import com.example.duy.calculator.math_eval.Constants;

import org.javia.arity.SyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public class BaseModule extends Module {

    // Used to keep a reference to the cursor in text
    public static final char SELECTION_HANDLE = '\u2620';

    // How many decimal places to approximate base changes
    private final static int PRECISION = 8;
    private static final String TAG = BaseModule.class.getName();
    // Regex to strip out things like "90" from "sin(90)"
    private final String REGEX_NUMBER;
    private final String REGEX_NOT_NUMBER;
    // The current base. Defaults to decimal.
    private Base mBase = Base.DECIMAL;
    // A listener for when the base changes.
    private OnBaseChangeListener mBaseChangeListener;

    public BaseModule(Evaluator evaluator) {
        super(evaluator);

        // Modify the constants to include a fake character, SELECTION_HANDLE
        REGEX_NUMBER = Constants.REGEX_NUMBER
                .substring(0, Constants.REGEX_NUMBER.length() - 1) + SELECTION_HANDLE + "]";
        REGEX_NOT_NUMBER = Constants.REGEX_NOT_NUMBER
                .substring(0, Constants.REGEX_NOT_NUMBER.length() - 1) + SELECTION_HANDLE + "]";
    }

    public Base getBase() {
        return mBase;
    }

    public void setBase(Base base) {
        mBase = base;
        if (mBaseChangeListener != null) mBaseChangeListener.onBaseChange(mBase);
    }

    public String setBase(String input, Base base) throws SyntaxException {
        String text = changeBase(input, mBase, base);
        setBase(base);
        return text;
    }

    /**
     * Updates the text to the new base. This does not set the active base.
     */
    public String changeBase(String text, Base base) throws SyntaxException {
        return changeBase(text, Base.DECIMAL, base);
    }

    /**
     * Updates the text to the new base. This does not set the active base.
     */
    public String changeBase(final String originalText, final Base oldBase, final Base newBase) throws SyntaxException {
        if (oldBase.equals(newBase) || originalText.isEmpty() || originalText.matches(REGEX_NOT_NUMBER)) {
            return originalText;
        }
        Log.d(TAG, "changeBase: " + originalText + "; oldBase " + String.valueOf(oldBase) + "; newBase " + String.valueOf(newBase));

        String[] operations = originalText.split(REGEX_NUMBER);
        String[] numbers = originalText.split(REGEX_NOT_NUMBER);
        String[] translatedNumbers = new String[numbers.length];

        Log.d(TAG, "changeBase: operation " + Arrays.toString(operations) + "; numbers " + Arrays.toString(numbers));

        for (int i = 0; i < numbers.length; i++) {
            if (!numbers[i].isEmpty()) {
                switch (oldBase) {
                    case BINARY:
                        switch (newBase) {
                            case BINARY:
                                break;
                            case DECIMAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 2, 10);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case HEXADECIMAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 2, 16);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case OCTAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 2, 8);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                        }
                        break;
                    case OCTAL:
                        switch (newBase) {
                            case BINARY:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 8, 2);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case OCTAL:
                                break;
                            case DECIMAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 8, 10);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);

                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case HEXADECIMAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 8, 16);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                        }
                        break;
                    case DECIMAL:
                        switch (newBase) {
                            case BINARY:
                                try {

                                    translatedNumbers[i] = newBase(numbers[i], 10, 2);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case DECIMAL:
                                break;
                            case HEXADECIMAL:
                                try {

                                    translatedNumbers[i] = newBase(numbers[i], 10, 16);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case OCTAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 10, 8);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                        }
                        break;
                    case HEXADECIMAL:
                        switch (newBase) {
                            case BINARY:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 16, 2);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);

                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                            case DECIMAL:
                                try {
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                    translatedNumbers[i] = newBase(numbers[i], 16, 10);
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, numbers[i] + " is not a number", e);
                                    throw new SyntaxException();
                                }
                                break;
                            case HEXADECIMAL:
                                break;
                            case OCTAL:
                                try {
                                    translatedNumbers[i] = newBase(numbers[i], 16, 8);
                                    Log.d(TAG, "translatedNumbers " + i + " = " + translatedNumbers[i]);
                                } catch (NumberFormatException e) {
                                    throw new SyntaxException();
                                }
                                break;
                        }
                        break;
                }
            }
        }
        String text = "";
        Object[] o = removeWhitespace(operations);
        Object[] n = removeWhitespace(translatedNumbers);
        if (originalText.substring(0, 1).matches(REGEX_NUMBER)) {
            for (int i = 0; i < o.length && i < n.length; i++) {
                text += n[i];
                text += o[i];
            }
        } else {
            for (int i = 0; i < o.length && i < n.length; i++) {
                text += o[i];
                text += n[i];
            }
        }
        if (o.length > n.length) {
            text += o[o.length - 1];
        } else if (n.length > o.length) {
            text += n[n.length - 1];
        }
//        Log.d(TAG, "changeBase return " + text);
        return text;
    }

    public String newBase(String originalNumber, int originalBase, int base) throws SyntaxException {
        String[] split = originalNumber.split(Pattern.quote(getDecimalPoint() + ""));

        //remove white space. such as "6 "
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        if (split.length == 0) {
            split = new String[1];
            split[0] = "0";
        }
        if (split[0].isEmpty()) {
            split[0] = "0";
        }
        if (originalBase != 10) {
            split[0] = Long.toString(Long.parseLong(split[0], originalBase));
        }
//        Log.d(TAG, "onNewBase " + String.valueOf(base));
        String wholeNumber = "";
        switch (base) {
            case 2:
                wholeNumber = Long.toBinaryString(Long.parseLong(split[0]));
                break;
            case 10:
                wholeNumber = split[0];
                break;
            case 16:
                wholeNumber = Long.toHexString(Long.parseLong(split[0]));
                break;
            case 8:
                wholeNumber = Long.toOctalString(Long.parseLong(split[0]));
                break;
        }
//        Log.d(TAG, "wholeNumber " + wholeNumber);
        if (split.length == 1) return wholeNumber.toUpperCase(Locale.US);

        // Catch overflow (it's a decimal, it can be (slightly) rounded
        if (split[1].length() > 13) {
            split[1] = split[1].substring(0, 13);
        }

        double decimal = 0;
        if (originalBase != 10) {
            String decimalFraction = Long.toString(Long.parseLong(split[1], originalBase)) + "/" + originalBase + "^" + split[1].length();
            decimal = getSolver().eval(decimalFraction);
        } else {
            decimal = Double.parseDouble("0." + split[1]);
        }
        if (decimal == 0) return wholeNumber.toUpperCase(Locale.US);

        String decimalNumber = "";
        for (int i = 0; decimal != 0 && i <= PRECISION; i++) {
            decimal *= base;
            int id = (int) Math.floor(decimal);
            decimal -= id;
            decimalNumber += Integer.toHexString(id);
        }
        return (wholeNumber + getDecimalPoint() + decimalNumber).toUpperCase(Locale.US);
    }

    private Object[] removeWhitespace(String[] strings) {
        ArrayList<String> formatted = new ArrayList<String>(strings.length);
        for (String s : strings) {
            if (s != null && !s.isEmpty()) formatted.add(s);
        }
        return formatted.toArray();
    }

    public String groupSentence(String originalText, int selectionHandle) {
        if (originalText.isEmpty() || originalText.matches(REGEX_NOT_NUMBER)) return originalText;

        if (selectionHandle >= 0 && selectionHandle <= originalText.length()) {
            originalText = originalText.substring(0, selectionHandle) +
                    SELECTION_HANDLE +
                    originalText.substring(selectionHandle);
        }
        String[] operations = originalText.split(REGEX_NUMBER);
        String[] numbers = originalText.split(REGEX_NOT_NUMBER);
        String[] translatedNumbers = new String[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            if (!numbers[i].isEmpty()) {
                translatedNumbers[i] = groupDigits(numbers[i], mBase);
            }
        }
        String text = "";
        Object[] o = removeWhitespace(operations);
        Object[] n = removeWhitespace(translatedNumbers);
        if (originalText.substring(0, 1).matches(REGEX_NUMBER)) {
            for (int i = 0; i < o.length && i < n.length; i++) {
                text += n[i];
                text += o[i];
            }
        } else {
            for (int i = 0; i < o.length && i < n.length; i++) {
                text += o[i];
                text += n[i];
            }
        }
        if (o.length > n.length) {
            text += o[o.length - 1];
        } else if (n.length > o.length) {
            text += n[n.length - 1];
        }

        return text;
    }

    public String groupDigits(String number, Base base) {
        String sign = "";
        if (Evaluator.isNegative(number)) {
            sign = String.valueOf(Constants.MINUS_UNICODE);
            number = number.substring(1);
        }
        String wholeNumber = number;
        String remainder = "";
        // We only group the whole number
        if (number.contains(getDecimalPoint() + "")) {
            if (!number.startsWith(getDecimalPoint() + "")) {
                String[] temp = number.split(Pattern.quote(getDecimalPoint() + ""));
                wholeNumber = temp[0];
                remainder = getDecimalPoint() + ((temp.length == 1) ? "" : temp[1]);
            } else {
                wholeNumber = "";
                remainder = number;
            }
        }

        String modifiedNumber = group(wholeNumber, getSeparatorDistance(base), getSeparator(base));

        return sign + modifiedNumber + remainder;
    }

    private String group(String wholeNumber, int spacing, char separator) {
        StringBuilder sb = new StringBuilder();
        int digitsSeen = 0;

        for (int i = wholeNumber.length() - 1; i >= 0; --i) {
            char curChar = wholeNumber.charAt(i);
            sb.insert(0, curChar);
            if (curChar != SELECTION_HANDLE && i != 0 && !(i == 1 && wholeNumber.charAt(0) == SELECTION_HANDLE)) {
                ++digitsSeen;
                if (digitsSeen > 0 && digitsSeen % spacing == 0) {
                    sb.insert(0, separator);
                }
            }
        }
        return sb.toString();
    }

    public char getSeparator(Base base) {
        switch (base) {
            case DECIMAL:
                return getDecSeparator();
            case BINARY:
                return getBinSeparator();
            case HEXADECIMAL:
                return getHexSeparator();
            case OCTAL:
                return getOctSeparator();
            default:
                return 0;
        }
    }

    public char getSeparator() {
        return getSeparator(mBase);
    }

    private int getSeparatorDistance(Base base) {
        switch (base) {
            case DECIMAL:
                return getDecSeparatorDistance();
            case BINARY:
                return getBinSeparatorDistance();
            case HEXADECIMAL:
                return getHexSeparatorDistance();
            case OCTAL:
                return getOctSeparatorDistance();
            default:
                return -1;
        }
    }


    public OnBaseChangeListener getOnBaseChangeListener() {
        return mBaseChangeListener;
    }

    public void setOnBaseChangeListener(OnBaseChangeListener l) {
        mBaseChangeListener = l;
    }

    public int getBaseNumber(Base base) {
        switch (base) {
            case BINARY:
                return 2;
            case OCTAL:
                return 8;
            case DECIMAL:
                return 10;
            case HEXADECIMAL:
                return 16;
            default:
                return 10;
        }
    }

    public interface OnBaseChangeListener {
        public void onBaseChange(Base newBase);
    }
}

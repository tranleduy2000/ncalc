package com.example.duy.calculator.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.example.duy.calculator.utils.VariableUtil;

import java.util.ArrayList;

/**
 * Created by Duy on 19/7/2016
 */
public class AutoCompleteFunctionEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {

    public AutoCompleteFunctionEditText(Context context) {
        super(context);
        init(context);
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            /**
             * create adapter
             */
            ArrayList<String> mListFunction = VariableUtil.getListFunction(context);
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, mListFunction);
            setAdapter(mAdapter);

            setTokenizer(new FunctionTokenizer());
            setThreshold(1);
        }
    }

    public class FunctionTokenizer implements Tokenizer {
        String token = "!@#$%^&*()_+-={}|[]:'<>/<.?1234567890";

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !token.contains(Character.toString(text.charAt(i - 1)))) {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (token.contains(Character.toString(text.charAt(i - 1)))) {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && token.contains(Character.toString(text.charAt(i - 1)))) {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text;
                }
            }
        }
    }

}

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

package com.duy.ncalc.document.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.duy.calculator.R;
import com.duy.ncalc.document.FunctionSuggestionAdapter;
import com.duy.ncalc.document.MarkdownListDocumentFragment;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duy on 19/7/2016
 */
public class AutoCompleteFunctionEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {

    private final Handler mHandler = new Handler();
    private HighlightWatcher mHighlightWatcher = new HighlightWatcher();
    private boolean isEnableTextListener;
    @Nullable
    private FunctionSuggestionAdapter adapter;
    private Pattern mFunctionPattern;
    private final Runnable mUpdateHighlight = new Runnable() {
        @Override
        public void run() {
            highlight(getEditableText());
        }
    };

    public AutoCompleteFunctionEditText(Context context) {
        super(context);
        init();
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAutoSuggestEnable(boolean enable) {
        if (!enable) {
            setAdapter(null);
        } else {
            init();
        }
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        String[] keyWords = new String[0];
        Context context = getContext();
        try {
            keyWords = context.getAssets().list("doc/functions");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < keyWords.length; i++) {
            if (keyWords[i].endsWith(".md")) {
                keyWords[i] = keyWords[i].substring(0, keyWords[i].length() - 3);
            }
        }
        StringBuilder patternStr = new StringBuilder("\\b(");
        for (int i = 0; i < keyWords.length; i++) {
            patternStr.append(keyWords[i]);
            if (i != keyWords.length - 1) {
                patternStr.append("|");
            }
        }
        patternStr.append(")\\b");
        mFunctionPattern = Pattern.compile(patternStr.toString(),
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        adapter = new FunctionSuggestionAdapter(context, R.layout.list_item_suggest,
                MarkdownListDocumentFragment.loadDocumentStructure(context));
        setAdapter(adapter);
        setTokenizer(new FunctionTokenizer());
        setThreshold(1);
        enableTextChangeListener();
    }

    public void setOnSuggestionClickListener(FunctionSuggestionAdapter.OnSuggestionClickListener onHelpListener) {
        if (adapter != null) {
            adapter.setOnSuggestionClickListener(onHelpListener);
        }
    }

    private void enableTextChangeListener() {
        if (!isEnableTextListener) {
            addTextChangedListener(mHighlightWatcher);
            isEnableTextListener = true;
        }
    }

    private void disableTextChangeListener() {
        this.isEnableTextListener = false;
        removeTextChangedListener(mHighlightWatcher);
    }

    public void highlight(Editable editable) {
        disableTextChangeListener();
        StyleSpan[] spans = editable.getSpans(0, editable.length(), StyleSpan.class);
        for (StyleSpan span : spans) {
            editable.removeSpan(span);
        }

        String s = editable.toString();
        Matcher matcher = mFunctionPattern.matcher(s);
        while (matcher.find()) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            editable.setSpan(styleSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        enableTextChangeListener();
    }

    public class FunctionTokenizer implements Tokenizer {

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && Character.isJavaIdentifierStart(text.charAt(i - 1))) {
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
                if (!Character.isJavaIdentifierStart(text.charAt(i - 1))) {
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

            if (i > 0 && Character.isJavaIdentifierStart(text.charAt(i - 1))) {
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

    class HighlightWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mHandler.removeCallbacks(mUpdateHighlight);
            mHandler.postDelayed(mUpdateHighlight, 1000);
        }
    }
}

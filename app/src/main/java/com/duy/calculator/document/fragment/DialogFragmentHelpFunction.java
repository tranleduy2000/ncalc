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

package com.duy.calculator.document.fragment;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.mukesh.MarkdownView;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Duy on 23-May-17.
 */
public class DialogFragmentHelpFunction extends AppCompatDialogFragment {

    public static final String TAG = DialogFragmentHelpFunction.class.getSimpleName();

    public static DialogFragmentHelpFunction newInstance(String key) {
        Bundle args = new Bundle();
        args.putString("key", key);
        DialogFragmentHelpFunction fragment = new DialogFragmentHelpFunction();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * set full height for dialog
     */
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_help_function, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MarkdownView markdownView = view.findViewById(R.id.markdown_view);
        String key = getArguments().getString("key");
        if (key != null) {
            new LoadDataTask(markdownView, getContext().getAssets()).execute(key);
        }
    }

    private class LoadDataTask extends AsyncTask<String, Void, String> {
        private MarkdownView markdownView;
        private AssetManager assetManager;

        LoadDataTask(MarkdownView markdownView, AssetManager assetManager) {
            this.markdownView = markdownView;
            this.assetManager = assetManager;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground() called with: params = [" + Arrays.toString(params) + "]");
            String key = params[0] + ".md";
            try {
                String[] functions = assetManager.list("doc/functions");
                for (String function : functions) {
                    if (function.equalsIgnoreCase(key)) {
                        return "doc/functions/" + function;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute() called with: s = [" + s + "]");

            if (s != null) {
                markdownView.loadMarkdownFromAssets(s);
            }
        }
    }

}

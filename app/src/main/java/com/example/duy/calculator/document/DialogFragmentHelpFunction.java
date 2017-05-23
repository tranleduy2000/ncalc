/*
 * Copyright 2017 Tran Le Duy
 *
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

package com.example.duy.calculator.document;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.R;
import com.mukesh.MarkdownView;

import java.io.IOException;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_help_function, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MarkdownView markdownView = (MarkdownView) view.findViewById(R.id.markdown_view);
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
            try {
                String[] functions = assetManager.list("functions");
                for (String function : functions) {
                    if (function.equalsIgnoreCase(params[0])) {
                        return "functions/" + function;
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
            if (s != null) {
                markdownView.loadMarkdownFromAssets(s);
            }
        }
    }

}

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

package com.duy.calculator.document;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.mukesh.MarkdownView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Duy on 23-May-17.
 */
public class MarkdownFragment extends Fragment {

    public static final String TAG = MarkdownFragment.class.getSimpleName();
    public static final String EXTRA_PATH = "MarkdownFragment.EXTRA_PATH";
    private MarkdownView markdownView;

    /**
     * @param relativePath relative path of doc/functions
     */
    public static MarkdownFragment newInstance(String relativePath) {
        Bundle args = new Bundle();
        args.putString(EXTRA_PATH, relativePath);
        MarkdownFragment fragment = new MarkdownFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_markdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markdownView = view.findViewById(R.id.markdown_view);
        if (getArguments() != null) {
            String relativePath = getArguments().getString(EXTRA_PATH);
            if (relativePath != null) {
                loadContent(relativePath);
            }
        }
    }

    private void loadContent(String documentName) {
        if (!documentName.endsWith(".md")) {
            documentName = documentName + ".md";
        }
        String path = searchAssetPath(new String[]{"doc/functions", "doc", ""}, documentName);
        if (path != null) {
            markdownView.loadMarkdownFromAssets(path);
        }
    }

    @Nullable
    private String searchAssetPath(String[] paths, String expectedFileName) {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        for (String path : paths) {
            try {
                String fullPath = path + (!path.isEmpty() ? "/" : "") + expectedFileName;
                InputStream inputStream = context.getAssets().open(fullPath);
                // File exist
                inputStream.close();
                return fullPath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

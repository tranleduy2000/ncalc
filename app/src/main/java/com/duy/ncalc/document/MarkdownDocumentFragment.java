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

package com.duy.ncalc.document;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.duy.ncalc.document.view.MarkdownDocumentView;

import static com.duy.ncalc.document.MarkdownListDocumentFragment.KEY_ASSET_PATHS;

/**
 * Created by Duy on 23-May-17.
 */
public class MarkdownDocumentFragment extends Fragment {

    public static final String TAG = "MarkdownDocumentFragment";

    public static MarkdownDocumentFragment newInstance(String assetPath) {

        Bundle args = new Bundle();
        args.putString(KEY_ASSET_PATHS, assetPath);
        MarkdownDocumentFragment fragment = new MarkdownDocumentFragment();
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
        MarkdownDocumentView documentView = view.findViewById(R.id.markdown_view);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String documentPath = arguments.getString(KEY_ASSET_PATHS);
            documentView.loadMarkdownFromAssets(documentPath);
        }
    }

}

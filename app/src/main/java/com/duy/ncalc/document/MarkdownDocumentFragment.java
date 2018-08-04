package com.duy.ncalc.document;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.duy.ncalc.view.DocumentView;

import static com.duy.ncalc.document.MarkdownListDocumentFragment.KEY_ASSET_PATH;

public class MarkdownDocumentFragment extends BottomSheetDialogFragment {
    public static MarkdownDocumentFragment newInstance(String assetPath) {

        Bundle args = new Bundle();
        args.putString(KEY_ASSET_PATH, assetPath);
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
        DocumentView documentView = view.findViewById(R.id.markdown_view);
        String documentPath = getArguments().getString(KEY_ASSET_PATH);
        documentView.loadMarkdownFromAssets(documentPath);
    }
}

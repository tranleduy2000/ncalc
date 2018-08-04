package com.duy.ncalc.document;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MarkdownListDocumentFragment extends Fragment implements MarkdownListDocumentAdapter.OnDocumentClickListener, MaterialSearchView.OnQueryTextListener {
    public static final String KEY_ASSET_PATH = "KEY_ASSET_PATH";
    private MarkdownListDocumentAdapter mAdapter;

    public static MarkdownListDocumentFragment newInstance(String assetPath) {

        Bundle args = new Bundle();
        args.putString(KEY_ASSET_PATH, assetPath);
        MarkdownListDocumentFragment fragment = new MarkdownListDocumentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_documents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String assetPath = getArguments().getString(KEY_ASSET_PATH);

        mAdapter = new MarkdownListDocumentAdapter(getContext(), assetPath);
        mAdapter.setOnDocumentClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onDocumentClick(String path) {
        MarkdownDocumentFragment fragment = MarkdownDocumentFragment.newInstance(path);
        fragment.show(getFragmentManager(), MarkdownDocumentFragment.class.getName());
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.query(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.query(newText);
        return false;
    }
}

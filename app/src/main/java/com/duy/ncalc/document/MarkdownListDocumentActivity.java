package com.duy.ncalc.document;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import static com.duy.ncalc.document.MarkdownListDocumentFragment.KEY_ASSET_PATH;

public class MarkdownListDocumentActivity extends BaseActivity
        implements MaterialSearchView.OnQueryTextListener {
    private MaterialSearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(KEY_ASSET_PATH)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_document);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.document);

        final String assetPath = intent.getStringExtra(KEY_ASSET_PATH);
        final String tag = MarkdownListDocumentFragment.class.getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MarkdownListDocumentFragment.newInstance(assetPath), tag)
                .commit();

        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_documents, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        FragmentManager fm = getSupportFragmentManager();
        MarkdownListDocumentFragment fragment = (MarkdownListDocumentFragment) fm.findFragmentByTag(MarkdownListDocumentFragment.class.getName());
        return fragment != null && fragment.onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        FragmentManager fm = getSupportFragmentManager();
        MarkdownListDocumentFragment fragment = (MarkdownListDocumentFragment) fm.findFragmentByTag(MarkdownListDocumentFragment.class.getName());
        return fragment != null && fragment.onQueryTextChange(newText);
    }
}

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

package com.duy.calculator.document.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.AbstractAppCompatActivity;
import com.duy.calculator.document.DocumentAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mukesh.MarkdownView;

import butterknife.ButterKnife;

/**
 * Created by Duy on 19-May-17.
 */

public class DocumentActivity extends AbstractAppCompatActivity implements MaterialSearchView.OnQueryTextListener, DocumentAdapter.OnDocumentClickListener {
    private MaterialSearchView searchView;
    private DocumentAdapter documentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.see_doc);

        documentAdapter = new DocumentAdapter(this);
        documentAdapter.setOnDocumentClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(documentAdapter);

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_document, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        documentAdapter.query(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onDocumentClick(String path) {
        showDialogMarkdown(path);
    }

    private void showDialogMarkdown(String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        MarkdownView markdownView = new MarkdownView(this);
        markdownView.loadMarkdownFromAssets(path);
        builder.setView(markdownView);
        builder.create().show();
    }
}

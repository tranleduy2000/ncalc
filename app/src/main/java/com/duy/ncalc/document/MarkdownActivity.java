package com.duy.ncalc.document;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseActivity;

public class MarkdownActivity extends BaseActivity {

    /**
     * @param relativePath relative path of doc/functions
     */
    public static void open(@NonNull Context context, @NonNull String relativePath) {
        Intent intent = new Intent(context, MarkdownActivity.class);
        intent.putExtra(DocumentFragment.EXTRA_PATH, relativePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment_content);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = getDocumentPath();
        setTitle(makeTitle(path));
        if (path != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, DocumentFragment.newInstance(path))
                    .commitAllowingStateLoss();
        }
    }

    private String makeTitle(String path) {
        if (path == null) {
            return null;
        }
        if (path.contains("/")) {
            path = path.substring(path.lastIndexOf("/") + 1);
        }
        path = path.replace(".md", "");
        return path;
    }

    @Nullable
    private String getDocumentPath() {
        return getIntent().getStringExtra(DocumentFragment.EXTRA_PATH);
    }
}

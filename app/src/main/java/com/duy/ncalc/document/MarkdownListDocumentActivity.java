package com.duy.ncalc.document;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duy.calculator.R;
import com.duy.calculator.activities.base.BaseActivity;

public class MarkdownListDocumentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment_content);
        setupToolbar();
        setTitle(R.string.documentation);
        MarkdownListDocumentFragment fragment = MarkdownListDocumentFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commitAllowingStateLoss();
    }
}

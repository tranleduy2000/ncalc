package com.example.duy.calculator.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.duy.calculator.R;

import io.github.kexanie.library.MathView;

/**
 * MathView custom as mContext
 * Created by DUy on 04-Nov-16.
 */

public class MathViewActivity extends AppCompatActivity {
    public static final String DATA = "data_math_view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getBundleExtra(DATA);
            if (bundle != null) {
                try {
                    String res = bundle.getString(DATA);
                    MathView mathView = (MathView) findViewById(R.id.math_view);
                    mathView.getSettings().setSupportZoom(true);
                    mathView.getSettings().setJavaScriptEnabled(true);
                    mathView.getSettings().setBuiltInZoomControls(true);
                    mathView.getSettings().setDisplayZoomControls(true);
                    mathView.setText(res);
                    Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    new AlertDialog.Builder(this).setTitle(R.string.error)
                            .setMessage(e.getMessage())
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

package com.example.duy.calculator.hand_write;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.utils.ClipboardManager;
import com.example.duy.calculator.utils.FileUtils;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;
import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.math.widget.MathWidgetApi;

import java.util.Date;


/**
 * Hand calculator
 * <p>
 * Created by DUy on 10/29/2016.
 */

public class HandCalculatorActivity extends AbstractAppCompatActivity {
    public static final String TAG = HandCalculatorActivity.class.getName();
    private MathWidgetApi widget;
    private GestureState mGestureState = GestureState.COLLAPSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.duy.calculator.R.layout.activity_hand_calc);
        Toolbar toolbar = (Toolbar) findViewById(com.example.duy.calculator.R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        if (intent != null)
            if (intent.getStringExtra(BasicCalculatorActivity.DATA) != null)
                toolbar.setTitle(intent.getStringExtra(BasicCalculatorActivity.DATA));

        widget = (MathWidgetApi) findViewById(com.example.duy.calculator.R.id.math_widget);
        boolean success = HandWriteManager.initHandWrite(widget,
                this, new CalcHandWriteCallback(), true);
        if (!success) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Lỗi");
            dlgAlert.setTitle("");
            dlgAlert.setCancelable(true);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dlgAlert.create().show();
            return;
        }

        widget.setOnPenListener(new MathWidgetApi.OnPenListener() {
            @Override
            public void onPenDown(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {

            }

            @Override
            public void onPenUp(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {

            }

            @Override
            public void onPenMove(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {

            }

            @Override
            public void onPenAbort(MathWidgetApi mathWidgetApi) {

            }
        });

        widget.setOnRecognitionListener(new MathWidgetApi.OnRecognitionListener() {
            @Override
            public void onRecognitionBegin(MathWidgetApi mathWidgetApi) {

            }

            @Override
            public void onRecognitionEnd(MathWidgetApi mathWidgetApi) {
                processOutput(mathWidgetApi);
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String s = widget.getResultAsText();
                s = s.replace("[", "(");
                s = s.replace("]", ")");
                intent1.putExtra(BasicCalculatorActivity.DATA, s);
                setResult(RESULT_OK, intent1);
                HandCalculatorActivity.this.finish();
            }
        });

    }

    private void processOutput(MathWidgetApi mathWidgetApi) {
        String s = mathWidgetApi.getResultAsText();
        s = s.replace("[", "(");
        s = s.replace("]", ")");
//        Log.d(TAG + " res : ", s);
        s = mathWidgetApi.getResultAsLaTeX();
//        Log.d(TAG, s);
//        s = mathWidgetApi.getResultAsMathML();
//        Log.d(TAG, s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String out;
        String filename;
        FileUtils fileUtils = new FileUtils(this);
        ClipboardManager myClipboard = new ClipboardManager();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.clear:
                widget.clear(true);
                return true;
            case R.id.undo:
                widget.undo();
                return true;
            case R.id.redo:
                widget.redo();
                return true;
            case R.id.out_latex:
                out = widget.getResultAsLaTeX();
                filename = (new Date().toString()) + " LaTex" + ".txt";
                fileUtils.save(filename, out);
                return true;
            case R.id.out_mathml:
                out = widget.getResultAsMathML();
                filename = (new Date().toString()) + " MathML" + ".txt";
                fileUtils.save(filename, out);
                return true;
            case R.id.out_text:
                out = widget.getResultAsText();
                filename = (new Date().toString()) + " Text" + ".txt";
                fileUtils.save(filename, out);
                return true;
            case R.id.copy_as_text:
                out = widget.getResultAsText();
                ClipboardManager.setClipboard(this, out);
                return true;
            case R.id.copy_as_latex:
                out = widget.getResultAsLaTeX();
                ClipboardManager.setClipboard(this, out);
                return true;
            case R.id.copy_as_mathml:
                out = widget.getResultAsMathML();
                ClipboardManager.setClipboard(this, out);

                return true;
            case R.id.info:
                showDialogHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * show alert dialog "how to use"
     */
    private void showDialogHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.hand_write_help)
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        widget.release();
        super.onDestroy();
    }

    public enum GestureState {COLLAPSE, EXPAND}
}

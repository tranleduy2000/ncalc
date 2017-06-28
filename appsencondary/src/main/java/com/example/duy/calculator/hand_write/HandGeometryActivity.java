package com.example.duy.calculator.hand_write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.geometry.widget.GeometryWidgetApi;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * geometric hand writer
 * <p>
 * triangle
 * rect
 * circle
 * square
 * ....
 */
public class HandGeometryActivity extends AbstractAppCompatActivity implements GeometryWidgetApi.OnEditingListener {

    private static final String TAG = "GeometryActivity";
    private GeometryWidgetApi mWidget;
    private SlidingUpPanelLayout mSilde;
    private EditText mEditText;
    private Button btnSave;
    private TextView txtInfo;
    private NumberFormat numberFormat = new DecimalFormat("#.##");
    private Button btnCancel;
    private StateEdit stateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geometric);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.hand_geometry);
        mWidget = (GeometryWidgetApi) findViewById(R.id.geometry_widget);

        if (!mWidget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }

        setupHandWrite();
        findViewById(R.id.fab_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSilde = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSilde.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mWidget.undo(); //???
            }
        });
        mEditText = (EditText) mSilde.findViewById(R.id.edit_value);
        btnSave = (Button) mSilde.findViewById(R.id.btn_save);
        txtInfo = (TextView) mSilde.findViewById(R.id.txt_info);
        btnCancel = (Button) mSilde.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCancel();
            }
        });
    }

    /**
     * config hand write view
     */
    private void setupHandWrite() {


        mWidget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf/");
        mWidget.configure("shape", "standard");
        mWidget.setOnEditingListener(this);

        //set pen listener
        mWidget.setOnPenListener(new GeometryWidgetApi.OnPenListener() {
            @Override
            public void onPenDown(GeometryWidgetApi geometryWidgetApi, CaptureInfo captureInfo) {
                Log.d(TAG, "onPenDown: ");
            }

            @Override
            public void onPenUp(GeometryWidgetApi geometryWidgetApi, CaptureInfo captureInfo) {
                Log.d(TAG, "onPenUp: ");
            }

            @Override
            public void onPenMove(GeometryWidgetApi geometryWidgetApi, CaptureInfo captureInfo) {
//                Log.d(TAG, "onPenMove: ");
            }

            @Override
            public void onPenAbort(GeometryWidgetApi geometryWidgetApi) {
                Log.d(TAG, "onPenAbort: ");
            }
        });

        mWidget.setOnRecognitionListener(new GeometryWidgetApi.OnRecognitionListener() {
            @Override
            public void onRecognitionBegin(GeometryWidgetApi geometryWidgetApi) {
                Log.d(TAG, "onRecognitionBegin: ");
            }

            @Override
            public void onRecognitionEnd(GeometryWidgetApi geometryWidgetApi) {
                Log.d(TAG, "onRecognitionEnd: ");
            }
        });
    }

    private void doCancel() {
        switch (stateEdit) {
            case ANGEL:
            case LENGTH:
                mWidget.undo();
                mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case LABEL:
                //do some thing
                mSilde.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
        }
    }

    private void doCommitChange() {
    }

    @Override
    protected void onDestroy() {
        if (mWidget != null) {
            mWidget.release();
            mWidget = null;
        }
        super.onDestroy();
    }

    /**
     * init layout menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hand_geo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                mWidget.clear(true);
                return true;
            case R.id.undo:
                mWidget.undo();
                return true;
            case R.id.redo:
                mWidget.redo();
                return true;
            case R.id.action_out_img:
                saveImage();
                return true;
            case R.id.action_share:
                shareImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * get bitmap form widget and share
     */
    private void shareImage() {
        Bitmap bitmap = mWidget.getResultAsImage();

        bitmap = null; //recycle
    }

    /**
     * save bitmap to storage
     */
    private void saveImage() {
        Bitmap bitmap = mWidget.getResultAsImage();
        boolean res = PictUtil.saveToFile("Ncacl_" + SystemClock.currentThreadTimeMillis() + ".png", bitmap);
        Toast.makeText(this, String.valueOf(res), Toast.LENGTH_SHORT).show();
    }

    private void showInputLength(final GeometryWidgetApi widget, final float existingValue, final long idGeom) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(HandGeometryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_length, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HandGeometryActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.lengthEdit);
        int _v = (int) ((existingValue * 100.0f) + 0.5f);
        float v = (float) _v / 100.f;
        final String textValue = Float.toString(v);
        editText.setText(textValue, TextView.BufferType.EDITABLE);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = String.valueOf(editText.getText());
                        if (textValue.equals(text)) {
                            // set existing value without loss of precision
                            widget.setValue(idGeom, existingValue);
                        } else {
                            float value = Float.parseFloat(text);
                            widget.setValue(idGeom, value);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                widget.undo();
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showInputAngle(final GeometryWidgetApi widget, final float existingValue, final long idGeom) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(HandGeometryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_angle, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HandGeometryActivity.this);
        alertDialogBuilder.setView(promptView);


        final EditText editText = (EditText) promptView.findViewById(R.id.angleEdit);

        float existingDegrees = (float) (180.0f * existingValue / Math.PI);
        int _v = (int) ((existingDegrees * 100.0f) + 0.5f);
        float v = (float) _v / 100.f;
        final String textValue = Float.toString(v);
        editText.setText(textValue, TextView.BufferType.EDITABLE);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = String.valueOf(editText.getText());
                        if (textValue.equals(text)) {
                            // set existing value without loss of precision
                            widget.setValue(idGeom, existingValue);
                        } else {
                            float valueDegrees = Float.parseFloat(text);
                            widget.setValue(idGeom, (float) (Math.PI * valueDegrees / 180.0f));
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                widget.undo();
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showInputLabel(final GeometryWidgetApi widget, final String label, final long idGeom) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(HandGeometryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_label, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HandGeometryActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.labelEdit);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = String.valueOf(editText.getText());
                        widget.setLabel(idGeom, text);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                widget.undo();
                                dialog.cancel();
                            }
                        });

        editText.setText(label, TextView.BufferType.EDITABLE);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onEditingLengthValue(final GeometryWidgetApi widget, final float existingValue, final PointF position, final long id) {
        this.stateEdit = StateEdit.LENGTH;
        mSilde.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        txtInfo.setText(R.string.edit_length);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEditText.setText(numberFormat.format(existingValue).replace(",", "."));
        mEditText.selectAll();
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_NUMPAD_ENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                            if (mEditText.getText().toString().isEmpty()) {
                                mEditText.setError(getString(R.string.enter_value));
                            } else {
                                Float f = Float.valueOf(mEditText.getText().toString());
                                widget.setValue(id, f);
                                mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        }
                        hideKeyboard(mEditText);

                        return true;
                }
                return false;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText.getText().toString().isEmpty()) {
                    mEditText.setError(getString(R.string.enter_value));
                } else {
                    Float f = Float.valueOf(mEditText.getText().toString());
                    widget.setValue(id, f);
                    mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    hideKeyboard(mEditText);

                }
            }
        });

    }

    @Override
    public void onEditingAngleValue(final GeometryWidgetApi widget, final float existingValue, final PointF position, final long id) {
        this.stateEdit = StateEdit.ANGEL;

        mSilde.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        txtInfo.setText(R.string.edit_angle);

        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEditText.setText(numberFormat.format(Math.toDegrees(existingValue)).replace(",", "."));
        mEditText.selectAll();
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_NUMPAD_ENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                            saveAngleValue(widget, existingValue, id);
                        }
                        hideKeyboard(mEditText);
                        return true;
                }
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText.getText().toString().isEmpty()) {
                    mEditText.setError(getString(R.string.enter_value));
                } else {
                    saveAngleValue(widget, existingValue, id);
                    hideKeyboard(mEditText);

                }
            }
        });
    }

    private void saveAngleValue(GeometryWidgetApi widget, float existingValue, long id) {
        String text = mEditText.getText().toString();
        float valueDegrees = Float.parseFloat(text);
        widget.setValue(id, (float) (Math.PI * valueDegrees / 180.0f));
        mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onEditingLabel(final GeometryWidgetApi widget, final String existingLabel, final PointF position, final long id) {
        this.stateEdit = StateEdit.LABEL;

        mSilde.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        txtInfo.setText(R.string.edit_label);

        mEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        mEditText.setText(existingLabel);
        mEditText.selectAll();
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_NUMPAD_ENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                            String label = mEditText.getText().toString();
                            widget.setLabel(id, label);
                            mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                        hideKeyboard(mEditText);
                        return true;
                }
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String label = mEditText.getText().toString();
                widget.setLabel(id, label);
                mSilde.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                hideKeyboard(mEditText);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSilde.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            doCancel();
        } else super.onBackPressed();

    }

    private enum StateEdit {ANGEL, LENGTH, LABEL}
}

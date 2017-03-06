/*
 * Copyright (C) 2010 Andrew P McSherry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.duy.calculator.version_old.graph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.utils.ConfigApp;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class GraphActivity extends AbstractAppCompatActivity {
    public static final String DATA = GraphActivity.class.getName();
    public static final String FUNC = GraphActivity.class.getName();
    public static final String TAG = "GraphActivity";

    private static final int REQUEST_CODE = 1212;
    private static final String GRAPH_STATED = "GRAPH_STATED";
    Graph2DView mGraph2D;
    Graph3DView mGraph3D;
    SwitchCompat imgTrace;
    SwitchCompat imgDervi;
    SwitchCompat mModeGraph;
    private boolean isTrace = false, isDerivative = false;
    private int mode;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        mGraph2D = (Graph2DView) findViewById(R.id.graph_2d);
        imgTrace = (SwitchCompat) findViewById(R.id.img_trace);
        imgDervi = (SwitchCompat) findViewById(R.id.btn_der);
        mModeGraph = (SwitchCompat) findViewById(R.id.sw_mode);
        mGraph3D = (Graph3DView) findViewById(R.id.graph_3d);


        imgTrace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != isTrace) changeModeTrace(b);
            }
        });

        imgDervi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != isDerivative) changeModeDerivative(b);
            }
        });

        boolean is2d = mPreferences.getBoolean("is2d", false);
        mModeGraph.setChecked(is2d);
        mModeGraph.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPreferences.edit().putBoolean("is2d", b).apply();
                invalidate(b);
            }
        });

        findViewById(R.id.img_add_fun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFunction();
            }
        });

        findViewById(R.id.img_zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomIn();
            }
        });

        findViewById(R.id.img_zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomOut();
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        //show graph view
        invalidate(mModeGraph.isChecked());

        findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelp();
            }
        });

        if (!(mPreferences.getBoolean(GRAPH_STATED, false))
                || ConfigApp.DEBUG)
            showHelp();

        if (!mPreferences.getBoolean(GRAPH_STATED, false)) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("f1", "x^2");
            editor.apply();
            editor.putBoolean(GRAPH_STATED, true);
        }

        receiveData(); //intent data

    }

    private void addFunction() {
        Intent intent = new Intent(GraphActivity.this, GraphAddFunction.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void zoomIn() {
        Log.d(TAG, "onClick: " + mode);
        if (mode != GraphMode.THREE_D) {
            mGraph2D.zoom((float) -.5);
        } else {
            mGraph3D.zoom((float) -.5);
        }
    }

    private void zoomOut() {
        Log.d(TAG, "onClick: " + mode);
        if (mode != GraphMode.THREE_D) {
            mGraph2D.zoom((float) .5);
        } else {
            mGraph3D.zoom((float) .5);
        }
    }

    private void saveImage() {
        try {
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                Date d = new Date();
                File imageLoc = new File(root, "com/duy/example/com.duy.calculator/graph/" + d.getMonth() + d.getDay() + d.getHours() + d.getMinutes() + d.getSeconds() + ".png");
                FileOutputStream out = new FileOutputStream(imageLoc);
            } else {
                Toast.makeText(GraphActivity.this, getString(R.string.cannotwrite), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(GraphActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void changeModeDerivative(boolean isDerivative) {
        this.isTrace = false;
        this.isDerivative = isDerivative;

        //diable trace mode
        mGraph2D.setTrace(false);

        //mode derivative is always change when call this method
        boolean result = mGraph2D.setDeriv(this.isDerivative);

        if (!result) {
            this.isDerivative = false;
            Toast.makeText(GraphActivity.this, getString(R.string.noFunDisp),
                    Toast.LENGTH_LONG).show();
        } else if (this.isDerivative) {
            Toast.makeText(GraphActivity.this, getString(R.string.tapFun),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * change graph to mode isTrace, user cannot move the graph view
     *
     * @param isTrace - isTrace enable
     */
    private void changeModeTrace(boolean isTrace) {
        //disable mode derivative if is enable
        this.isDerivative = false;
        imgDervi.setChecked(false);
        mGraph2D.setDeriv(false);

        //set mode trace
        this.isTrace = isTrace;
        boolean result = mGraph2D.setTrace(this.isTrace);

        if (!result) {
            this.isTrace = false;
            Toast.makeText(GraphActivity.this,
                    getString(R.string.noFunDisp), Toast.LENGTH_LONG).show();
        } else if (this.isTrace) {
            Toast.makeText(GraphActivity.this,
                    getString(R.string.tapFun), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * help for first start graph
     * <p>
     * user press help button
     */
    private void showHelp() {
        TapTarget target1 = TapTarget.forView(this.findViewById(R.id.img_add_fun),
                getString(R.string.add_function),
                getString(R.string.input_graph_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(60);
        TapTarget target2 = TapTarget.forView(this.findViewById(R.id.sw_mode),
                "2D/3D",
                getString(R.string.choose_mode))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(60);

        TapTargetSequence sequence = new TapTargetSequence(GraphActivity.this);
        sequence.targets(target1, target2);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                mPreferences.edit().putBoolean(GRAPH_STATED, true).apply();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                mPreferences.edit().putBoolean(GRAPH_STATED, true).apply();
            }
        });
        sequence.start();
    }

    /**
     * add graph view
     *
     * @param is2d
     */
    private void invalidate(boolean is2d) {
        if (!is2d) {
            mGraph2D.setVisibility(GONE);

            mGraph3D.setVisibility(VISIBLE);
            mGraph3D.drawGraph();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgTrace.setVisibility(GONE);
                    imgDervi.setVisibility(GONE);
                }
            }, 100);
            mode = GraphMode.THREE_D;
        } else {
            mGraph3D.setVisibility(GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgTrace.setVisibility(VISIBLE);
                    imgDervi.setVisibility(VISIBLE);
                }
            }, 100);

            mGraph2D.setVisibility(View.VISIBLE);
            mGraph2D.drawGraph();

            mode = GraphMode.TWO_D;
        }
    }

    private void receiveData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(DATA);
        if (bundle != null) {
            String fx = bundle.getString(FUNC);
            if (!fx.isEmpty()) mPreferences.edit().putString("f1", fx).apply();
            Log.d(TAG, "receiveData: " + fx);
            Log.d(TAG, "onResume: ok");
        } else {
            //do some thing
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mode = mPreferences.getInt("GraphMode", GraphMode.TWO_D);
        invalidate(mode == GraphMode.TWO_D);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences.edit().putInt("GraphMode", mode).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        if (requestCode == REQUEST_CODE) {
            //invalidate((mode == Graph2DView.RECT));
        }
        invalidate(mode == GraphMode.TWO_D);
    }
}

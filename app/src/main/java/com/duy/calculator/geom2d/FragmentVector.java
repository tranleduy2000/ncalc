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

package com.duy.calculator.geom2d;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.calculator.geom2d.util.Angle2D;

/**
 * Created by Duy on 3/7/2016
 */
public class FragmentVector extends Fragment implements Geometric, TextWatcher {
    private double xa, xb, ya, yb;
    private EditText mXa, mXb, mYa, mYb;
    private CheckBox ckbCollinear, ckbOrthogonal;
    private TextView txtAngle, txtMinus, txtPlus, txtAngleA, txtAngleB;
    private boolean isVectorA = false, isVectorB = false;
    private Vector2D mVectorA, mVectorB;
    private TextView txtTimeA, txtTimeB, txtScalar;
    private String TAG = FragmentVector.class.getName();
    private EditText editK;
    private boolean isNumK = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_geom_vecter, container, false);
        mXa = (EditText) view.findViewById(R.id.editVectoXa);
        mYa = (EditText) view.findViewById(R.id.editYa);
        mXb = (EditText) view.findViewById(R.id.editXb);
        mYb = (EditText) view.findViewById(R.id.editYb);

        mXa.addTextChangedListener(this);
        mXb.addTextChangedListener(this);
        mYa.addTextChangedListener(this);
        mYb.addTextChangedListener(this);

        ckbCollinear = (CheckBox) view.findViewById(R.id.ckbColinear);
        ckbOrthogonal = (CheckBox) view.findViewById(R.id.ckbOrthoganal);
        txtAngle = (TextView) view.findViewById(R.id.txtAngle);
        txtPlus = (TextView) view.findViewById(R.id.txtPlus);
        txtMinus = (TextView) view.findViewById(R.id.txtMinus);

        txtAngleA = (TextView) view.findViewById(R.id.txtAngleA);
        txtAngleB = (TextView) view.findViewById(R.id.txtAngelB);

        txtTimeA = (TextView) view.findViewById(R.id.txtMulA);
        txtTimeB = (TextView) view.findViewById(R.id.txtMulB);
        txtScalar = (TextView) view.findViewById(R.id.txtScalar);

        editK = (EditText) view.findViewById(R.id.editK);
        editK.addTextChangedListener(this);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onPrepare();
        onResult();
        // Log.d(TAG, "onTextChange");
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * get data and parse input
     */
    public void onPrepare() {
        try {
            if (mXa.getText().toString().isEmpty()
                    || mYa.getText().toString().isEmpty()) {
                isVectorA = false;
            } else {
                xa = Double.parseDouble(mXa.getText().toString());
                ya = Double.parseDouble(mYa.getText().toString());
                Log.d(TAG, String.valueOf(xa));
                Log.d(TAG, String.valueOf(ya));
                mVectorA = new Vector2D(xa, ya);
                isVectorA = true;
            }

            if (mXb.getText().toString().isEmpty()
                    || mYb.getText().toString().isEmpty()) {
                isVectorB = false;
            } else {
                xb = Double.parseDouble(mXb.getText().toString());
                yb = Double.parseDouble(mYb.getText().toString());
                Log.d(TAG, String.valueOf(xb));
                Log.d(TAG, String.valueOf(yb));
                mVectorB = new Vector2D(xb, yb);
                isVectorB = true;
            }

            if (!editK.getText().toString().isEmpty()) {
                isNumK = true;
            } else {
                isNumK = false;
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    /**
     * computes and show
     */
    public void onResult() {
//        Log.d(TAG, String.valueOf(isVectorA) + String.valueOf(isVectorB));
        java.text.DecimalFormat format = new java.text.DecimalFormat("#.###");
        if (isVectorA && isVectorB) {

            boolean isCollinear = mVectorA.isColinear(mVectorB);
            boolean isOrthoganol = mVectorA.isOrthogonal(mVectorB);

            ckbCollinear.setChecked(isCollinear);
            ckbOrthogonal.setChecked(isOrthoganol);

            //angle vector a vs vector b
            double angle = Angle2D.angle(mVectorA, mVectorB);
            txtAngle.setText(format.format(angle) + " (RAD)\n " +
                    format.format(Math.toDegrees(angle)) + " (DEG)");

            Vector2D plus = mVectorA.plus(mVectorB);
            txtPlus.setText(plus.toString());

            Vector2D minus = mVectorA.minus(mVectorB);
            txtMinus.setText(minus.toString());

            //angle
            double angleA = Angle2D.horizontalAngle(mVectorA);
            double angleB = Angle2D.horizontalAngle(mVectorB);

            txtAngleA.setText(format.format(angleA) + " (RAD)\n" +
                    format.format(Math.toDegrees(angleA)) + " (DEG)");

            txtAngleB.setText(format.format(angleB) + " (RAD)\n" +
                    format.format(Math.toDegrees(angleB)) + " (DEG)");
            //times
            if (isNumK) {
                txtTimeA.setText(
                        mVectorA.times(Double.parseDouble(editK.getText().toString()))
                                .toString());

                txtTimeB.setText(
                        mVectorB.times(Double.parseDouble(editK.getText().toString()))
                                .toString());
            } else {
                txtTimeB.setText(null);
                txtTimeA.setText(null);
            }

            double scalar = mVectorA.getScalar(mVectorB);
            txtScalar.setText(String.valueOf(scalar));

        } else if (isVectorA || isVectorB) {
            txtAngleA.setText(null);
            txtAngleB.setText(null);
            txtAngle.setText(null);
            txtPlus.setText(null);
            txtMinus.setText(null);

            Log.d(TAG, String.valueOf(isVectorA));
            if (isVectorA) {
                double angleA = Angle2D.horizontalAngle(mVectorA);
                txtAngleA.setText(String.valueOf(angleA));
            } else if (isVectorB) {
                double angleB = Angle2D.horizontalAngle(mVectorB);
                txtAngleB.setText(String.valueOf(angleB));
            }
        }
    }

    @Override
    public void onError() {

    }
}

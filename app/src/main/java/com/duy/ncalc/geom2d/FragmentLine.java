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

package com.duy.ncalc.geom2d;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.ncalc.geom2d.line.Line2D;
import com.duy.ncalc.geom2d.line.StraightLine2D;
import com.duy.ncalc.geom2d.util.Angle2D;

/**
 * Created by Duy on 3/7/2016
 */
public class FragmentLine extends Fragment implements Geometric, TextWatcher {
    private EditText editXa, editXb, editYa, editYb, editXc, editYc, editXm, editYm;
    private TextView txtLenght, txtInfo, txtMidPoint, txtVectorPhoenix,
            txtVectorTangent, tatGeneralEquation, txtAngleOx, txtDistancePoint, txtEquationParam;
    private TextView txtInfo2, txtAngle;
    private StraightLine2D mLine;
    private boolean isLine = false, isLine2, isPoint;
    private EditText editXd;
    private EditText editYd;
    private StraightLine2D mLine2;
    private Point2D mPoint;
    private TextView txtPositon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_geom_line, container, false);

        editXa = (EditText) view.findViewById(R.id.editXa);
        editXa.addTextChangedListener(this);
        editYa = (EditText) view.findViewById(R.id.editYa);
        editYa.addTextChangedListener(this);

        editXb = (EditText) view.findViewById(R.id.editXb);
        editXb.addTextChangedListener(this);
        editYb = (EditText) view.findViewById(R.id.editYb);
        editYb.addTextChangedListener(this);

        editXc = (EditText) view.findViewById(R.id.editXc);
        editXc.addTextChangedListener(this);
        editYc = (EditText) view.findViewById(R.id.editYc);
        editYc.addTextChangedListener(this);

        editXd = (EditText) view.findViewById(R.id.editXd);
        editXd.addTextChangedListener(this);
        editYd = (EditText) view.findViewById(R.id.editYd);
        editYd.addTextChangedListener(this);

        editXm = (EditText) view.findViewById(R.id.editXm);
        editXm.addTextChangedListener(this);
        editYm = (EditText) view.findViewById(R.id.editYm);
        editYm.addTextChangedListener(this);

        txtLenght = (TextView) view.findViewById(R.id.txtLength);
        txtMidPoint = (TextView) view.findViewById(R.id.txtMid);
        txtVectorPhoenix = (TextView) view.findViewById(R.id.txtVectorLine);
        txtVectorTangent = (TextView) view.findViewById(R.id.txtVectorTangent);
        tatGeneralEquation = (TextView) view.findViewById(R.id.txtGenaralEquation);
        txtAngleOx = (TextView) view.findViewById(R.id.txtAngleOx);
        txtDistancePoint = (TextView) view.findViewById(R.id.txtDistancePoint);
        txtEquationParam = (TextView) view.findViewById(R.id.txtParameter);
        txtInfo2 = (TextView) view.findViewById(R.id.txtInfor2);
        txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        txtAngle = (TextView) view.findViewById(R.id.txtAngle);
        txtPositon = (TextView) view.findViewById(R.id.txtPositon);
        return view;
    }

    @Override
    public void onPrepare() {
        try {
            //line 1
            if (!editXa.getText().toString().isEmpty()
                    && !editYa.getText().toString().isEmpty()
                    && !editXb.getText().toString().isEmpty()
                    && !editYb.getText().toString().isEmpty()) {
                try {
                    mLine = new StraightLine2D(
                            new Point2D(Double.parseDouble(editXa.getText().toString()),
                                    Double.parseDouble(editYa.getText().toString()))
                            ,
                            new Point2D(Double.parseDouble(editXb.getText().toString()),
                                    Double.parseDouble(editYb.getText().toString()))
                    );

                    isLine = true;
                    txtInfo.setText(null);
                } catch (RuntimeException e) {
                    txtInfo.setText(getResources().getString(R.string.not_line));
                }


            } else {
                isLine = false;
            }
            //line2
            if (!editXc.getText().toString().isEmpty()
                    && !editYc.getText().toString().isEmpty()
                    && !editXd.getText().toString().isEmpty()
                    && !editYd.getText().toString().isEmpty()) {

                try {
                    mLine2 = new StraightLine2D(
                            Double.parseDouble(editXc.getText().toString()),
                            Double.parseDouble(editYc.getText().toString()),
                            Double.parseDouble(editXd.getText().toString()),
                            Double.parseDouble(editYd.getText().toString())
                    );
                    isLine2 = true;
                    txtInfo2.setText(null);
                } catch (RuntimeException e) {
                    txtInfo2.setText(getResources().getString(R.string.not_line));
                }
            } else {
                isLine2 = false;
            }
            //point
            if (!editXm.getText().toString().isEmpty()
                    && !editYm.getText().toString().isEmpty()) {
                mPoint = new Point2D(
                        Double.parseDouble(editXm.getText().toString()),
                        Double.parseDouble(editYm.getText().toString())
                );
                isPoint = true;
            } else {
                isPoint = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * In geom2d mode, when user input the two points' coordinates, this method will be called.
     */
    @Override
    public void onResult() {
        if (isLine) {
            Line2D line2D = new Line2D(mLine);
            //chiều dài
            double length = new Line2D(mLine).length();
            txtLenght.setText(String.valueOf(length));
            //góc
            double tmp = Angle2D.horizontalAngle(mLine);
            tmp = (tmp + Math.PI) %Math.PI;
            txtAngleOx.setText(String.valueOf(tmp));
            //trung điểm
            Point2D midP = line2D.getMidPoint();
            txtMidPoint.setText(midP.toString());

            //vector
            Vector2D vectorPhoenix = new Vector2D(line2D.getPoint1(), line2D.getPoint2());
            //vector chỉ phương
            txtVectorPhoenix.setText(vectorPhoenix.toString());
            //phương trình tham số
            String equationParam = line2D.getEquationParameter();
            txtEquationParam.setText(equationParam);
            //phương trình tổng quát
            String generalEqual;
            try {
                generalEqual = line2D.getGeneralEquation();
                tatGeneralEquation.setText(generalEqual);
            } catch (Exception e) {
                tatGeneralEquation.setText("null");
                e.printStackTrace();
            }
            //vector pháp tuyến
            Vector2D vecTangent = vectorPhoenix.getOrthogonal();
            txtVectorTangent.setText(vecTangent.toString());
            if (isPoint) {
                double distance = mLine.distance(mPoint);
                txtDistancePoint.setText(String.valueOf(distance));
            } else {
                txtDistancePoint.setText(null);
            }

            if (isLine2) {
                Line2D line2D_2 = new Line2D(mLine2);
                 boolean isCollinear = line2D.isColinear(line2D_2);
                 if (isCollinear) {
                     txtPositon.setText(getResources().getString(R.string.collinear));
                 } else {
                     boolean isParallel = line2D.isParallel(line2D_2);
                     if (isParallel) {
                         txtPositon.setText(getResources().getString(R.string.parallel));
                     } else {
                        Point2D point2Ds = line2D.intersection(line2D_2);
                        if (point2Ds!=null) {
                            txtPositon.setText(getResources().getString(R.string.interaction)
                                    + "\n"
                                    + point2Ds.toString());
                        }
                     }
                 }
                //angle
                txtAngle.setText(String.valueOf(Angle2D.angle(line2D, line2D_2)));
            } else {
                txtAngle.setText(null);
                txtPositon.setText(null);
            }

        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onPrepare();
        onResult();

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

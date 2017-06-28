package com.example.duy.calculator.geom2d;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.example.duy.calculator.geom2d.conic.Circle2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Duy on 3/7/2016
 */
public class FragmentCircle extends Fragment implements Geometric, TextWatcher {
    private EditText editX1, editY1, editX2, editY2, editXa, editXb, editYa, editYb, editXc, editYc, editR1, editR2;
    private TextView txtInteractionCircle, txtInteractionPoint, txtInteractionLine, txtLength, txtArea;
    private TextView txtEquationTangent, txtEquationCircle;
    private boolean isCircle = false, isLine = false, isPoint = false, isCircle2 = false;
    private boolean isPointTangent;
    private Circle2D mCircle, mCircle2;
    private LineSegment2D mLine;
    private Point2D mPointC;
    private String TAG = FragmentCircle.class.getName();
    private EditText editXm, editYm;
    private Point2D mPointM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_geom_circle, container, false);
        editX1 = (EditText) view.findViewById(R.id.editX1);
        editX1.addTextChangedListener(this);
        editX2 = (EditText) view.findViewById(R.id.editX2);
        editX2.addTextChangedListener(this);
        editY1 = (EditText) view.findViewById(R.id.editY1);
        editY1.addTextChangedListener(this);
        editY2 = (EditText) view.findViewById(R.id.editY2);
        editY2.addTextChangedListener(this);

        editYa = (EditText) view.findViewById(R.id.editYa);
        editYa.addTextChangedListener(this);
        editYb = (EditText) view.findViewById(R.id.editYb);
        editYb.addTextChangedListener(this);

        editXa = (EditText) view.findViewById(R.id.editXa);
        editXa.addTextChangedListener(this);

        editXb = (EditText) view.findViewById(R.id.editXb);
        editXb.addTextChangedListener(this);

        editXc = (EditText) view.findViewById(R.id.editXc);
        editXc.addTextChangedListener(this);

        editYc = (EditText) view.findViewById(R.id.editYc);
        editYc.addTextChangedListener(this);

        editR1 = (EditText) view.findViewById(R.id.editR1);
        editR1.addTextChangedListener(this);

        editR2 = (EditText) view.findViewById(R.id.editR2);
        editR2.addTextChangedListener(this);

        txtInteractionCircle = (TextView) view.findViewById(R.id.txtInteractionCircle);
        txtInteractionLine = (TextView) view.findViewById(R.id.txtInteractionLine);
        txtInteractionPoint = (TextView) view.findViewById(R.id.txtInteractionPoint);
        txtArea = (TextView) view.findViewById(R.id.txtArea);
        txtLength = (TextView) view.findViewById(R.id.txtLength);

        txtEquationTangent = (TextView) view.findViewById(R.id.txtEquationTangent);
        txtEquationCircle = (TextView) view.findViewById(R.id.txtEquaCircle);

        editXm = (EditText) view.findViewById(R.id.editXm);
        editXm.addTextChangedListener(this);
        editYm = (EditText) view.findViewById(R.id.editYm);
        editYm.addTextChangedListener(this);

        return view;
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

    @Override
    public void onPrepare() {
        try {
            if (!editX1.getText().toString().isEmpty()
                    && !editY1.getText().toString().isEmpty()
                    && !editR1.getText().toString().isEmpty()) {
                mCircle = new Circle2D(
                        Double.parseDouble(editX1.getText().toString()),
                        Double.parseDouble(editY1.getText().toString()),
                        Double.parseDouble(editR1.getText().toString())
                );
                isCircle = true;
            } else {
                isCircle = false;
            }

            if (!editX2.getText().toString().isEmpty()
                    && !editY2.getText().toString().isEmpty()
                    && !editR2.getText().toString().isEmpty()) {
                mCircle2 = new Circle2D(
                        Double.parseDouble(editX2.getText().toString()),
                        Double.parseDouble(editY2.getText().toString()),
                        Double.parseDouble(editR2.getText().toString())
                );
                isCircle2 = true;
            } else {
                isCircle2 = false;
            }

            //line
            if (!editXa.getText().toString().isEmpty()
                    && !editYa.getText().toString().isEmpty()
                    && !editXb.getText().toString().isEmpty()
                    && !editYb.getText().toString().isEmpty()) {
                mLine = new LineSegment2D(
                        Double.parseDouble(editXa.getText().toString()),
                        Double.parseDouble(editYa.getText().toString()),
                        Double.parseDouble(editXb.getText().toString()),
                        Double.parseDouble(editYb.getText().toString()));
                isLine = true;
            } else {
                isLine = false;
            }

            //point
            if (!editXc.getText().toString().isEmpty()
                    && !editYc.getText().toString().isEmpty()) {
                mPointC = new Point2D(
                        Double.parseDouble(editXc.getText().toString()),
                        Double.parseDouble(editYc.getText().toString())
                );
                isPoint = true;
            } else {
                isPoint = false;
            }

            if (!editXm.getText().toString().isEmpty()
                    || !editYm.getText().toString().isEmpty()) {
                mPointM = new Point2D(
                        Double.parseDouble(editXm.getText().toString()),
                        Double.parseDouble(editYm.getText().toString()));
                isPointTangent = true;
            } else {
                isPointTangent = false;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onResult() {
        Log.d(TAG, String.valueOf(isCircle) + String.valueOf(isLine) + String.valueOf(isPoint));
        if (isCircle) {
            try {

                if (isCircle2) {
                    Collection<Point2D> point2Ds = mCircle.intersections(mCircle2);
                    List<Point2D> list = new ArrayList<>(point2Ds);

                    //ko co giao diem
                    if (list.size() == 0) {
                        txtInteractionCircle.setText(getResources().getString(R.string.not_interaction));
                    } else if (list.size() == 1) {
                        txtInteractionCircle.setText(getResources().getString(R.string.circle_tangent)
                                + "\n"
                                + list.get(0).toString());
                    } else if (point2Ds.size() == 2) {
                        txtInteractionCircle.setText(getResources().getString(R.string.circle_interaction)
                                + "\n"
                                + list.get(0).toString()
                                + "\n"
                                + list.get(1).toString());
                    }
                } else {
                    txtInteractionCircle.setText(null);
                }
            } catch (RuntimeException e) {
                e.getStackTrace();
            }

            try {

                if (isLine) {
                    Collection<Point2D> point2Ds = mCircle.intersections(mLine);
                    List<Point2D> list = new ArrayList<>(point2Ds);
                    switch (list.size()) {
                        case 0:
                            txtInteractionLine.setText(getResources().getString(R.string.not_interaction));
                            break;
                        case 1:
                            txtInteractionLine.setText(getResources().getString(R.string.tangent)
                                    + "\n"
                                    + list.get(0).toString());
                            break;
                        case 2:
                            txtInteractionLine.setText(getResources().getString(R.string.interaction)
                                    + "\n"
                                    + list.get(0).toString()
                                    + "\n"
                                    + list.get(1).toString());
                            break;
                    }
                } else {
                    txtInteractionLine.setText(null);
                }
            } catch (RuntimeException e) {
                txtInteractionLine.setText(getResources().getString(R.string.not_line));
            }
            if (isPoint) {
                boolean isContains = mCircle.contains(mPointC);
                boolean isInside = mCircle.isInside(mPointC);
                if (isContains) {
                    txtInteractionPoint.setText(getResources().getString(R.string.above_circle));
                } else if (isInside) {
                    txtInteractionPoint.setText(getResources().getString(R.string.inside));
                } else {
                    txtInteractionPoint.setText(getResources().getString(R.string.outside));
                }
            } else {
                txtInteractionPoint.setText(null);
            }

            if (isPointTangent) {
                try {
                    String s = mCircle.getEquationTangent(mPointM);
                    txtEquationTangent.setText(s);
                } catch (RuntimeException e) {
                    txtEquationTangent.setText(getResources().getString(R.string.not_in_circle));
                }
            }

            double length = mCircle.length();
            txtLength.setText(String.valueOf(length));

            double area = mCircle.getArea();
            txtArea.setText(String.valueOf(area));

            String equaCircle = mCircle.getEquationCircle();
            txtEquationCircle.setText(equaCircle);

        } else {
            txtArea.setText(null);
            txtLength.setText(null);
            txtEquationCircle.setText(null);
        }
    }

    @Override
    public void onError() {

    }
}

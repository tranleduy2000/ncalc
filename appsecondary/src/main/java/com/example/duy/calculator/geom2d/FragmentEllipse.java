package com.example.duy.calculator.geom2d;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Duy on 3/7/2016
 */
public class FragmentEllipse extends Fragment implements Geometric {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.la);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onResult() {

    }

    @Override
    public void onError() {

    }
}

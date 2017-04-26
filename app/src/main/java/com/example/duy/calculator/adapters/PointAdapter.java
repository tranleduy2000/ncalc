/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duy.calculator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item.Point;

import java.util.ArrayList;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Point> mPoints = new ArrayList<>();

    public PointAdapter(Context context, ArrayList<Point> points) {
        this.mPoints = points;
        this.context = context;
    }

    public PointAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_point, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mPoints.remove(position);
//                notifyItemRemoved(position);
//            }
//        });

        holder.editTextX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (holder.editTextX.getText().toString().isEmpty()) {
                        mPoints.get(position).setX(0d);
                    } else {
                        mPoints.get(position).setX(Double.parseDouble(holder.editTextX.getText().toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.editTextY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (holder.editTextY.getText().toString().isEmpty()) {
                        mPoints.get(position).setY(0d);
                    } else {
                        mPoints.get(position).setY(Double.parseDouble(holder.editTextY.getText().toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void addPoint() {
        mPoints.add(new Point(0, 0));
        notifyItemInserted(mPoints.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mPoints.size();
    }

    public void remove(int position) {
        mPoints.remove(position);
        notifyItemRemoved(position);
    }

    public double[] getListPointX() {
        double[] x = new double[mPoints.size()];
        for (int i = 0; i < mPoints.size(); i++) {
            x[i] = mPoints.get(i).getX();
        }
        return x;
    }

    public double[] getListPointY() {
        double[] x = new double[mPoints.size()];
        for (int i = 0; i < mPoints.size(); i++) {
            x[i] = mPoints.get(i).getY();
        }
        return x;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextX;
        private EditText editTextY;
        private Button btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            editTextX = (EditText) itemView.findViewById(R.id.edit_x);
            editTextY = (EditText) itemView.findViewById(R.id.edit_y);
//            btnRemove = (Button) itemView.findViewById(R.id.btn_remove);

        }
    }
}

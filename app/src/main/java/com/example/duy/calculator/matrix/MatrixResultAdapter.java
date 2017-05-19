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

package com.example.duy.calculator.matrix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.StepItem;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * adapter for recycle view mResult
 */
public class MatrixResultAdapter extends RecyclerView.Adapter<MatrixResultAdapter.ResultViewHolder> {
    final static String TAG = "ResultAdapter";
    private Context mContext;
    private ArrayList<StepItem> stepItems = new ArrayList<>();

    public MatrixResultAdapter(Context activity) {
        mContext = activity;
    }

    public ArrayList<StepItem> getResults() {
        return stepItems;
    }

    public void setResults(ArrayList<StepItem> mResults) {
        this.stepItems = mResults;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_matrix_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, final int position) {
        StepItem stepItem = stepItems.get(position);
        ViewGroup.LayoutParams layoutParams = holder.padding.getLayoutParams();
        layoutParams.width = stepItem.getDepth() * 15;
        holder.padding.setLayoutParams(layoutParams);
        holder.txtResult.setText(stepItem.toTex());
    }

    @Override
    public int getItemCount() {
        return stepItems.size();
    }

    private void updateEmptyView() {

    }

    public void setResult(StepItem result) {
        stepItems.clear();
        stepItems.add(result);
        notifyDataSetChanged();
    }

    public void clear() {
        stepItems.clear();
        notifyDataSetChanged();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        MathView txtResult;
        View padding;

        public ResultViewHolder(View itemView) {
            super(itemView);
            txtResult = (MathView) itemView.findViewById(R.id.txt_result);
            padding = itemView.findViewById(R.id.padding);
        }
    }
}
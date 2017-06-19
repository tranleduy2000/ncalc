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

package com.duy.calculator.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.calculator.utils.VariableUtil;

import java.util.ArrayList;

/**
 * Created by Duy on 09-Nov-16 .
 * Tool equation
 */

public class FunctionRecyclerView extends RecyclerView {

    public FunctionRecyclerView(Context context) {
        super(context);
        if (!isInEditMode())
            init((Context) context);

    }

    public FunctionRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init((Context) context);

    }

    public FunctionRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init((Context) context);
    }

    private void init(Context context) {
        FunctionAdapter mAdapter = new FunctionAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(linearLayoutManager);
        setAdapter(mAdapter);
    }


    static class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
        private ArrayList mList = new ArrayList();
        private Context context;

        private FunctionAdapter(Context context) {
            mList = VariableUtil.getListFunction(context);
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.function_text, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtContent.setText(mList.get(position).toString());
            holder.txtContent.setOnClickListener(null);
            holder.txtContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mContext.insertTextDisplay(((TextView) view).getText().toString().trim());
                }
            });
            holder.txtContent.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData.Item item = new ClipData.Item(((TextView) view).getText().toString());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData dragData = new ClipData(((TextView) view).getText().toString(), mimeTypes, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(dragData, shadowBuilder, view, 0);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtContent;

            ViewHolder(View itemView) {
                super(itemView);
                txtContent = (TextView) itemView.findViewById(R.id.text_view);
            }
        }
    }
}

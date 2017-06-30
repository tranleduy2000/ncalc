/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Duy on 26-Jun-17.
 */

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.VIewHolder> {

    @Override
    public VIewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VIewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class VIewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;

        public VIewHolder(View itemView) {
            super(itemView);
        }
    }

}

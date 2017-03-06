package com.example.duy.calculator.version_old.converter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duy.calculator.R;

import java.util.ArrayList;

/**
 * custom adapter for rc view
 */
public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.Holder> {
    final Context context;
    private ArrayList<ItemUnitConverter> mList = new ArrayList<>();

    public UnitAdapter(Context context, ArrayList<ItemUnitConverter> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemUnitConverter> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_unit, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ItemUnitConverter itemUnitConverter = mList.get(position);
        holder.txtUnit.setText(itemUnitConverter.getTitle());
        holder.txtResult.setText(String.valueOf(itemUnitConverter.getRes()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtUnit, txtResult;

        public Holder(View itemView) {
            super(itemView);
            txtUnit = (TextView) itemView.findViewById(R.id.txtTitle);
            txtResult = (TextView) itemView.findViewById(R.id.txtResult);
        }
    }
}
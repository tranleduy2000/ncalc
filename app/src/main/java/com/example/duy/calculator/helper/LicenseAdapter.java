package com.example.duy.calculator.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duy.calculator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Duy on 28-Mar-17.
 */

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {
    private static final String TAG = LicenseAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<ItemInfo> listData = new ArrayList<>();
    private Context mContext;

    public LicenseAdapter(Context context, ArrayList<ItemInfo> listData) {
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_info, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindContent(listData.get(position));
//        holder.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, listData.get(position).toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_desc)
        TextView txtDesc;
        @BindView(R.id.container)
        View root;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindContent(ItemInfo itemInfo) {
            txtTitle.setText(itemInfo.getTitle());
            txtDesc.setText(itemInfo.getLink());
        }
    }

}

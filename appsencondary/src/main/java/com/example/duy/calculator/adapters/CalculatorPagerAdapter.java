//package com.example.duy.calculator.adapters;
//
//import android.content.Context;
//import android.support.v4.view.PagerAdapter;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.duy.calculator.R;
//import com.example.duy.calculator.data.Database;
//import com.example.duy.calculator.history.ItemHistory;
//import com.example.duy.calculator.item.ItemVariable;
//import com.example.duy.calculator.view.CalculatorViewPager;
//import com.example.duy.calculator.view.EventListener;
//
//import java.util.ArrayList;
//
///**
// * Created by Duy on 3/7/2016
// */
//public class CalculatorPagerAdapter extends PagerAdapter {
//    private ViewGroup mSimplePad;
//    private ViewGroup mSciencePad;
//    //    private ViewGroup mConstantPad;
//    //    private ViewGroup mHistory;
//    private CalculatorViewPager mParent;
//    private LayoutInflater mInflater;
//    //    private HistoryAdapter mHistoryAdapter;
//    private int mCount = 2;
//    private Context mContext;
//    private Database mDatabase;
//    //    private EventListener mListener;
//    private RecyclerView rcHistory;
//    private ArrayList<ItemHistory> itemHistories = new ArrayList<>();
//
//    public CalculatorPagerAdapter(CalculatorViewPager parent, EventListener listener) {
//        mInflater = LayoutInflater.from(parent.getContext());
//        mContext = parent.getContext();
//        mParent = parent;
////        mListener = listener;
//        mDatabase = new Database(mContext);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public int getCount() {
//        return mCount;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View view = getViewAt(position);
//        container.addView(view);
//        return view;
//    }
//
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        container.removeView((View) object);
//    }
//
//    private void updateValueVariable(String var, String result, View v) {
//        if (var.toLowerCase().equals("a")) {
//            ((TextView) v.findViewById(R.id.txt_var_a)).setText(result);
//        } else if (var.toLowerCase().equals("b")) {
//            ((TextView) v.findViewById(R.id.txt_var_b)).setText(result);
//        } else if (var.toLowerCase().equals("c")) {
//            ((TextView) v.findViewById(R.id.txt_var_c)).setText(result);
//        } else if (var.toLowerCase().equals("d")) {
//            ((TextView) v.findViewById(R.id.txt_var_d)).setText(result);
//        }
//    }
//
//    public View getViewAt(int position) {
//        switch (position) {
//            case 0:
//                mSciencePad = (ViewGroup) mInflater.inflate(R.layout.pad_advance, mParent, false);
//                return mSciencePad;
//            case 1:
//                mSimplePad = (ViewGroup) mInflater.inflate(R.layout.layout_pad_simple, mParent, false);
//                ArrayList<ItemVariable> variables = new Database(mContext).getAllVariable();
//                for (ItemVariable itemVariable : variables) {
//                    updateValueVariable(itemVariable.getKey(), itemVariable.getValue(), mSimplePad);
//                }
//                return mSimplePad;
//        }
//        return null;
//    }
//
////    private void deleteDatabase() {
////        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
////        builder.setMessage("Bạn có muốn xóa lịch sử?");
////        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                mDatabase.deleteAllHistoryMath();
////                mHistoryAdapter.clear();
////            }
////        });
////        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                dialogInterface.cancel();
////            }
////        });
////        builder.create().show();
////    }
//
////    public void onNewHistory(ItemHistory history) {
////        mDatabase.saveHistory(history);
////        itemHistories.add(history);
////        mHistoryAdapter.notifyDataSetChanged();
////        rcHistory.scrollToPosition(mHistoryAdapter.getItemCount() - 1);
////    }
//
//}
//

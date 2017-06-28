package com.example.duy.calculator.define;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.duy.calculator.R;
import com.example.duy.calculator.data.Database;

import java.util.ArrayList;


/**
 * Adapter variabel for recycle view
 */
public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.ViewHolder> {
    private final String TAG = VariableAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<VariableEntry> entries = new ArrayList<>();
    private LayoutInflater inflater;
    private Database database;

    public VariableAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.database = new Database(context);
//        this.entries = database.getAllVariable();
    }

    /**
     * @return list variables
     */
    public ArrayList<VariableEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<VariableEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.variable_line, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        final VariableEntry entry = entries.get(position);
        holder.txtName.setText(entry.getName());
        holder.txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                entry.setName(((EditText) (view)).getText().toString());
            }
        });
        holder.editValue.setText(String.valueOf(entry.getValue()));
        holder.editValue.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return "0123456789+-*/^.!".toCharArray();
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            }
        });
        holder.editValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) entry.setValue(((EditText) (view)).getText().toString());
            }
        });
        holder.editValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                entry.setValue(editable.toString());
            }
        });
        holder.editValue.requestFocus();
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**
     * add variable item
     */
    public void addVar(VariableEntry a) {
        this.entries.add(a);
        Log.d(TAG, "addVar: " + a.getName());
        this.notifyItemInserted(getItemCount() - 1);
    }

    /**
     * remove item
     */
    public void removeVar(int adapterPosition) {
        database.removeVariable(entries.get(adapterPosition).getName());
        entries.remove(adapterPosition);
        this.notifyItemRemoved(adapterPosition);
    }

    /**
     * add empty variable
     */
    public void add() {
        entries.add(new VariableEntry("", ""));
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * view holder for adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText txtName;
        public EditText editValue;

        public ViewHolder(View itemView) {
            super(itemView);
            editValue = (EditText) itemView.findViewById(R.id.edit_value);
            txtName = (EditText) itemView.findViewById(R.id.name);
        }
    }

}

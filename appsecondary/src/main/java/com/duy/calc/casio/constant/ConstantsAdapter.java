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

package com.duy.calc.casio.constant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.duy.calc.casio.SpannedHelper;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * The custom Adapter for the ListView in the consts list.
 */
public class ConstantsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ConstantToken> constants;
    private LayoutInflater inflater;
    private BaseThread.ResultCallback resultCallback;

    public ConstantsAdapter(Context context, ArrayList<ConstantToken> constants,
                            BaseThread.ResultCallback resultCallback) {
        this.context = context;
        this.constants = constants;
        this.inflater = LayoutInflater.from(context);
        this.resultCallback = resultCallback;
    }

    private static String prettifyConstValue(double d) {
        String s = Double.toString(d);
        String[] parts = s.split("E");
        if (parts.length < 2) {
            String[] temp = s.split("\\.");
            if (temp.length < 2 || temp.length > 2) {
                return s;
            } else if (temp.length == 2) {
                if (temp[0].length() >= 4) {
                    temp[0] = spaceOutString(new StringBuilder(temp[0]).reverse().toString());
                    temp[0] = new StringBuilder(temp[0]).reverse().toString();
                }
                if (temp[1].length() >= 4) {
                    temp[1] = spaceOutString(temp[1]);
                }
                s = temp[0].concat("." + temp[1]);
                return s;
            }
        } else if (parts.length == 2) {
            String[] temp = parts[0].split("\\.");
            if (temp.length < 2 || temp.length > 2) {
                return parts[0].concat(" × 10<sup><small>" + parts[1] + "</small></sup>");
            } else if (temp.length == 2) {
                if (temp[0].length() >= 4) {
                    temp[0] = spaceOutString(new StringBuilder(temp[0]).reverse().toString());
                    temp[0] = new StringBuilder(temp[0]).reverse().toString();
                }
                if (temp[1].length() >= 4) {
                    temp[1] = spaceOutString(temp[1]);
                }
                parts[0] = temp[0].concat("." + temp[1]);
                return parts[0].concat(" × 10<sup><small>" + parts[1] + "</small></sup>");
            }
        }
        return s;
    }

    private static String spaceOutString(String s) {
        return s.replaceAll("(.{3})", "$1 ");
    }

    @Override
    public int getCount() {
        return constants.size();
    }

    @Override
    public ConstantToken getItem(int position) {
        return constants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Prepares the View of each item in the ListView that this Adapter will be attached to.
     *
     * @param position    The index of the item
     * @param convertView The old view that may be reused, or null if not possible
     * @param parent      The parent view
     * @return The newly prepared View that will visually represent the item in the ListView in the given position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        ConstantToken constantToken = getItem(position);
        if (convertView == null) { //For efficiency purposes so that it does not unnecessarily inflate Views
            convertView = inflater.inflate(R.layout.list_item_constant, parent, false);
        }

        //Sets up the child Views within each item in the ListView
        TextView constantName = (TextView) convertView.findViewById(R.id.constantName);
        TextView constantSymbol = (TextView) convertView.findViewById(R.id.constantSymbol);
        TextView constantVal = (TextView) convertView.findViewById(R.id.constantVal);
        TextView constantUnits = (TextView) convertView.findViewById(R.id.constantUnits);

        constantName.setText(constantToken.getName());

        //Set the constant symbol to be the actual symbol, the symbol var of the constant
        //is the numeric value to be displayed in the user's input
        constantSymbol.setText(SpannedHelper.fromHtml(constantToken.getHTML()));

        constantVal.setText(SpannedHelper.fromHtml(prettifyConstValue(constantToken.getNumericValue())));

        constantUnits.setText(SpannedHelper.fromHtml(constantToken.getUnits()));

        //To respond to user touches
        final ConstantToken cnst = constants.get(position); //Makes a constant reference so that cnst can be accessed by an inner class
        convertView.findViewById(R.id.constantContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Token> result = new ArrayList<>();
                result.add(cnst);
                resultCallback.onSuccess(result);
            }
        });
        return convertView;
    }


}
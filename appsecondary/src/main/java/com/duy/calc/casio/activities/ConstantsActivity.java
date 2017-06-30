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

package com.duy.calc.casio.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.duy.calc.casio.token.factory.ConstantFactory;
import com.duy.calc.casio.token.ConstantToken;

import java.util.ArrayList;

/**
 * Created by david on 1/20/2017.
 */

public class ConstantsActivity extends ListViewActivity {
    ArrayList<ConstantToken> constants = new ArrayList<ConstantToken>();//Constants data

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_constant);

        setUpArraylist();
        //Finds the ListView from the inflated History XML so it could be manipulated
        ListView lv = (ListView) findViewById(R.id.constantsList);

        //Attaches the custom Adapter to the ListView so that it can configure the items and their Views within it
        lv.setAdapter(new ConstantsAdapter());
    }

    @Override
    public String getViewTitle() {
        return "Constants";
    }

    private void setUpArraylist() {
        constants.add(ConstantFactory.makeProtonMass());
        constants.add(ConstantFactory.makeNeutronMass());
        constants.add(ConstantFactory.makeElectronMass());
        constants.add(ConstantFactory.makeMounMass());
        constants.add(ConstantFactory.makeBohrRadius());
        constants.add(ConstantFactory.makePlanck());
        //nuclear magenton
        constants.add(ConstantFactory.makeBohrMagneton());
        constants.add(ConstantFactory.makeRedPlanck());
        constants.add(ConstantFactory.makeFineStruct());
//        constants.add(ConstantFactory.makeClassicalElectioRadius());
//        constants.add(ConstantFactory.makeComptonWavelenght());
//        constants.add(ConstantFactory.makeProtonGyromagenticRaito());
//        constants.add(ConstantFactory.makeProtonComptonWavelenght());
//        constants.add(ConstantFactory.makeNewtronComptonWavelenght());
        constants.add(ConstantFactory.makeRydberg());
        constants.add(ConstantFactory.makeAtomicMass());
//        constants.add(ConstantFactory.makeProtonMagenticMoment());
//        constants.add(ConstantFactory.makeElectronMagenticMoment());
//        constants.add(ConstantFactory.makeMounMagenticMoment());
        constants.add(ConstantFactory.makeFaraday());
        constants.add(ConstantFactory.makeElemCharge());
        constants.add(ConstantFactory.makeAvogadro());
        constants.add(ConstantFactory.makeBoltzmann());
//        constants.add(ConstantFactory.makeMolarVolumeOfIdealGas());
        constants.add(ConstantFactory.makeMolarGas());
        constants.add(ConstantFactory.makeSpeedOfLight());
//        constants.add(ConstantFactory.makeFirstRadiation());
//        constants.add(ConstantFactory.makeSecondRadiation());
        constants.add(ConstantFactory.makeStefanBoltzmann());
        constants.add(ConstantFactory.makeElectric());
        constants.add(ConstantFactory.makeMagnetic());
        constants.add(ConstantFactory.makeMagneticFluxQuantum());
        //standard acceleration of gravity
        //conductance quantum
        //characteristic impedance of vaccuum
//        constants.add(ConstantFactory.makeCeliusTemp());
        constants.add(ConstantFactory.makeNewtonianOfGravity());
//        constants.add(ConstantFactory.makeStardardAtmosphere());

/*
        constants.add(ConstantFactory.makeCoulomb());
        constants.add(ConstantFactory.makeElectronVolt());
        constants.add(ConstantFactory.makeEarthGrav());
        constants.add(ConstantFactory.makeEarthMass());
        constants.add(ConstantFactory.makeEarthRadius());
        constants.add(ConstantFactory.makeSolarMass());
        constants.add(ConstantFactory.makeSolarRadius());
        constants.add(ConstantFactory.makeSolarLuminosity());
        constants.add(ConstantFactory.makeAU());
        constants.add(ConstantFactory.makeLightYear());
        constants.add(ConstantFactory.makePhi());
        constants.add(ConstantFactory.makeEulerMascheroni());*/
    }

    /**
     * The custom Adapter for the ListView in the consts list.
     */
    private class ConstantsAdapter extends BaseAdapter {

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
            ConstantToken constant = getItem(position);
            if (convertView == null) { //For efficiency purposes so that it does not unnecessarily inflate Views
                //Inflates the XML file to get the View of the consts element
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_constant, parent, false);
            }

            //Sets up the child Views within each item in the ListView
            TextView constantName = (TextView) convertView.findViewById(R.id.constantName);
            TextView constantSymbol = (TextView) convertView.findViewById(R.id.constantSymbol);
            TextView constantVal = (TextView) convertView.findViewById(R.id.constantVal);
            TextView constantUnits = (TextView) convertView.findViewById(R.id.constantUnits);


            constantName.setText(constant.getName());

            //Set the constant symbol to be the actual symbol, the symbol var of the constant
            //is the numeric value to be displayed in the user's input
            constantSymbol.setText(Html.fromHtml(constant.getHTML()));

            constantVal.setText(Html.fromHtml(prettifyConstValue(constant.getNumericValue())));

            constantUnits.setText(Html.fromHtml(constant.getUnits()));

            //To respond to user touches
            final ConstantToken cnst = constants.get(position); //Makes a constant reference so that cnst can be accessed by an inner class
            convertView.findViewById(R.id.constantContainer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    DisplayView displayView = ComputeListener.getInstance().activity.mDisplay;
                    ComputeListener.getInstance().expression.add(
                            displayView.getCursorIndex(),
                            VariableFactory.makeConstantToken(cnst)); //Adds the token to the input
                    ComputeListener.getInstance().updateInput();
                    displayView.setCursorIndex(displayView.getCursorIndex() + 1); //Moves the cursor to the right of the constant
                    ComputeListener.getInstance().updateOutput();*/
                    finish();
                }
            });
            return convertView;
        }
    }
}

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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.duy.calculator.R;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.token.factory.ConstantFactory;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * Created by Duy on 26-Jun-17.
 */

public class ConstantFragment extends AppCompatDialogFragment {
    public static ConstantFragment newInstance() {
        Bundle args = new Bundle();
        ConstantFragment fragment = new ConstantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    private ConstantSelectListener constantSelectListener;
    private ArrayList<ConstantToken> constants = new ArrayList<>();//Constants data

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.constantSelectListener = (ConstantSelectListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_constant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.constanst);
        addConstantsToList();
        //Finds the ListView from the inflated History XML so it could be manipulated
        ListView listView = (ListView) view.findViewById(R.id.constantsList);
        //Attaches the custom Adapter to the ListView so that it can configure the items and their Views within it
        listView.setAdapter(new ConstantsAdapter(getActivity().getApplicationContext(),
                constants, new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<Token> result) {
                if (constantSelectListener != null) {
                    constantSelectListener.onConstantSelected((ConstantToken) result.get(0));
                }
                dismiss();
            }

            @Override
            public void onError(Exception e) {
            }
        }));
    }

    private void addConstantsToList() {
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

    public interface ConstantSelectListener {
        void onConstantSelected(ConstantToken constantToken);
    }
}

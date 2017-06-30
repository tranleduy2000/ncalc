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

package com.duy.calc.casio.calculator.listener.compute;

import android.view.View;

import com.example.duy.calculator.R;

/**
 * Created by Duy on 28-Jun-17.
 */

public class HyperbolicHandler implements View.OnClickListener {
    private ComputeListener listener;

    public HyperbolicHandler(ComputeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_one:
                listener.restoreDefaultDisplay();
                listener.clickSinh();
                break;
            case R.id.btn_two:
                listener.restoreDefaultDisplay();
                listener.clickCosh();
                break;
            case R.id.btn_three:
                listener.restoreDefaultDisplay();
                listener.clickTanh();
                break;
            case R.id.btn_four:
                listener.restoreDefaultDisplay();
                listener.clickASinh();
                break;
            case R.id.btn_five:
                listener.restoreDefaultDisplay();
                listener.clickACosh();
                break;
            case R.id.btn_six:
                listener.restoreDefaultDisplay();
                listener.clickATanh();
                break;
            case R.id.btn_hyp:
                listener.restoreDefaultDisplay();
                break;
            case R.id.btn_clear:
                listener.restoreDefaultDisplay();
                listener.clickClear();
                break;
            default:
                listener.getDisplay().showToast("Select by press 1 to 6");
                break;
        }
    }
}

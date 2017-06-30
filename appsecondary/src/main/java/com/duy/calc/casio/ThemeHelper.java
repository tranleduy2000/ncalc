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

package com.duy.calc.casio;

import android.content.Context;

import com.example.duy.calculator.R;

/**
 * Created by david on 1/15/2017.
 */

public class ThemeHelper {

    public final static int BLACK_AND_YELLOW = 2;
    public final static int DEFAULT_THEME = 2;
    public final static int DAVID = 1;
    public final static int PURPLE = 3;
    public final static int BLUE = 4;
    public final static int BLUEGREEN = 5;
    public final static int DONATE = 6;

    public static int setUpTheme(Context context, boolean appbar) {
        if (true) {
            context.setTheme(appbar ?
                    R.style.CasioAppTheme_David :
                    R.style.CasioAppThemeNoActionBar_David);
            return DAVID;
        }
        return -1;
    }
}

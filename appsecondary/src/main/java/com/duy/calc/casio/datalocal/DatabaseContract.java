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

package com.duy.calc.casio.datalocal;

import android.support.annotation.Nullable;

import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * Created by Duy on 28-Jun-17.
 */

public class DatabaseContract {
    public interface View {
        void setPresenter(DatabaseContract.Presenter presenter);

        void onSaveSuccess();

        void onSaveError(@Nullable Exception e);

        void onReadError(@Nullable Exception e);

        void onReadSuccess();
    }

    public interface Presenter {
        void saveVariable();

        void saveHistory();

        void restoreHistory();

        ArrayList<ArrayList<Token>> getHistory();

        void restoreVariable();
    }
}

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

package com.duy.calc.casio.evaluator;

import android.content.Context;

import com.duy.calc.casio.token.Token;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static com.duy.calc.casio.keyboardlistener.BasicListener.HISTORY_SIZE;

/**
 * Created by Duy on 23-Jun-17.
 */

public class HistoryManager {
    /**
     * Saves the equation into the calculation history.
     *
     * @param input  The expression that the user inputted into the calculator
     * @param output The result of the calculation
     */
    public static void saveEquation(ArrayList<Token> input, ArrayList<Token> output, String filepath, Context context)
            throws IOException, ClassNotFoundException {
        ArrayList<Object[]> history = new ArrayList<>();
        try {
            FileInputStream inStream = context.openFileInput(filepath);
            ObjectInputStream objectStreamIn = new ObjectInputStream(inStream);
            history = (ArrayList<Object[]>) objectStreamIn.readObject();
        } catch (Exception e) {
        }

        FileOutputStream outStream = context.openFileOutput(filepath, Context.MODE_PRIVATE);
        Object[] toWrite = new Object[2];
        toWrite[0] = input;
        toWrite[1] = output;
        history.add(toWrite);

        while (history.size() > HISTORY_SIZE) {
            history.remove(0);
        }

        ObjectOutputStream objectStreamOut = new ObjectOutputStream(outStream);
        objectStreamOut.writeObject(history);
        objectStreamOut.flush();
        objectStreamOut.close();
        outStream.close();
    }

    /**
     * Saves the equation into the calculation history.
     *
     * @param input  The expression that the user inputted into the calculator
     */
    public static void saveEquation(ArrayList<Token> input, String filepath, Context context)
            throws IOException, ClassNotFoundException {
        ArrayList<Object[]> history = new ArrayList<>();
        try {
            FileInputStream inStream = context.openFileInput(filepath);
            ObjectInputStream objectStreamIn = new ObjectInputStream(inStream);
            history = (ArrayList<Object[]>) objectStreamIn.readObject();
        } catch (Exception e) {
        }

        FileOutputStream outStream = context.openFileOutput(filepath, Context.MODE_PRIVATE);
        ArrayList[] toWrite = new ArrayList[2];
        toWrite[0] = input;
        toWrite[1] = new ArrayList<>();
        history.add(toWrite);

        while (history.size() > HISTORY_SIZE) {
            history.remove(0);
        }

        ObjectOutputStream objectStreamOut = new ObjectOutputStream(outStream);
        objectStreamOut.writeObject(history);
        objectStreamOut.flush();
        objectStreamOut.close();
        outStream.close();
    }
}

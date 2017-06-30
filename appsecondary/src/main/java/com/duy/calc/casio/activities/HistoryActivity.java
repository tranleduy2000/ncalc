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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.duy.calculator.R;
import com.duy.calc.casio.naturalview.DisplayView;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * Created by david on 1/15/2017.
 */
public class HistoryActivity extends AbstractActivity {
    public static final double HISTORY_IO_RATIO = 0.7;
    public static final String FILENAME = "FILENAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.history);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

/*
        String filename = getIntent().getStringExtra(FILENAME);


        //Retrieves the user data from saved memory
        ArrayList<Object[]> history;
        try {
            FileInputStream stream = openFileInput(filename);
            ObjectInputStream objectStream = new ObjectInputStream(stream);
            history = (ArrayList<Object[]>) objectStream.readObject();
            //Reverses the order so that the most recent is at the top
            Collections.reverse(history);
        } catch (FileNotFoundException e) { //No history
            history = new ArrayList<>();

            ArrayList<Token> list1 = new ArrayList<>();
            ArrayList<Token> list2 = new ArrayList<>();

            list1.add(new StringToken("No History to show"));
            list2.add(new StringToken(""));

            ArrayList<Token>[] message = new ArrayList[2];
            message[0] = list1;
            message[1] = list2;
            history.add(message);
        } catch (ClassNotFoundException e) {
            history = new ArrayList<>();

            ArrayList<Token> list1 = new ArrayList<>();
            ArrayList<Token> list2 = new ArrayList<>();

            list1.add(new StringToken("No History to show"));
            list2.add(new StringToken(""));

            ArrayList<Token>[] message = new ArrayList[2];
            message[0] = list1;
            message[1] = list2;
            history.add(message);
        } catch (IOException e) {
            history = new ArrayList<>();

            ArrayList<Token> list1 = new ArrayList<>();
            ArrayList<Token> list2 = new ArrayList<>();

            list1.add(new StringToken("No History to show"));
            list2.add(new StringToken(""));

            ArrayList<Token>[] message = new ArrayList[2];
            message[0] = list1;
            message[1] = list2;
            history.add(message);
        }

        //Finds the ListView from the inflated History XML so it could be manipulated
        ListView lv = (ListView) findViewById(R.id.historyList);

        //Attaches the custom Adapter to the ListView so that it can configure the items and their Views within it
        lv.setAdapter(new HistoryAdapter(history));*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    /**
     * The custom Adapter for the ListView in the calculation history.
     */
    private class HistoryAdapter extends BaseAdapter {
        private GestureDetector gestureDetector;
        private ArrayList<Object[]> history; //The data that will be shown in the ListView

        public HistoryAdapter(ArrayList<Object[]> history) {
            this.history = history;
            gestureDetector = new GestureDetector(getApplicationContext(), new SingleTapUp());
        }

        @Override
        public int getCount() {
            return history.size();
        }

        @Override
        public Object getItem(int position) {
            return history.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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
            if (convertView == null) { //For efficiency purposes so that it does not unnecessarily inflate Views
                //Inflates the XML file to get the View of the history element
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.history_element, parent, false);
            }

            //Sets up the child Views within each item in the ListView
            DisplayView input = (DisplayView) convertView.findViewById(R.id.input);
            DisplayView output = (DisplayView) convertView.findViewById(R.id.output);

            //Sets the font size of each DisplayView
//            input.setFontSize(KeyboardListenerImpl.getInstance().activity.getFontSize());
//            output.setFontSize((int) (KeyboardListenerImpl.getInstance().activity.getFontSize() * HISTORY_IO_RATIO));

            //Enters the appropriate expressions to the DisplayView
            Object[] entry = history.get(position);
            input.display((ArrayList<Token>) entry[0]);
            output.display((ArrayList<Token>) entry[1]);

            //To respond to user touches
            final ArrayList<Token> INPUT = (ArrayList<Token>) history.get(position)[0]; //Makes a constant reference so that history can be accessed by an inner class
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        ArrayList<Token> input = new ArrayList<>();
                        //Removes any StringTokens
                        for (Token t : INPUT) {
                            if (!(t instanceof StringToken)) {
                                input.add(t);
                            }
                        }
/*
                        //Adds the input expression to the current expression
                        BasicListener.getInstance().expression.addAll(BasicListener.getInstance().activity.mDisplay.getCursorIndex(), input); //Adds the input of the entry
                        int cursorChange = 0;
                        for (Token t : input) { //Determines the correct cursor position after the inserted expression
                            if (!(t instanceof PlaceholderToken && (t.getType() == PlaceholderToken.SUPERSCRIPT_BLOCK || t.getType() == PlaceholderToken.BASE_BLOCK) ||
                                    (t instanceof OperatorToken && t.getType() == OperatorToken.VARROOT) ||
                                    (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_OPEN) ||
                                    (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) ||
                                    (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_OPEN) ||
                                    (t instanceof BracketToken && t.getType() == BracketToken.FRACTION_CLOSE) ||
                                    (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION))) {
                                cursorChange++;
                            }
                        }
                        BasicListener.getInstance().updatePlaceHolders();
                        BasicListener.getInstance().activity.mDisplay.setCursorIndex(BasicListener.getInstance().activity.mDisplay.getCursorIndex() + cursorChange);*/
                        finish(); //Exits history once an Item has been selected
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            return convertView;
        }

        private class SingleTapUp extends GestureDetector.SimpleOnGestureListener { //CLASSCEPTION

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return true;
            }
        }

    }
}

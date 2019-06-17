/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duy.ncalc.utils.DLog;

import java.util.ArrayList;

import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.CREATE_TABLE_HISTORY;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.HISTORY_DATABASE_NAME;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.KEY_HISTORY_COLOR;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.KEY_HISTORY_ID;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.KEY_HISTORY_INPUT;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.KEY_HISTORY_RESULT;
import static com.duy.calculator.history.DatabaseHelper.HISTORY_DATABASE.KEY_HISTORY_TYPE;
import static com.duy.calculator.history.DatabaseHelper.VARIABLE_DATABASE.CREATE_TABLE_VARIABLE;
import static com.duy.calculator.history.DatabaseHelper.VARIABLE_DATABASE.KEY_VAR_NAME;
import static com.duy.calculator.history.DatabaseHelper.VARIABLE_DATABASE.KEY_VAR_VALUE;
import static com.duy.calculator.history.DatabaseHelper.VARIABLE_DATABASE.TABLE_VARIABLE_NAME;

/**
 * Created by Duy on 3/7/2016
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history_manager";
    public static final int DATABASE_VERSION = 5;
    private String TAG = DatabaseHelper.class.getName();

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_VARIABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_DATABASE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLE_NAME);
        onCreate(db);
    }

    /**
     * Save item history to database
     *
     * @param resultEntry - {@link ResultEntry}
     * @return - <tt>true</tt> if ok, otherwise <tt>false</tt>
     */
    public long saveHistory(ResultEntry resultEntry) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HISTORY_ID, resultEntry.getTime());
        contentValues.put(KEY_HISTORY_INPUT, resultEntry.getExpression());
        contentValues.put(KEY_HISTORY_RESULT, resultEntry.getResult());
        contentValues.put(KEY_HISTORY_COLOR, resultEntry.getColor());
        contentValues.put(KEY_HISTORY_TYPE, resultEntry.getType());
        return sqLiteDatabase.insert(HISTORY_DATABASE_NAME, null, contentValues);
    }

    public ArrayList<ResultEntry> getAllItemHistory() {
        ArrayList<ResultEntry> itemHistories = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + HISTORY_DATABASE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        long time = cursor.getLong(cursor.getColumnIndex(KEY_HISTORY_ID));
                        String math = cursor.getString(cursor.getColumnIndex(KEY_HISTORY_INPUT));
                        String result = cursor.getString(cursor.getColumnIndex(KEY_HISTORY_RESULT));
                        int color = cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_COLOR));
                        int type = cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_TYPE));
                        ResultEntry resultEntry = new ResultEntry(math, result, color, time, type);
                        itemHistories.add(resultEntry);
                        Log.d(TAG, "getAllItemHistory: " + time + " " + math + " " + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemHistories;
    }

    public long removeItemHistory(ResultEntry resultEntry) {
        long id = resultEntry.getTime();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long res = -1;
        try {
            res = sqLiteDatabase.delete(HISTORY_DATABASE_NAME, KEY_HISTORY_ID + "=?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public long removeItemHistory(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long res = -1;
        try {
            res = sqLiteDatabase.delete(HISTORY_DATABASE_NAME, KEY_HISTORY_ID + "=?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * clear all history mHistoryDatabase
     *
     * @return
     */
    public long clearHistory() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(HISTORY_DATABASE_NAME, null, null);
    }

    /**
     * add new variable
     *
     * @param name
     * @param value
     * @return
     */
    public long addVariable(String name, String value) {
        Log.d(TAG, "addVariable: " + name + " =" + value);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_VAR_NAME, name);
        contentValues.put(KEY_VAR_VALUE, value);
        return sqLiteDatabase.insert(TABLE_VARIABLE_NAME, null, contentValues);
    }

    /**
     * remove variable
     *
     * @param name - name of var
     * @return
     */
    public long removeVariable(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long res = -1;
        try {
            res = sqLiteDatabase.delete(TABLE_VARIABLE_NAME, KEY_VAR_NAME + "=?", new String[]{String.valueOf(name)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * update value of variable
     *
     * @param name
     * @param newValue
     */
    public void updateValueVariable(String name, String newValue) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_VAR_NAME, name);
        contentValues.put(KEY_VAR_VALUE, newValue);
        Log.d(TAG, "updateValueVariable: " + name + " " + newValue);
        long res = sqLiteDatabase.update(TABLE_VARIABLE_NAME, contentValues, KEY_VAR_NAME + "=?", new String[]{name});
        Log.d(TAG, "updateValueVariable: " + res);
    }

    public void saveHistory(ArrayList<ResultEntry> histories) {
        for (ResultEntry h : histories) {
            saveHistory(h);
        }
        DLog.d(TAG, "saveHistory: ok");
    }

    public static final class VARIABLE_DATABASE {
        public static final String TABLE_VARIABLE_NAME = "tbl_variable";
        public static final String KEY_VAR_NAME = "name";
        public static final String KEY_VAR_VALUE = "value";
        public static final String CREATE_TABLE_VARIABLE =
                "CREATE TABLE " + TABLE_VARIABLE_NAME +
                        "(" + KEY_VAR_NAME + " TEXT PRIMARY KEY, "
                        + KEY_VAR_VALUE + " TEXT" + ")";
    }

    public static final class HISTORY_DATABASE {
        public static final String HISTORY_DATABASE_NAME = "tbl_history";
        public static final String KEY_HISTORY_ID = "time";
        public static final String KEY_HISTORY_INPUT = "math";
        public static final String KEY_HISTORY_RESULT = "result";
        public static final String KEY_HISTORY_COLOR = "color";
        public static final String KEY_HISTORY_TYPE = "color";
        public static final String CREATE_TABLE_HISTORY =
                "create table " + HISTORY_DATABASE_NAME + "(" +
                        KEY_HISTORY_ID + " LONG PRIMARY KEY, " +
                        KEY_HISTORY_INPUT + " TEXT, " +
                        KEY_HISTORY_RESULT + " TEXT, " +
                        KEY_HISTORY_COLOR + " INTEGER," +
                        KEY_HISTORY_TYPE + "INTEGER" + ")";
    }
}

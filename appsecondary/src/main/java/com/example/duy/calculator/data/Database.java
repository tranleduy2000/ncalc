package com.example.duy.calculator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.duy.calculator.define.VariableEntry;
import com.example.duy.calculator.history.HistoryEntry;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * SQ lite database for calc
 * include history, variable, ...
 * Created by Duy on 3/7/2016
 */
public class Database extends SQLiteOpenHelper implements Serializable {

    private static final String DATABASE_NAME = "history_manager";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_HISTORY = "tbl_history";
    private static final String KEY_ID = "time";
    private static final String KEY_MATH = "math";
    private static final String KEY_RESULT = "result";
    private static final String KEY_COLOR = "color";
    private static final String KEY_TYPE = "color";
    public static final String CREATE_TABLE_HISTORY =
            "create table " + TABLE_HISTORY +
                    "(" +
                    KEY_ID + " LONG PRIMARY KEY, " +
                    KEY_MATH + " TEXT, " +
                    KEY_RESULT + " TEXT, " +
                    KEY_COLOR + " INTEGER," +
                    KEY_TYPE + "INTEGER" +
                    ")";

    private static final String TABLE_VARIABLE = "tbl_variable";
    private static final String KEY_VAR_NAME = "name";
    private static final String KEY_VAR_VALUE = "value";

    public static final String CREATE_TABLE_VARIABLE =
            "CREATE TABLE " + TABLE_VARIABLE +
                    "(" + KEY_VAR_NAME + " TEXT PRIMARY KEY, " + KEY_VAR_VALUE + " TEXT" + ")";

    private String TAG = Database.class.getName();

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_VARIABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLE);
        onCreate(db);
    }

    public long saveHistory(HistoryEntry historyEntry) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, historyEntry.getTime());
        contentValues.put(KEY_MATH, historyEntry.getMath());
        contentValues.put(KEY_RESULT, historyEntry.getResult());
        contentValues.put(KEY_COLOR, historyEntry.getColor());
        contentValues.put(KEY_TYPE, historyEntry.getType());
        return sqLiteDatabase.insert(TABLE_HISTORY, null, contentValues);
    }

    public ArrayList<HistoryEntry> getAllItemHistory() {
        ArrayList<HistoryEntry> itemHistories = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long time = cursor.getLong(cursor.getColumnIndex(KEY_ID));
                String math = cursor.getString(cursor.getColumnIndex(KEY_MATH));
                String result = cursor.getString(cursor.getColumnIndex(KEY_RESULT));
                int color = cursor.getInt(cursor.getColumnIndex(KEY_COLOR));
                int type = cursor.getInt(cursor.getColumnIndex(KEY_TYPE));
                HistoryEntry historyEntry = new HistoryEntry(math, result, color, time, type);
                itemHistories.add(historyEntry);
                Log.d(TAG, "getAllItemHistory: " + time + " " + math + " " + result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemHistories;
    }

    public long removeItemHistory(HistoryEntry historyEntry) {
        long id = historyEntry.getTime();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long res = -1;
        try {
            res = sqLiteDatabase.delete(TABLE_HISTORY, KEY_ID + "=?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public long removeItemHistory(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long res = -1;
        try {
            res = sqLiteDatabase.delete(TABLE_HISTORY, KEY_ID + "=?", new String[]{String.valueOf(id)});
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
        return sqLiteDatabase.delete(TABLE_HISTORY, null, null);
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
        return sqLiteDatabase.insert(TABLE_VARIABLE, null, contentValues);
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
            res = sqLiteDatabase.delete(TABLE_VARIABLE, KEY_VAR_NAME + "=?", new String[]{String.valueOf(name)});
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
        long res = sqLiteDatabase.update(TABLE_VARIABLE, contentValues, KEY_VAR_NAME + "=?", new String[]{name});
        Log.d(TAG, "updateValueVariable: " + res);
    }

    /**
     * return list var
     *
     * @return
     */
    public ArrayList<VariableEntry> getAllVariable() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VARIABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<VariableEntry> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String key = cursor.getString(cursor.getColumnIndex(KEY_VAR_NAME));
                String value = cursor.getString(cursor.getColumnIndex(KEY_VAR_VALUE));
                list.add(new VariableEntry(key, value));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void saveHistory(ArrayList<HistoryEntry> histories) {
        for (HistoryEntry h : histories) {
            saveHistory(h);
        }
        Log.d(TAG, "saveHistory: ok");
    }
}

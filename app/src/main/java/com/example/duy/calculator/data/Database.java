package com.example.duy.calculator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.define.VariableEntry;
import com.example.duy.calculator.history.HistoryEntry;

import java.util.ArrayList;

import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.CREATE_TABLE_HISTORY;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.HISTORY_DATABASE_NAME;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.KEY_HISTORY_COLOR;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.KEY_HISTORY_ID;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.KEY_HISTORY_INPUT;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.KEY_HISTORY_RESULT;
import static com.example.duy.calculator.data.Database.HISTORY_DATABASE.KEY_HISTORY_TYPE;
import static com.example.duy.calculator.data.Database.VARIABLE_DATABASE.CREATE_TABLE_VARIABLE;
import static com.example.duy.calculator.data.Database.VARIABLE_DATABASE.KEY_VAR_NAME;
import static com.example.duy.calculator.data.Database.VARIABLE_DATABASE.KEY_VAR_VALUE;
import static com.example.duy.calculator.data.Database.VARIABLE_DATABASE.TABLE_VARIABLE_NAME;

/**
 * Created by Duy on 3/7/2016
 */
public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history_manager";
    public static final int DATABASE_VERSION = 5;
    public static final long serialVersionUID = 2123122312L;
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
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_DATABASE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLE_NAME);
        onCreate(db);
    }

    /**
     * Save item history to database
     *
     * @param historyEntry - {@link HistoryEntry}
     * @return - <tt>true</tt> if ok, otherwise <tt>false</tt>
     */
    public long saveHistory(HistoryEntry historyEntry) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HISTORY_ID, historyEntry.getTime());
        contentValues.put(KEY_HISTORY_INPUT, historyEntry.getMath());
        contentValues.put(KEY_HISTORY_RESULT, historyEntry.getResult());
        contentValues.put(KEY_HISTORY_COLOR, historyEntry.getColor());
        contentValues.put(KEY_HISTORY_TYPE, historyEntry.getType());
        return sqLiteDatabase.insert(HISTORY_DATABASE_NAME, null, contentValues);
    }

    public ArrayList<HistoryEntry> getAllItemHistory() {
        ArrayList<HistoryEntry> itemHistories = new ArrayList<>();
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
                        HistoryEntry historyEntry = new HistoryEntry(math, result, color, time, type);
                        itemHistories.add(historyEntry);
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

    public long removeItemHistory(HistoryEntry historyEntry) {
        long id = historyEntry.getTime();
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

    /**
     * get list variable from database
     * - name: name of variable
     * - value: value of variable
     *
     * @return list variable
     */
    public ArrayList<VariableEntry> getAllVariable() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VARIABLE_NAME;
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

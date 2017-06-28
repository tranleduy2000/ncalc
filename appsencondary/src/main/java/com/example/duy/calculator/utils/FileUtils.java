package com.example.duy.calculator.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * <<<<<<< HEAD
 * File manager
 * =======
 * >>>>>>> refs/remotes/origin/master
 * Created by DUy on 04-Nov-16.
 */

public class FileUtils {
    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * save file
     *
     * @param fileName - name of file
     * @param data     - string content
     * @return - <code>true</code> if success, otherwise <code>false</code>
     */
    public boolean save(String fileName, String data) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            Toast.makeText(context, "Save OK, " + fileName, Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

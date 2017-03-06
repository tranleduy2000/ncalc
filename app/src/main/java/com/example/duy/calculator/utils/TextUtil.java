package com.example.duy.calculator.utils;

import android.widget.TextView;


/**
 * For formatting text in the display
 */
public class TextUtil {
    public static String getCleanText(TextView textView) {
        return textView.getText().toString();
    }

    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }
}

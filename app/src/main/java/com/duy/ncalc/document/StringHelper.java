package com.duy.ncalc.document;

import android.support.annotation.NonNull;

public class StringHelper {
    @NonNull
    public static String capitalize(@NonNull String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i == 0) {
                builder.append(Character.toUpperCase(text.charAt(i)));
            } else {
                if (text.charAt(i - 1) == ' ') {
                    builder.append(Character.toUpperCase(text.charAt(i)));
                } else {
                    builder.append((text.charAt(i)));
                }
            }
        }
        return builder.toString();
    }

    public static String addSpaceBeforeUpperCase(@NonNull String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i)) && i != 0) {
                builder.append(' ');
            }
            builder.append(text.charAt(i));
        }
        return builder.toString().replace("  ", " ");
    }
}

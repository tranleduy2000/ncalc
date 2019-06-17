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

package com.duy.ncalc.utils;

import java.util.ArrayList;

/**
 * Created by edoga on 28-Oct-16.
 */

public class VoiceUtils {
    private static ArrayList<ItemReplace> mReplace = new ArrayList<>();

    public static void init() {
        mReplace.add(new ItemReplace("không", "0"));
        mReplace.add(new ItemReplace("một", "1"));
        mReplace.add(new ItemReplace("hai", "2"));
        mReplace.add(new ItemReplace("ba", "3"));
        mReplace.add(new ItemReplace("bốn", "4"));
        mReplace.add(new ItemReplace("năm", "5"));
        mReplace.add(new ItemReplace("lăm", "5"));
        mReplace.add(new ItemReplace("sáu", "6"));

        mReplace.add(new ItemReplace("bảy", "7"));
        mReplace.add(new ItemReplace("bẩy", "7"));
        mReplace.add(new ItemReplace("tám", "8"));

        mReplace.add(new ItemReplace("chín", "9"));
        mReplace.add(new ItemReplace("chính", "9"));
        mReplace.add(new ItemReplace("mười", "10"));

        mReplace.add(new ItemReplace("cộng", "+"));
        mReplace.add(new ItemReplace("trừ", "-"));
        mReplace.add(new ItemReplace("chừ", "-"));
        mReplace.add(new ItemReplace("nhân", "*"));
        mReplace.add(new ItemReplace("chia", "/"));
        mReplace.add(new ItemReplace("mũ", "^"));
        mReplace.add(new ItemReplace("mủ", "^"));
        mReplace.add(new ItemReplace("giai thừa", "!"));
    }

    public static String replace(String res) {
        init();
        for (int i = 0; i < mReplace.size(); i++) {
            res = res.replace(mReplace.get(i).getText(), mReplace.get(i).getMath());
        }
        return res;
    }

    public static class ItemReplace {
        private String text;
        private String math;

        public ItemReplace(String text, String math) {
            this.text = text;
            this.math = math;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getMath() {
            return math;
        }

        public void setMath(String math) {
            this.math = math;
        }
    }
}

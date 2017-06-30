package com.example.duy.calculator.voice;

class ItemReplace {
    private String text;
    private String math;

    ItemReplace(String text, String math) {
        this.text = text;
        this.math = math;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String getMath() {
        return math;
    }

    public void setMath(String math) {
        this.math = math;
    }
}
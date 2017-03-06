package com.example.duy.calculator.item;

/**
 * Created by Duy on 19/7/2016
 */
public class ItemVariable {
    private String key;
    private String value;

    public ItemVariable(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

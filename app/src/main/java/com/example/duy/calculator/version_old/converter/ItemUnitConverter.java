package com.example.duy.calculator.version_old.converter;

import java.io.Serializable;

/**
 * Created by tranleduy on 27-May-16.
 */
public class ItemUnitConverter implements Serializable {
    public static final long serialVersionUID = 12312312434L;
    private String title;
    private String res;
    private String unit;

    public ItemUnitConverter(String title, String res, String unit) {
        this.title = title;
        this.res = res;
        this.unit = unit;
    }

    public ItemUnitConverter() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

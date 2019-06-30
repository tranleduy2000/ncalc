package com.duy.ncalc.document.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class FunctionDocumentItem implements Serializable {
    @NonNull
    private String assetPath;
    @NonNull
    private String name;
    @Nullable
    private String description;

    public FunctionDocumentItem(@NonNull String assetPath, @NonNull String name, @Nullable String description) {
        this.assetPath = assetPath;
        this.name = name;
        this.description = description;
    }

    @NonNull
    public String getAssetPath() {
        return assetPath;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Override
    @NonNull
    public String toString() {
        return "FunctionDocumentItem{" +
                "assetPath='" + assetPath + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

package com.minehot.palette;

import org.jetbrains.annotations.NotNull;

public class ModernMaterial {
    private final String material;
    private final int variant;

    public ModernMaterial(@NotNull String material, int variant) {
        this.material = material;
        this.variant = variant;
    }

    @NotNull
    public String getMaterial() {
        return material;
    }

    public int getVariant() {
        return variant;
    }
}

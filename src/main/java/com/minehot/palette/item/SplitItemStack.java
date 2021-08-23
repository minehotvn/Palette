package com.minehot.palette.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SplitItemStack {
    private final ItemStack sub;
    private final ItemStack remain;

    public SplitItemStack(@NotNull ItemStack sub, @NotNull ItemStack remain) {
        this.sub = sub;
        this.remain = remain;
    }

    @NotNull
    public ItemStack getSub() {
        return sub;
    }

    @NotNull
    public ItemStack getRemain() {
        return remain;
    }
}

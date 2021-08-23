package com.minehot.palette.gui;

import com.minehot.palette.item.PreparedItem;
import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Setting;
import org.jetbrains.annotations.Nullable;

@Configurable
public class ItemSlot extends PreparedItem {
    @Setting
    private String type;

    @Nullable
    public String getType() {
        return type;
    }
}

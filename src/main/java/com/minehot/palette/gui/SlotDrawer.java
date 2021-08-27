package com.minehot.palette.gui;

import com.minehot.palette.item.PreparedItem;
import org.jetbrains.annotations.NotNull;

public interface SlotDrawer {
    @NotNull
    PreparedItem draw(@NotNull PreparedItem model, int slot);
}

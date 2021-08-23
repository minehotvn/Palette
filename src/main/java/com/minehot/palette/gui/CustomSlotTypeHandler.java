package com.minehot.palette.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomSlotTypeHandler {
    /**
     * Handles specific slot types
     * @param type the current slot type
     * @param guiHandler the GUI handler
     * @param event the fired event
     * @return if {@code false}, the process will passed down to {@link GUIHandler#onClick(InventoryClickEvent, String)}
     */
    boolean handle(@Nullable String type, @NotNull GUIHandler guiHandler, @NotNull InventoryClickEvent event);
}

package com.minehot.palette.gui;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.minehot.palette.item.PreparedItem;
import com.minehot.palette.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GUIHandler implements InventoryHolder {
    private final List<String> itemHolderSlot = new ArrayList<>();
    private Multimap<String, Integer> slotByTypes = LinkedListMultimap.create();
    private Inventory inventory;
    private ItemSlot[] backupLayer;

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @NotNull
    public ItemSlot[] getBackupLayer() {
        return backupLayer;
    }

    public void setBackupLayer(@NotNull ItemSlot[] backupLayer) {
        this.backupLayer = backupLayer;
        Multimap<String, Integer> map = LinkedListMultimap.create();
        for(int i = 0; i < backupLayer.length; i++){
            if(backupLayer[i].getType() != null) {
                map.put(backupLayer[i].getType(), i);
            }
        }
        slotByTypes = map;
    }

    @NotNull
    public List<String> getItemHolderSlot() {
        return itemHolderSlot;
    }

    public void renderBackup() {
        for (int i = 0; i < backupLayer.length; i++) {
            inventory.setItem(i, backupLayer[i].build());
        }
    }

    @Nullable
    public String getSlotType(int slot){
        return backupLayer[slot].getType();
    }

    @NotNull
    public Collection<Integer> getSlotsByType(String type){
        return slotByTypes.get(type);
    }

    @NotNull
    public ItemStack getBackupItem(int slot) {
        return backupLayer[slot].build();
    }

    @NotNull
    public ItemStack getActualItem(int slot){
        ItemStack item = inventory.getItem(slot);
        ItemStack bg = getBackupItem(slot);
        if(item == null || item.isSimilar(bg)) {
            return new ItemStack(Material.AIR, 0);
        } else {
            return item;
        }
    }

    @Nullable
    public ItemStack collectItem(String slotType) {
        Iterator<Integer> i = getSlotsByType(slotType).iterator();
        return i.hasNext() ? getActualItem(i.next()) : null;
    }

    @Nullable
    public ItemStack collectPresentItem(String slotType) {
        return getSlotsByType(slotType).stream()
                .map(this::getActualItem)
                .filter(item -> !ItemUtil.isEmpty(item))
                .findFirst().orElse(null);
    }

    @NotNull
    public List<ItemStack> collectItems(String slotType) {
        return getSlotsByType(slotType).stream()
                .map(this::getActualItem)
                .collect(Collectors.toList());
    }

    public int resetItem(String slotType, @NotNull UnaryOperator<PreparedItem> itemConsumer) {
        return resetItem(slotType, itemConsumer, Integer.MAX_VALUE);
    }

    public int resetItem(String slotType, @NotNull UnaryOperator<PreparedItem> itemConsumer, int max) {
        int i = 0;
        int j = 0;
        for(int slot : getSlotsByType(slotType)) {
            if(i >= max) break;
            ItemStack item = itemConsumer.apply(backupLayer[slot].duplicate()).build();
            inventory.setItem(slot, item);
            j++;
        }
        return j;
    }

    public int resetItem(String slotType, @NotNull SlotDrawer slotDrawer, int max) {
        int i = 0;
        int j = 0;
        for(int slot : getSlotsByType(slotType)) {
            if(i >= max) break;
            ItemStack item = slotDrawer.draw(backupLayer[slot].duplicate(), slot).build();
            inventory.setItem(slot, item);
            j++;
        }
        return j;
    }

    public void setItemOnce(@NotNull String slotType, @Nullable ItemStack item){
        Iterator<Integer> i = getSlotsByType(slotType).iterator();
        if(i.hasNext()) {
            inventory.setItem(i.next(), item);
        }
    }

    public abstract void onClick(@NotNull InventoryClickEvent event, @Nullable String slotType);

    public void onRendered() { }

    public void onDisplayed(@NotNull HumanEntity player) { }

    public boolean canPut(@NotNull String type, @Nullable ItemStack cursor) {
        return true;
    }

    public void onClose(@NotNull HumanEntity player) {
        getItemHolderSlot().stream()
                .flatMap((Function<String, Stream<ItemStack>>) s -> collectItems(s).stream())
                .filter(i -> !ItemUtil.isEmpty(i))
                .forEach(i -> ItemUtil.addToInventory(player, i));
        renderBackup(); // FIX ITEM DUPLICATION
    }

    public int findSuitableSlot(@Nullable ItemStack item) {
        for(String s : getItemHolderSlot()) {
            if (canPut(s, item)) {
                for (int i : getSlotsByType(s)) {
                    if (ItemUtil.isEmpty(getActualItem(i))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}

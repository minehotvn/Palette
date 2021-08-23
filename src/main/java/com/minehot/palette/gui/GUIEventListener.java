package com.minehot.palette.gui;

import com.minehot.palette.item.SplitItemStack;
import com.minehot.palette.utils.EnumUtil;
import com.minehot.palette.utils.ItemUtil;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIEventListener implements Listener {
    private final Plugin plugin;
    private final CustomSlotTypeHandler customSlotTypeHandler;
    private Sound itemAddSound;
    private Sound itemRemoveSound;

    public GUIEventListener(@NotNull Plugin plugin, @Nullable CustomSlotTypeHandler customSlotTypeHandler){
        this.plugin = plugin;
        this.customSlotTypeHandler = customSlotTypeHandler;
        itemAddSound = (Sound) EnumUtil.findEnum(Sound.class, "ENTITY_ITEM_FRAME_ADD_ITEM");
        if(itemAddSound == null) {
            itemAddSound = (Sound) EnumUtil.findEnum(Sound.class, "ENTITY_ITEMFRAME_ADD_ITEM");
        }
        itemRemoveSound = (Sound) EnumUtil.findEnum(Sound.class, "ENTITY_ITEM_FRAME_REMOVE_ITEM");
        if(itemRemoveSound == null) {
            itemRemoveSound = (Sound) EnumUtil.findEnum(Sound.class, "ENTITY_ITEMFRAME_REMOVE_ITEM");
        }
    }

    @EventHandler
    public void handle(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        if(inv == null) return;
        HumanEntity who = event.getWhoClicked();
        if(inv.getHolder() instanceof GUIHandler) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            GUIHandler gh = (GUIHandler) inv.getHolder();
            String type = gh.getSlotType(event.getSlot());
            if(gh.getItemHolderSlot().contains(type)) {
                ItemStack cursor = event.getCursor();
                ItemStack target = event.getCurrentItem();
                boolean dirty = false;
                if(ItemUtil.isEmpty(cursor)) {
                    ItemStack bg = gh.getBackupItem(event.getSlot());
                    if(!ItemUtil.isEmpty(target) && !target.isSimilar(bg)) {
                        ((Player) who).playSound(who.getLocation(), itemRemoveSound, 1f, 1f);
                        event.setCurrentItem(bg);
                        if(event.isShiftClick()) {
                            ItemUtil.addToInventory(who, target);
                        } else {
                            who.setItemOnCursor(target);
                        }
                        dirty = true;
                    }
                } else if(gh.canPut(type, cursor)) {
                    ((Player) who).playSound(who.getLocation(), itemAddSound, 1f, 1f);
                    ItemStack bg = gh.getBackupItem(event.getSlot());
                    if(ItemUtil.isEmpty(target) || target.isSimilar(bg)) {
                        SplitItemStack p = ItemUtil.splitItem(cursor, 1);
                        event.setCurrentItem(p.getSub());
                        who.setItemOnCursor(p.getRemain());
                        dirty = true;
                    } else {
                        SplitItemStack p = ItemUtil.splitItem(cursor, 1);
                        event.setCurrentItem(p.getSub());
                        who.setItemOnCursor(target);
                        ItemUtil.addToInventory(who, p.getRemain());
                        dirty = true;
                    }
                }
                if(dirty && event.getInventory().getHolder() instanceof Refreshable) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ((Refreshable) event.getInventory().getHolder()).refreshGUI((Player) who);
                        }
                    }.runTask(plugin);
                }
            } else {
                if(customSlotTypeHandler != null) {
                    if(!customSlotTypeHandler.handle(type, gh, event)) {
                        gh.onClick(event, type);
                    }
                } else {
                    gh.onClick(event, type);
                }
            }
        } else if(
                event.getClick().isShiftClick() &&
                event.getView().getTopInventory().getHolder() instanceof GUIHandler &&
                !ItemUtil.isEmpty(event.getCurrentItem()) &&
                event.getView().convertSlot(event.getRawSlot()) != event.getRawSlot()
        ) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            GUIHandler gh = (GUIHandler) event.getView().getTopInventory().getHolder();
            int slot = gh.findSuitableSlot(event.getCurrentItem());
            if(slot != -1) {
                SplitItemStack item = ItemUtil.splitItem(event.getCurrentItem(), 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inv.setItem(event.getSlot(), item.getRemain());
                        gh.getInventory().setItem(slot, item.getSub());
                        ((Player) who).playSound(who.getLocation(), itemAddSound, 1f, 1f);
                        if(gh.getInventory().getHolder() instanceof Refreshable) {
                            ((Refreshable) gh.getInventory().getHolder()).refreshGUI((Player) who);
                        }
                    }
                }.runTask(plugin);
            }
        }
    }

    @EventHandler
    public void handle(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof GUIHandler) {
            if(event.getInventory().getHolder() instanceof Refreshable) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ((Refreshable) event.getInventory().getHolder()).refreshGUI((Player) event.getWhoClicked());
                    }
                }.runTask(plugin);
            }
            if(event.getRawSlots().stream().anyMatch(r -> event.getView().convertSlot(r) == r)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handle(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof GUIHandler) {
            GUIHandler gh = (GUIHandler) event.getInventory().getHolder();
            gh.onClose(event.getPlayer());
        }
    }
}

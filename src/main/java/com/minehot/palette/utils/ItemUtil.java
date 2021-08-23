package com.minehot.palette.utils;

import com.minehot.palette.item.SplitItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {
    public static boolean isEmpty(@Nullable ItemStack item){
        return item == null || item.getType() == Material.AIR || item.getType().name().contains("_AIR");
    }

    @NotNull
    public static SplitItemStack splitItem(@NotNull ItemStack item, int size) {
        if(item.getAmount() == 0) {
            return new SplitItemStack(item, item);
        }
        ItemStack sub = item.clone();
        sub.setAmount(size);
        if(item.getAmount() - size <= 0) {
            return new SplitItemStack(sub, new ItemStack(Material.AIR));
        }
        ItemStack remain = item.clone();
        remain.setAmount(Math.max(0, item.getAmount() - size));
        return new SplitItemStack(sub, remain);
    }

    public static void addToInventory(@NotNull HumanEntity entity, @NotNull ItemStack... items){
        Location location = entity.getLocation();
        for(ItemStack i : entity.getInventory().addItem(items).values()){
            location.getWorld().dropItem(location, i);
        }
    }
}

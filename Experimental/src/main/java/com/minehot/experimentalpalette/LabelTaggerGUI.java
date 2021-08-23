package com.minehot.experimentalpalette;

import com.minehot.palette.gui.GUIHandler;
import com.minehot.palette.gui.Refreshable;
import com.minehot.palette.utils.ItemUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.UnaryOperator;

public class LabelTaggerGUI extends GUIHandler implements Refreshable {
    public LabelTaggerGUI(){
        getItemHolderSlot().add("item");
        getItemHolderSlot().add("tag");
        getItemHolderSlot().add("result");
    }

    @Override
    public void refreshGUI(Player whoClicked) { }

    @Override
    public void onClick(InventoryClickEvent event, String slotType) {
        if(slotType == null) return;
        HumanEntity who = event.getWhoClicked();
        if(slotType.equals("confirm")) {
            ItemStack result = collectItem("result");
            if(!ItemUtil.isEmpty(result)){
                who.sendMessage("Result slot must be cleared first!");
                return;
            }
            ItemStack item = collectItem("item");
            if(ItemUtil.isEmpty(item)){
                who.sendMessage("Please place your desired item!");
                return;
            }
            ItemStack tag = collectItem("tag");
            if(ItemUtil.isEmpty(tag)){
                who.sendMessage("Please place your desired tag!");
                return;
            }
            item = item.clone();
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name == null || name.isEmpty()) {
                name = String.format("%s x%d", item.getType().name(), item.getAmount());
            }
            meta.setDisplayName(String.format("%s [%s]", name, tag.getItemMeta().getDisplayName()));
            item.setItemMeta(meta);
            setItemOnce("result", item);
            resetItem("tag", UnaryOperator.identity());
            resetItem("item", UnaryOperator.identity());
        }
    }

    @Override
    public void onRendered() { }

    public boolean canPut(String type, ItemStack cursor) {
        return type == null || !type.equals("result");
    }
}

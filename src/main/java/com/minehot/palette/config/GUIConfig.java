package com.minehot.palette.config;

import com.minehot.palette.gui.GUIHandler;
import com.minehot.palette.gui.ItemSlot;
import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.PostHandler;
import dev.anhcraft.config.annotations.Setting;
import dev.anhcraft.config.utils.ObjectUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configurable
public class GUIConfig {
    @Setting
    public String title;

    @Setting
    public List<String> layout;

    @Setting
    public Map<Character, ItemSlot> items;

    @PostHandler
    private void handle(){
        layout = layout.stream().map(String::trim).collect(Collectors.toList());
        int max = layout.stream().mapToInt(String::length).max().orElse(0);
        for(String s : layout){
            if(s.length() < max) {
                throw new RuntimeException("Invalid layout detected! Max = " + max);
            }
        }
    }

    @NotNull
    public Inventory render(@NotNull Class<? extends GUIHandler> guiHandlerClass) throws InstantiationException {
        GUIHandler guiHandler = (GUIHandler) ObjectUtil.newInstance(guiHandlerClass);
        Inventory inv = Bukkit.createInventory(guiHandler, layout.size() * 9, ChatColor.translateAlternateColorCodes('&', title));
        guiHandler.setInventory(inv);
        ItemSlot[] backupLayer = new ItemSlot[inv.getSize()];
        for(int y = 0; y < layout.size(); y++){
            String s = layout.get(y);
            for(int x = 0; x < s.length(); x++){
                int i = y * 9 + x;
                backupLayer[i] = items.get(s.charAt(x));
            }
        }
        guiHandler.setBackupLayer(backupLayer);
        guiHandler.renderBackup();
        guiHandler.onRendered();
        return inv;
    }

    public void openGUI(@NotNull HumanEntity player, @NotNull Class<? extends GUIHandler> guiHandlerClass) {
        InventoryHolder h = player.getOpenInventory().getTopInventory().getHolder();
        if(h instanceof GUIHandler) {
            ((GUIHandler) h).onClose(player);
        }
        ((Player) player).playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);
        try {
            InventoryView view = player.openInventory(render(guiHandlerClass));
            if(view.getTopInventory().getHolder() instanceof GUIHandler) {
                ((GUIHandler) view.getTopInventory().getHolder()).onDisplayed(player);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

package com.minehot.experimentalpalette;

import com.minehot.palette.config.GUIConfig;
import com.minehot.palette.gui.GUIEventListener;
import com.minehot.palette.utils.ConfigHelper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExperimentalPalette extends JavaPlugin {
    private GUIConfig guiConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        getServer().getPluginManager().registerEvents(new GUIEventListener(this, null), this);

        guiConfig = ConfigHelper.load(GUIConfig.class, getConfig().getConfigurationSection("gui"));

        getCommand("ep").setExecutor((sender, command, label, args) -> {
            if(sender instanceof HumanEntity && sender.hasPermission("ep.open")) {
                guiConfig.openGUI((HumanEntity) sender, LabelTaggerGUI.class);
            }
            return false;
        });
    }
}

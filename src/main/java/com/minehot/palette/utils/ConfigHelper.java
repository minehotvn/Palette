package com.minehot.palette.utils;

import com.minehot.palette.config.adapters.EnchantmentAdapter;
import com.minehot.palette.config.adapters.ItemStackAdapter;
import com.minehot.palette.config.adapters.MaterialAdapter;
import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.defaults.EnumAdapter;
import dev.anhcraft.config.bukkit.BukkitConfigProvider;
import dev.anhcraft.config.bukkit.struct.YamlConfigSection;
import dev.anhcraft.config.schema.SchemaScanner;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ConfigHelper {
    public static final ConfigSerializer SERIALIZER;
    public static final ConfigDeserializer DESERIALIZER;

    static {
        SERIALIZER = BukkitConfigProvider.YAML.createSerializer();
        SERIALIZER.registerTypeAdapter(Material.class, new MaterialAdapter());
        SERIALIZER.registerTypeAdapter(Enchantment.class, new EnchantmentAdapter());
        SERIALIZER.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());

        DESERIALIZER = BukkitConfigProvider.YAML.createDeserializer();
        DESERIALIZER.registerTypeAdapter(Material.class, new MaterialAdapter());
        DESERIALIZER.registerTypeAdapter(Enchantment.class, new EnchantmentAdapter());
        DESERIALIZER.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        EnumAdapter ea = new EnumAdapter();
        ea.preferUppercase(true);
        DESERIALIZER.registerTypeAdapter(Enum.class, ea);
    }

    public static <T> T load(Class<T> clazz, ConfigurationSection section){
        try {
            return DESERIALIZER.transformConfig(Objects.requireNonNull(SchemaScanner.scanConfig(clazz)), new YamlConfigSection(section));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T load(Class<T> clazz, ConfigurationSection section, T dest){
        try {
            return DESERIALIZER.transformConfig(Objects.requireNonNull(SchemaScanner.scanConfig(clazz)), new YamlConfigSection(section), dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void save(Class<T> clazz, ConfigurationSection section, T dest){
        try {
            SERIALIZER.transformConfig(Objects.requireNonNull(SchemaScanner.scanConfig(clazz)), new YamlConfigSection(section), dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

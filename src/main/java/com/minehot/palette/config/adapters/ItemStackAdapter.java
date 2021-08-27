package com.minehot.palette.config.adapters;

import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.TypeAdapter;
import dev.anhcraft.config.struct.SimpleForm;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class ItemStackAdapter implements TypeAdapter<ItemStack> {
    @Override
    public @Nullable SimpleForm simplify(@NotNull ConfigSerializer configSerializer, @NotNull Type type, @NotNull ItemStack itemStack) throws Exception {
        YamlConfiguration temp = new YamlConfiguration();
        temp.set("item", itemStack);
        return SimpleForm.of(temp.saveToString());
    }

    @Override
    public @Nullable ItemStack complexify(@NotNull ConfigDeserializer configDeserializer, @NotNull Type type, @NotNull SimpleForm simpleForm) throws Exception {
        YamlConfiguration temp = new YamlConfiguration();
        temp.loadFromString(simpleForm.asString());
        return temp.getItemStack("item");
    }
}

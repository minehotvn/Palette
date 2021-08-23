package com.minehot.palette.config.adapters;

import com.minehot.palette.utils.MaterialUtil;
import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.TypeAdapter;
import dev.anhcraft.config.struct.SimpleForm;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class MaterialAdapter implements TypeAdapter<Material> {
    @Override
    public @Nullable SimpleForm simplify(@NotNull ConfigSerializer configSerializer, @NotNull Type type, @NotNull Material material) throws Exception {
        return configSerializer.transform(Enum.class, material);
    }

    @Override
    public @Nullable Material complexify(@NotNull ConfigDeserializer configDeserializer, @NotNull Type type, @NotNull SimpleForm simpleForm) throws Exception {
        if(simpleForm.isString()) {
            return MaterialUtil.parse(simpleForm.asString()).orElse(null);
        }
        return null;
    }
}

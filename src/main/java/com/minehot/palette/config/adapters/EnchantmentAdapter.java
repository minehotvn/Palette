package com.minehot.palette.config.adapters;

import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.TypeAdapter;
import dev.anhcraft.config.struct.SimpleForm;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class EnchantmentAdapter implements TypeAdapter<Enchantment> {
    private final Map<String, Enchantment> ENCHANT_MAP = new HashMap<>();
    private final Map<Enchantment, String> REVERSED_ENCHANT_MAP = new HashMap<>();
    private final List<String> AVAILABLE_VANILLA_ENCHANTS = Arrays
            .stream(Enchantment.values())
            .map(Enchantment::getName)
            .collect(Collectors.toList());

    public EnchantmentAdapter() {
        registerLegacy("protection", "PROTECTION_ENVIRONMENTAL");
        registerLegacy("fire protection", "PROTECTION_FIRE");
        registerLegacy("feather falling", "PROTECTION_FALL");
        registerLegacy("blast protection", "PROTECTION_EXPLOSIONS");
        registerLegacy("projectile protection", "PROTECTION_PROJECTILE");
        registerLegacy("respiration", "OXYGEN");
        registerLegacy("aqua affinity", "WATER_WORKER");
        registerLegacy("sharpness", "DAMAGE_ALL");
        registerLegacy("smite", "DAMAGE_UNDEAD");
        registerLegacy("bane of arthropods", "DAMAGE_ARTHROPODS");
        registerLegacy("looting", "LOOT_BONUS_MOBS");
        registerLegacy("efficiency", "DIG_SPEED");
        registerLegacy("unbreaking", "DURABILITY");
        registerLegacy("fortune", "LOOT_BONUS_BLOCKS");
        registerLegacy("power", "ARROW_DAMAGE");
        registerLegacy("punch", "ARROW_KNOCKBACK");
        registerLegacy("flame", "ARROW_FIRE");
        registerLegacy("infinity", "ARROW_INFINITE");
        registerLegacy("luck of the sea", "LUCK");
        registerLegacy("curse of binding", "BINDING_CURSE");
        registerLegacy("curse of vanishing", "VANISHING_CURSE");
        registerLegacy("sweeping edge", "SWEEPING_EDGE");

        for (Enchantment enc : Enchantment.values()) {
            if(!REVERSED_ENCHANT_MAP.containsKey(enc)) {
                registerModern(enc.getName(), enc);
            }
        }
    }

    public void registerLegacy(String encName, String enumName){
        if(AVAILABLE_VANILLA_ENCHANTS.contains(enumName)) {
            registerModern(encName, Enchantment.getByName(enumName));
        }
    }

    public void registerModern(String encName, Enchantment enchantment){
        register(encName, enchantment);
        register(encName.replace(" ", ""), enchantment);
        register(encName.replace(" ", "-"), enchantment);
        register(encName.replace(" ", "_"), enchantment);
    }

    public void register(String encName, Enchantment enchantment){
        encName = encName.toLowerCase();
        ENCHANT_MAP.put(encName, enchantment);
        REVERSED_ENCHANT_MAP.put(enchantment, encName);
    }

    @Nullable
    public Enchantment getEnchant(@NotNull String encName){
        return ENCHANT_MAP.get(encName.toLowerCase());
    }

    @Override
    public @Nullable SimpleForm simplify(@NotNull ConfigSerializer configSerializer, @NotNull Type type, @NotNull Enchantment enchantment) throws Exception {
        return SimpleForm.of(REVERSED_ENCHANT_MAP.get(enchantment));
    }

    @Override
    public @Nullable Enchantment complexify(@NotNull ConfigDeserializer configDeserializer, @NotNull Type type, @NotNull SimpleForm simpleForm) throws Exception {
        return simpleForm.isString() ? getEnchant(Objects.requireNonNull(simpleForm.asString())) : null;
    }
}

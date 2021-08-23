/*
 *
 *     Battle Minigame.
 *     Copyright (c) 2019 by anhcraft.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.minehot.palette.item;

import dev.anhcraft.config.annotations.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Configurable
public class PreparedItem implements Serializable {
    private static final long serialVersionUID = 7808305902298157946L;

    @Setting
    @Path("material")
    @Description("The material that make up this item")
    @Validation(notNull = true, silent = true)
    private Material material = Material.AIR;

    @Setting
    @Path("amount")
    @Description("The amount of items in this stack")
    private int amount = 1;

    @Setting
    @Path("name")
    @Description("The name of this item")
    private String name;

    @Setting
    @Path("damage")
    @Description("The damaged value")
    private int damage;

    @Setting
    @Path("lore")
    @Description("Item's lore")
    @Validation(notNull = true, silent = true)
    private List<String> lore = new ArrayList<>();

    @Setting
    @Path("enchants")
    @Description("Item's enchantments")
    @Validation(notNull = true, silent = true)
    private Map<Enchantment, Integer> enchants = new HashMap<>();

    @Setting
    @Path("flags")
    @Description("Items's flags that used to hide something")
    @Validation(notNull = true, silent = true)
    private List<ItemFlag> flags = new ArrayList<>();

    @Setting
    @Path("unbreakable")
    @Description("Make the item unbreakable")
    private boolean unbreakable;

    @NotNull
    public static PreparedItem of(@Nullable ItemStack itemStack){
        PreparedItem pi = new PreparedItem();
        if(itemStack != null) {
            pi.material = itemStack.getType();
            pi.amount = itemStack.getAmount();
            pi.damage = itemStack.getDurability();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (meta.hasDisplayName()) pi.name = meta.getDisplayName();
                if (meta.hasLore()) pi.lore = meta.getLore();
                pi.flags = new ArrayList<>(meta.getItemFlags());
                pi.enchants = meta.getEnchants();
                pi.unbreakable = meta.isUnbreakable();
            }
        }

        return pi;
    }

    @NotNull
    public Material material() {
        return material;
    }

    public void material(@Nullable Material type) {
        this.material = type == null ? Material.AIR : type;
    }

    @Nullable
    public String name() {
        return name;
    }

    public void name(@Nullable String name) {
        this.name = name;
    }

    public int damage() {
        return damage;
    }

    public void damage(int damage) {
        this.damage = damage;
    }

    public int amount() {
        return amount;
    }

    public void amount(int amount) {
        this.amount = amount;
    }

    public boolean unbreakable() {
        return unbreakable;
    }

    public void unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @NotNull
    public List<String> lore() {
        return lore;
    }

    public void lore(@Nullable List<String> lore) {
        if(lore == null) this.lore.clear();
        else this.lore = lore;
    }

    @NotNull
    public List<ItemFlag> flags() {
        return flags;
    }

    public void flags(@Nullable List<ItemFlag> flags) {
        if(flags == null) this.flags.clear();
        else this.flags = flags;
    }

    @NotNull
    public Map<Enchantment, Integer> enchants() {
        return enchants;
    }

    public void enchants(@Nullable Map<Enchantment, Integer> enchants) {
        if(enchants == null) this.enchants.clear();
        else this.enchants = enchants;
    }

    @NotNull
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount, (short) damage);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            if(name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
            if(!lore.isEmpty()) {
                meta.setLore(lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
            }
            if(!flags.isEmpty()) {
                flags.stream().filter(Objects::nonNull).forEach(meta::addItemFlags);
            }
            if(!enchants.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> e : enchants.entrySet())
                    meta.addEnchant(e.getKey(), e.getValue(), true);
            }
            meta.setUnbreakable(unbreakable);
            item.setItemMeta(meta);
        }
        return item;
    }

    @NotNull
    public PreparedItem duplicate(){
        return copyTo(new PreparedItem());
    }

    @Deprecated
    @NotNull
    public PreparedItem merge(@NotNull PreparedItem pi){
        return copyTo(pi);
    }

    @NotNull
    public PreparedItem copyTo(@NotNull PreparedItem pi){
        pi.name = name;
        pi.damage = damage;
        pi.amount = amount;
        pi.unbreakable = unbreakable;
        pi.material = material;
        pi.enchants.putAll(enchants);
        pi.flags.addAll(flags);
        pi.lore.addAll(lore);
        return pi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreparedItem that = (PreparedItem) o;
        return amount == that.amount && damage == that.damage && unbreakable == that.unbreakable && material == that.material && Objects.equals(name, that.name) && lore.equals(that.lore) && enchants.equals(that.enchants) && flags.equals(that.flags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, amount, name, damage, lore, enchants, flags, unbreakable);
    }

    @NotNull
    public PreparedItem applyPlaceholder(String placeholder, String text) {
        name = name.replace(placeholder, text);
        lore.replaceAll(s -> s.replace(placeholder, text));
        return this;
    }
}

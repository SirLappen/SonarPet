/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.util.menu;

import lombok.*;

import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import net.techcable.sonarpet.item.ItemData;
import net.techcable.sonarpet.item.SkullItemData;
import net.techcable.sonarpet.item.SkullItemData.SkullType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum MenuItem {

    HORSE_TYPE(Material.HAY_BLOCK, 1, DataMenuType.HORSE_TYPE, "Type", "Horse"),
    HORSE_VARIANT(Material.LEASH, 1, DataMenuType.HORSE_VARIANT, "Variant", "Horse"),
    HORSE_MARKING(Material.INK_SACK, 1, DataMenuType.HORSE_MARKING, "Marking", "Horse"),
    HORSE_ARMOUR(Material.IRON_CHESTPLATE, 1, DataMenuType.HORSE_ARMOUR, "Armour", "Horse"),
    RABBIT_TYPE(Material.RABBIT_HIDE, 1, DataMenuType.RABBIT_TYPE, "Bunny type", "Rabbit"),
    CHESTED(Material.CHEST, 1, DataMenuType.BOOLEAN, "Chested", "Horse"),
    FIRE(Material.FIREBALL, 1, DataMenuType.BOOLEAN, "Fire", "Blaze"),
    SADDLE(Material.SADDLE, 1, DataMenuType.BOOLEAN, "Saddle", "Horse", "Pig"),
    SHEARED(Material.SHEARS, 1, DataMenuType.BOOLEAN, "Sheared", "Sheep"),
    SCREAMING(Material.ENDER_PEARL, 1, DataMenuType.BOOLEAN, "Screaming", "Enderman"),
    POTION(Material.POTION, 1, DataMenuType.BOOLEAN, "Potion", "Witch"),
    SHIELD(Material.GLASS, 1, DataMenuType.BOOLEAN, "Shield", "Wither"),
    POWER(Material.BEACON, 1, DataMenuType.BOOLEAN, "Powered", "Creeper"),
    SIZE(Material.SLIME_BALL, 1, DataMenuType.SIZE, "Size", "Slime", "MagmaCube"),
    BABY(Material.WHEAT, 1, DataMenuType.BOOLEAN, "Baby", "PigZombie", "Zombie", "Chicken", "Cow", "Horse", "MushroomCow", "Ocelot", "Pig", "Sheep", "Wolf", "Villager"),
    CAT_TYPE(Material.RAW_FISH, 1, DataMenuType.CAT_TYPE, "Cat Type", "Ocelot"),
    ANGRY(Material.BONE, 1, DataMenuType.BOOLEAN, "Angry", "Wolf"),
    TAMED(Material.BONE, 1, DataMenuType.BOOLEAN, "Tamed", "Wolf"),
    WITHER(SkullItemData.create(SkullType.WITHER_SKELETON_SKULL), 1, DataMenuType.BOOLEAN, "Wither", "Skeleton"),
    VILLAGER(Material.EMERALD, 1, DataMenuType.BOOLEAN, "Villager", "Zombie", "PigZombie"),
    ELDER(Material.SEA_LANTERN, 1, DataMenuType.BOOLEAN, "Elder", "Guardian"),
    COLOR(Material.WOOL, 1, DataMenuType.COLOR, "Color", "Sheep", "Wolf"),
    PROFESSION(Material.IRON_AXE, 1, DataMenuType.PROFESSION, "Profession", "Villager"),
    RIDE(Material.CARROT_STICK, 1, DataMenuType.BOOLEAN, "Ride Pet", "Control your pet."),
    HAT(Material.IRON_HELMET, 1, DataMenuType.BOOLEAN, "Hat Pet", "Wear your pet on your head.");

    private final int amount;
    @Getter
    private final ItemData itemData;
    private final DataMenuType menuType;

    MenuItem(Material material, int amount, DataMenuType menuType, String name, String... lore) {
        this(
                ItemData.create(material),
                amount,
                menuType,
                name,
                lore
        );
    }

    MenuItem(ItemData itemData, int amount, DataMenuType menuType, String name, String... lore) {
        this(
                Preconditions.checkNotNull(itemData, "Null item data")
                        .withDisplayName(ChatColor.RED + Preconditions.checkNotNull(name, "Null name"))
                        .withLore(ImmutableList.copyOf(Preconditions.checkNotNull(lore, "Null lore array"))),
                amount,
                menuType
        );
    }


    MenuItem(ItemData itemData, int amount, DataMenuType menuType) {
        this.itemData = Preconditions.checkNotNull(itemData, "Null item data");
        this.amount = amount;
        this.menuType = Preconditions.checkNotNull(menuType, "Null menu type");
    }

    public ItemStack getItem() {
        return itemData.createStack(amount);
    }

    public ItemStack getBoolean(boolean flag) {
        ItemData itemData = this.itemData;
        itemData = itemData.withDisplayName(itemData.getDisplayName().get() + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
        return itemData.createStack(amount);
    }

    public DataMenuType getMenuType() {
        return this.menuType;
    }
}
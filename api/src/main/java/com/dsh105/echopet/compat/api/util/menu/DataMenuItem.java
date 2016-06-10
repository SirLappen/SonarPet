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

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import net.techcable.sonarpet.item.DyeItemData;
import net.techcable.sonarpet.item.ItemData;
import net.techcable.sonarpet.item.StainedClayItemData;
import net.techcable.sonarpet.item.WoolItemData;

public enum DataMenuItem {
    BOOLEAN_TRUE(DataMenuType.BOOLEAN, null, Material.REDSTONE_TORCH_ON, 1, "True", "Turns the feature on."),
    BOOLEAN_FALSE(DataMenuType.BOOLEAN, null, Material.REDSTONE_TORCH_OFF, 1, "False", "Turns the feature off."),

    BLACK_CAT(DataMenuType.CAT_TYPE, PetData.BLACK, DyeItemData.create(DyeColor.BLACK), 1, "Black", "Cat Type"),
    RED_CAT(DataMenuType.CAT_TYPE, PetData.RED, DyeItemData.create(DyeColor.RED), 1, "Red", "Cat Type"),
    SIAMESE_CAT(DataMenuType.CAT_TYPE, PetData.SIAMESE, DyeItemData.create(DyeColor.SILVER), 1, "Siamese", "Cat Type"),
    WILD_CAT(DataMenuType.CAT_TYPE, PetData.WILD, DyeItemData.create(DyeColor.ORANGE), 1, "Wild", "Cat Type"),

    SMALL(DataMenuType.SIZE, PetData.SMALL, Material.SLIME_BALL, 1, "Small", "Slime Size"),
    MEDIUM(DataMenuType.SIZE, PetData.MEDIUM, Material.SLIME_BALL, 2, "Medium", "Slime Size"),
    LARGE(DataMenuType.SIZE, PetData.LARGE, Material.SLIME_BALL, 4, "Large", "Slime Size"),

    BLACKSMITH(DataMenuType.PROFESSION, PetData.BLACKSMITH, Material.COAL, 1, "Blacksmith", "Villager Profession"),
    BUTCHER(DataMenuType.PROFESSION, PetData.BUTCHER, Material.RAW_BEEF, 1, "Butcher", "Villager Profession"),
    FARMER(DataMenuType.PROFESSION, PetData.FARMER, Material.IRON_HOE, 1, "Farmer", "Villager Profession"),
    LIBRARIAN(DataMenuType.PROFESSION, PetData.LIBRARIAN, Material.BOOK, 1, "Librarian", "Villager Profession"),
    PRIEST(DataMenuType.PROFESSION, PetData.PRIEST, Material.PAPER, 1, "Priest", "Villager Profession"),

    BLACK(DataMenuType.COLOR, PetData.BLACK, WoolItemData.create(DyeColor.BLACK), 1, "Black", "Wool Or Collar Color"),
    BLUE(DataMenuType.COLOR, PetData.BLUE, WoolItemData.create(DyeColor.BLUE), 1, "Blue", "Wool Or Collar Color"),
    BROWN(DataMenuType.COLOR, PetData.BROWN, WoolItemData.create(DyeColor.BROWN), 1, "Brown", "Wool Or Collar Color"),
    CYAN(DataMenuType.COLOR, PetData.CYAN, WoolItemData.create(DyeColor.CYAN), 1, "Cyan", "Wool Or Collar Color"),
    GRAY(DataMenuType.COLOR, PetData.GRAY, WoolItemData.create(DyeColor.GRAY), 1, "Gray", "Wool Or Collar Color"),
    GREEN(DataMenuType.COLOR, PetData.GREEN, WoolItemData.create(DyeColor.GREEN), 1, "Green", "Wool Or Collar Color"),
    LIGHT_BLUE(DataMenuType.COLOR, PetData.LIGHTBLUE, WoolItemData.create(DyeColor.LIGHT_BLUE), 1, "Light Blue", "Wool Or Collar Color"),
    LIME(DataMenuType.COLOR, PetData.LIME, WoolItemData.create(DyeColor.LIME), 1, "Lime", "Wool Or Collar Color"),
    MAGENTA(DataMenuType.COLOR, PetData.MAGENTA, WoolItemData.create(DyeColor.MAGENTA), 1, "Magenta", "Wool Or Collar Color"),
    ORANGE(DataMenuType.COLOR, PetData.ORANGE, WoolItemData.create(DyeColor.ORANGE), 1, "Orange", "Wool Or Collar Color"),
    PINK(DataMenuType.COLOR, PetData.PINK, WoolItemData.create(DyeColor.PINK), 1, "Pink", "Wool Or Collar Color"),
    PURPLE(DataMenuType.COLOR, PetData.PURPLE, WoolItemData.create(DyeColor.PURPLE), 1, "Purple", "Wool Or Collar Color"),
    RED(DataMenuType.COLOR, PetData.RED, WoolItemData.create(DyeColor.RED), 1, "Red", "Wool Or Collar Color"),
    SILVER(DataMenuType.COLOR, PetData.SILVER, WoolItemData.create(DyeColor.SILVER), 1, "Silver", "Wool Or Collar Color"),
    WHITE(DataMenuType.COLOR, PetData.WHITE, WoolItemData.create(DyeColor.WHITE), 1, "White", "Wool Or Collar Color"),
    YELLOW(DataMenuType.COLOR, PetData.YELLOW, WoolItemData.create(DyeColor.YELLOW), 1, "Yellow", "Wool Or Collar Color"),

    NORMAL(DataMenuType.HORSE_TYPE, PetData.NORMAL, Material.HAY_BLOCK, 1, "Normal", "Type"),
    DONKEY(DataMenuType.HORSE_TYPE, PetData.DONKEY, Material.CHEST, 1, "Donkey", "Type"),
    MULE(DataMenuType.HORSE_TYPE, PetData.MULE, Material.CHEST, 1, "Mule", "Type"),
    ZOMBIE(DataMenuType.HORSE_TYPE, PetData.ZOMBIE, Material.ROTTEN_FLESH, 1, "Zombie", "Type"),
    SKELETON(DataMenuType.HORSE_TYPE, PetData.SKELETON, Material.BOW, 1, "Skeleton", "Type"),

    WHITE_HORSE(DataMenuType.HORSE_VARIANT, PetData.WHITE, WoolItemData.create(DyeColor.WHITE), 1, "White", "Variant"),
    CREAMY_HORSE(DataMenuType.HORSE_VARIANT, PetData.CREAMY, WoolItemData.create(DyeColor.YELLOW), 1, "Creamy", "Variant"),
    CHESTNUT_HORSE(DataMenuType.HORSE_VARIANT, PetData.CHESTNUT, StainedClayItemData.create(DyeColor.SILVER), 1, "Chestnut", "Variant"),
    BROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.BROWN, WoolItemData.create(DyeColor.BROWN), 1, "Brown", "Variant"),
    BLACK_HORSE(DataMenuType.HORSE_VARIANT, PetData.BLACK, WoolItemData.create(DyeColor.BLACK), 1, "Black", "Variant"),
    GRAY_HORSE(DataMenuType.HORSE_VARIANT, PetData.GRAY, WoolItemData.create(DyeColor.GRAY), 1, "Gray", "Variant"),
    DARKBROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.DARKBROWN, StainedClayItemData.create(DyeColor.GRAY), 1, "Dark Brown", "Variant"),

    NONE(DataMenuType.HORSE_MARKING, PetData.NONE, Material.LEASH, 1, "None", "Marking"),
    SOCKS(DataMenuType.HORSE_MARKING, PetData.SOCKS, Material.LEASH, 1, "White Socks", "Marking"),
    WHITE_PATCH(DataMenuType.HORSE_MARKING, PetData.WHITEPATCH, Material.LEASH, 1, "White Patch", "Marking"),
    WHITE_SPOTS(DataMenuType.HORSE_MARKING, PetData.WHITESPOT, Material.LEASH, 1, "White Spots", "Marking"),
    BLACK_SPOTS(DataMenuType.HORSE_MARKING, PetData.BLACKSPOT, Material.LEASH, 1, "Black Spots", "Marking"),

    NOARMOUR(DataMenuType.HORSE_ARMOUR, PetData.NOARMOUR, Material.LEASH, 1, "None", "Armour"),
    IRON(DataMenuType.HORSE_ARMOUR, PetData.IRON, Material.IRON_BARDING, 1, "Iron", "Armour"),
    GOLD(DataMenuType.HORSE_ARMOUR, PetData.GOLD, Material.GOLD_BARDING, 1, "Gold", "Armour"),
    DIAMOND(DataMenuType.HORSE_ARMOUR, PetData.DIAMOND, Material.DIAMOND_BARDING, 1, "Diamond", "Armour"),

    BROWN_RABBIT(DataMenuType.RABBIT_TYPE, PetData.BROWN, WoolItemData.create(DyeColor.BROWN), 1, "Brown", "Bunny type"),
    WHITE_RABBIT(DataMenuType.RABBIT_TYPE, PetData.WHITE, WoolItemData.create(DyeColor.WHITE), 1, "White", "Bunny type"),
    BLACK_RABBIT(DataMenuType.HORSE_ARMOUR, PetData.BLACK, WoolItemData.create(DyeColor.BLACK), 1, "Black", "Bunny type"),
    BLACK_AND_WHITE_RABBIT(DataMenuType.RABBIT_TYPE, PetData.BLACK_AND_WHITE, WoolItemData.create(DyeColor.GRAY), 1, "Black and White", "Bunny type"),
    SALT_AND_PEPPER_RABBIT(DataMenuType.RABBIT_TYPE, PetData.SALT_AND_PEPPER, WoolItemData.create(DyeColor.YELLOW), 1, "Salt and Pepper", "Bunny type"),
    KILLER_BUNNY(DataMenuType.RABBIT_TYPE, PetData.THE_KILLER_BUNNY, WoolItemData.create(DyeColor.RED), 1, "Killer Bunny", "Bunny type"),

    BACK(DataMenuType.OTHER, null, Material.BOOK, 1, "Back", "Return to the main menu."),
    CLOSE(DataMenuType.OTHER, null, Material.BOOK, 1, "Close", "Close the Pet Menu");

    private final DataMenuType menuType;
    private final PetData dataLink;
    @Getter
    private final ItemData itemData;
    private final int amount;

    DataMenuItem(DataMenuType menuType, PetData dataLink, Material material, int amount, String name, String... lore) {
        this(
                menuType,
                dataLink,
                ItemData.create(material),
                amount,
                name,
                lore
        );
    }

    DataMenuItem(DataMenuType menuType, PetData dataLink, ItemData itemData, int amount, String name, String... lore) {
        this(
                menuType,
                dataLink,
                Preconditions.checkNotNull(itemData, "Null item data")
                        .withDisplayName(ChatColor.RED + Preconditions.checkNotNull(name, "Null name"))
                        .withLore(ImmutableList.copyOf(Preconditions.checkNotNull(lore, "Null lore array"))),
                amount
        );
    }


    DataMenuItem(DataMenuType menuType, PetData dataLink,ItemData itemData, int amount) {
        this.itemData = Preconditions.checkNotNull(itemData, "Null item data");
        this.amount = amount;
        this.dataLink = dataLink;
        this.menuType = Preconditions.checkNotNull(menuType, "Null menu type");
    }

    public ItemStack getItem() {
        return getItemData().createStack(amount);
    }

    public DataMenuType getMenuType() {
        return this.menuType;
    }

    public PetData getDataLink() {
        return this.dataLink;
    }
}
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

package com.dsh105.echopet.compat.api.config;

import com.dsh105.echopet.compat.api.entity.PetType;
import com.google.common.base.Preconditions;

import net.techcable.sonarpet.item.ItemData;
import net.techcable.sonarpet.item.SkullItemData;
import net.techcable.sonarpet.item.SpawnEggItemData;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;
import org.bukkit.material.SpawnEgg;

public enum PetItem {

    BAT(PetType.BAT, SpawnEggItemData.create(EntityType.BAT), "Bat Pet", "bat"),
    BLAZE(PetType.BLAZE, SpawnEggItemData.create(EntityType.BLAZE), "Blaze Pet", "blaze"),
    CAVESPIDER(PetType.CAVESPIDER, SpawnEggItemData.create(EntityType.CAVE_SPIDER), "Cave Spider Pet", "cavespider"),
    CHICKEN(PetType.CHICKEN, SpawnEggItemData.create(EntityType.CHICKEN), "Chicken Pet", "chicken"),
    COW(PetType.COW, SpawnEggItemData.create(EntityType.COW), "Cow Pet", "cow"),
    CREEPER(PetType.CREEPER, SpawnEggItemData.create(EntityType.CREEPER), "Creeper Pet", "creeper"),
    ENDERDRAGON(PetType.ENDERDRAGON, Material.DRAGON_EGG, "EnderDragon Pet", "enderdragon"),
    ENDERMAN(PetType.ENDERMAN, SpawnEggItemData.create(EntityType.ENDERMAN), "Enderman Pet", "enderman"),
    ENDERMITE(PetType.ENDERMITE, SpawnEggItemData.create(EntityType.ENDERMITE), "Endermite Pet", "endermite"),
    GHAST(PetType.GHAST, SpawnEggItemData.create(EntityType.GHAST), "Ghast Pet", "ghast"),
    GIANT(PetType.GIANT, SpawnEggItemData.create(EntityType.GIANT), "Giant Pet", "giant"),
    GUARDIAN(PetType.GUARDIAN, SpawnEggItemData.create(EntityType.GUARDIAN), "Guardian Pet", "guardian"),
    HORSE(PetType.HORSE, SpawnEggItemData.create(EntityType.HORSE), "Horse Pet", "horse"),
    HUMAN(PetType.HUMAN, SkullItemData.createHuman(), "Human Pet", "human"),
    IRONGOLEM(PetType.IRONGOLEM, Material.PUMPKIN, "Iron Golem Pet", "irongolem"),
    MAGMACUBE(PetType.MAGMACUBE, SpawnEggItemData.create(EntityType.MAGMA_CUBE), "Magma Cube Pet", "magmacube"),
    MUSHROOMCOW(PetType.MUSHROOMCOW, SpawnEggItemData.create(EntityType.MUSHROOM_COW), "Mushroom Cow Pet", "mushroomcow"),
    OCELOT(PetType.OCELOT, SpawnEggItemData.create(EntityType.OCELOT), "Ocelot Pet", "ocelot"),
    PIG(PetType.PIG, SpawnEggItemData.create(EntityType.PIG), "Pig Pet", "pig"),
    PIGZOMBIE(PetType.PIGZOMBIE, SpawnEggItemData.create(EntityType.PIG_ZOMBIE), "PigZombie Pet", "pigzombie"),
    RABBIT(PetType.RABBIT, SpawnEggItemData.create(EntityType.RABBIT), "Rabbit Pet", "rabbit"),
    SHEEP(PetType.SHEEP, SpawnEggItemData.create(EntityType.SHEEP), "Sheep Pet", "sheep"),
    SILVERFISH(PetType.SILVERFISH, SpawnEggItemData.create(EntityType.SILVERFISH), "Silverfish Pet", "silverfish"),
    SKELETON(PetType.SKELETON, SpawnEggItemData.create(EntityType.SKELETON), "Skeleton Pet", "skeleton"),
    SLIME(PetType.SLIME, SpawnEggItemData.create(EntityType.SLIME), "Slime Pet", "slime"),
    SNOWMAN(PetType.SNOWMAN, Material.SNOW_BALL, "Snowman Pet", "snowman"),
    SPIDER(PetType.SPIDER, SpawnEggItemData.create(EntityType.SPIDER), "Spider Pet", "spider"),
    SQUID(PetType.SQUID, SpawnEggItemData.create(EntityType.SQUID), "Squid Pet", "squid"),
    VILLAGER(PetType.VILLAGER, SpawnEggItemData.create(EntityType.VILLAGER), "Villager Pet", "villager"),
    WITCH(PetType.WITCH, SpawnEggItemData.create(EntityType.WITCH), "Witch Pet", "witch"),
    WITHER(PetType.WITHER, Material.NETHER_STAR, "Wither Pet", "wither"),
    WOLF(PetType.WOLF, SpawnEggItemData.create(EntityType.WOLF), "Wolf Pet", "wolf"),
    ZOMBIE(PetType.ZOMBIE, SpawnEggItemData.create(EntityType.ZOMBIE), "Zombie Pet", "zombie");

    private String cmd;
    public PetType petType;
    private final ItemData itemData;

    PetItem(PetType petType, Material type, String name, String cmd) {
        this(petType, ItemData.create(type), name, cmd);
    }

    PetItem(PetType petType, ItemData itemData, String name, String cmd) {
        this.cmd = "pet " + Preconditions.checkNotNull(cmd, "Null command");
        this.petType = Preconditions.checkNotNull(petType, "Null pet type");
        Preconditions.checkNotNull(itemData, "Null item data");
        itemData = itemData.withDisplayName(name);
        this.itemData = itemData;
    }

    public String getCommand() {
        return cmd;
    }

    public PetType getPetType() {
        return petType;
    }

    public Material getMat() {
        return itemData.getType();
    }

    public ItemData getItemData() {
        return itemData;
    }

    public String getName() {
        return getItemData().getDisplayName().get();
    }
}

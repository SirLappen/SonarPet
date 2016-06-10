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

import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.inventory.MenuIcon;
import com.google.common.base.Preconditions;

import net.techcable.sonarpet.item.ItemData;
import net.techcable.sonarpet.item.SkullItemData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class SelectorIcon extends MenuIcon {

    private final String command;
    private final PetType petType;

    public SelectorIcon(int slot, String command, PetType petType, ItemData itemData) {
        super(slot, itemData);
        this.command = Preconditions.checkNotNull(command, "Null command");
        this.petType = petType;
    }

    public String getCommand() {
        return command;
    }

    public PetType getPetType() {
        return petType;
    }

    @Override
    public ItemStack getIcon(Player viewer) {
        ItemData itemData = getItemData();
        ChatColor c = this.petType == null ? ChatColor.YELLOW : (viewer.hasPermission("echopet.pet.type." + this.getPetType().toString().toLowerCase().replace("_", ""))) ? ChatColor.GREEN : ChatColor.RED;
        itemData = itemData.withDisplayName(ChatColor.translateAlternateColorCodes('&', c + this.getName()));

        if (this.petType == PetType.HUMAN && itemData instanceof SkullItemData) {
            itemData = ((SkullItemData) itemData).withOwner(viewer.getUniqueId());
        }
        return itemData.createStack(1);
    }

    @Override
    public void onClick(final Player viewer) {
        viewer.closeInventory();
        if (this.command.equalsIgnoreCase(EchoPet.getPlugin().getCommandString() + " menu")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    viewer.performCommand(getCommand());

                }
            }.runTaskLater(EchoPet.getPlugin(), 5L);
        } else {
            viewer.performCommand(this.getCommand());
        }
    }
}
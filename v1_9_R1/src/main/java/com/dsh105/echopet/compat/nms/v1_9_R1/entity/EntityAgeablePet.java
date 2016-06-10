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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;

import net.minecraft.server.v1_9_R1.EntityAgeable;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftAgeable;
import org.bukkit.entity.Ageable;

public interface EntityAgeablePet extends EntityInsentientPet, IEntityAgeablePet {

    @Override
    public EntityAgeable getEntity();

    @Override
    public CraftAgeable getBukkitEntity();

    @Override
    public EntityAgeablePetData getNmsData();

    public default int getAge() {
        return getEntity().getAge();
    }

    public default void setAge(int age) {
        getEntity().setAgeRaw(age);
    }

    public default boolean isAgeLocked() {
        return getEntity().ageLocked;
    }

    public default void setAgeLocked(boolean ageLocked) {
        getEntity().ageLocked = ageLocked;
    }

    @Override
    public default void setBaby(boolean flag) {
        if (flag) {
            getBukkitEntity().setBaby();
        } else {
            getBukkitEntity().setAdult();
        }
    }

    public default boolean isBaby() {
        return !getBukkitEntity().isAdult();
    }

    @Override
    public default SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }

    @Override
    public default void initiateEntityPet() {
        EntityInsentientPet.super.initiateEntityPet();
        this.setAgeLocked(true);
    }
}

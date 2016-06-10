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

package com.dsh105.echopet.compat.api.entity;


import org.bukkit.entity.Horse;

public enum HorseType {

    NORMAL(Horse.Variant.HORSE),
    DONKEY(Horse.Variant.DONKEY),
    MULE(Horse.Variant.MULE),
    ZOMBIE(Horse.Variant.UNDEAD_HORSE),
    SKELETON(Horse.Variant.SKELETON_HORSE);

    private Horse.Variant bukkitVariant;

    HorseType(Horse.Variant bukkitVariant) {
        this.bukkitVariant = bukkitVariant;
    }

    /**
     * Get the internal id of the horse type
     *
     * @return the internal id
     * @deprecated use version specific code
     */
    @Deprecated
    public int getId() {
        return getBukkitVariant().ordinal();
    }

    public Horse.Variant getBukkitVariant() {
        return bukkitVariant;
    }


    private static final HorseType[] BY_BUKKIT = new HorseType[Horse.Style.values().length];
    static {
        for (Horse.Variant bukkitVariant : Horse.Variant.values()) {
            HorseType matchingMarking = null;
            for (HorseType marking : HorseType.values()) {
                if (marking.getBukkitVariant().equals(bukkitVariant)) {
                    matchingMarking = marking;
                }
            }
            if (matchingMarking == null) {
                throw new AssertionError("No horse marking for bukkit style: " + bukkitVariant);
            }
            BY_BUKKIT[bukkitVariant.ordinal()] = matchingMarking;
        }
    }

    public static HorseType getForBukkitVariant(Horse.Variant variant) {
        return BY_BUKKIT[variant.ordinal()];
    }
}

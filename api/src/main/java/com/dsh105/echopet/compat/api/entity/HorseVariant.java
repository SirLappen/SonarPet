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

public enum HorseVariant {

    WHITE(Horse.Color.WHITE),
    CREAMY(Horse.Color.CREAMY),
    CHESTNUT(Horse.Color.CHESTNUT),
    BROWN(Horse.Color.BROWN),
    BLACK(Horse.Color.BLACK),
    GRAY(Horse.Color.GRAY),
    DARKBROWN(Horse.Color.DARK_BROWN);

    private Horse.Color bukkitColour;

    HorseVariant(Horse.Color bukkitColour) {
        this.bukkitColour = bukkitColour;
    }

    public Horse.Color getBukkitColour() {
        return bukkitColour;
    }

    public static HorseVariant getForBukkitColour(Horse.Color colour) {
        return BY_BUKKIT[colour.ordinal()];
    }

    private static final HorseVariant[] BY_BUKKIT = new HorseVariant[Horse.Color.values().length];
    static {
        for (Horse.Color bukkitColor : Horse.Color.values()) {
            HorseVariant matchingVariant = null;
            for (HorseVariant variant : HorseVariant.values()) {
                if (variant.getBukkitColour().equals(bukkitColor)) {
                    matchingVariant = variant;
                }
            }
            if (matchingVariant == null) {
                throw new AssertionError("No horse marking for bukkit color: " + bukkitColor);
            }
            BY_BUKKIT[bukkitColor.ordinal()] = matchingVariant;
        }
    }
}

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

public enum HorseMarking {
    NONE(Horse.Style.NONE),
    SOCKS(Horse.Style.WHITE),
    WHITE_PATCH(Horse.Style.WHITEFIELD),
    WHITE_SPOTS(Horse.Style.WHITE_DOTS),
    BLACK_SPOTS(Horse.Style.BLACK_DOTS);

    private final Horse.Style bukkitStyle;

    HorseMarking(Horse.Style bukkitStyle) {
        this.bukkitStyle = bukkitStyle;
    }

    /**
     * Get the internal id
     * @param v the variant associated with the id
     * @return the internal id
     * @deprecated use version specific code
     */
    @Deprecated
    public int getId(HorseVariant v) {
        return (v.getBukkitColour().ordinal() & 255) | (getBukkitStyle().ordinal() << 8);
    }

    public Horse.Style getBukkitStyle() {
        return bukkitStyle;
    }

    private static final HorseMarking[] BY_BUKKIT = new HorseMarking[Horse.Style.values().length];
    static {
        for (Horse.Style bukkitStyle : Horse.Style.values()) {
            HorseMarking matchingMarking = null;
            for (HorseMarking marking : HorseMarking.values()) {
                if (marking.getBukkitStyle().equals(bukkitStyle)) {
                    matchingMarking = marking;
                }
            }
            if (matchingMarking == null) {
                throw new AssertionError("No horse marking for bukkit style: " + bukkitStyle);
            }
            BY_BUKKIT[bukkitStyle.ordinal()] = matchingMarking;
        }
    }

    public static HorseMarking getForBukkitStyle(Horse.Style style) {
        return BY_BUKKIT[style.ordinal()];
    }
}
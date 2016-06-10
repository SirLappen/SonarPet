package net.techcable.sonarpet.item;

import org.bukkit.DyeColor;

public interface ColoredItemData {
    public DyeColor getColor();

    public ItemData withColor(DyeColor color);
}

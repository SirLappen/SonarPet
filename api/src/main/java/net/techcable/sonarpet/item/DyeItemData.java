package net.techcable.sonarpet.item;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class DyeItemData extends ItemData implements ColoredItemData {

    protected DyeItemData(byte rawData, ItemMeta meta) {
        super(Material.INK_SACK, rawData, meta);
    }

    @SuppressWarnings("deprecation")
    public DyeColor getColor() {
        return DyeColor.getByDyeData(getRawData());
    }

    @SuppressWarnings("deprecation")
    public DyeItemData withColor(DyeColor color) {
        return withRawData(Preconditions.checkNotNull(color, "Null color").getDyeData());
    }

    public DyeItemData withRawData(int rawData) {
        return (DyeItemData) super.withRawData(rawData);
    }

    public static DyeItemData create(DyeColor color) {
        return create(color, Bukkit.getItemFactory().getItemMeta(Material.INK_SACK));
    }

    public static DyeItemData create(DyeColor color, ItemMeta meta) {
        return new DyeItemData(Preconditions.checkNotNull(color, "Null color").getDyeData(), meta);
    }
}

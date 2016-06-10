package net.techcable.sonarpet.item;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class StainedClayItemData extends ItemData implements ColoredItemData {

    protected StainedClayItemData(byte rawData, ItemMeta meta) {
        super(Material.STAINED_CLAY, rawData, meta);
    }

    @SuppressWarnings("deprecation")
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getRawData());
    }

    @SuppressWarnings("deprecation")
    public StainedClayItemData withColor(DyeColor color) {
        return withRawData(Preconditions.checkNotNull(color, "Null color").getWoolData());
    }

    public StainedClayItemData withRawData(int rawData) {
        return (StainedClayItemData) super.withRawData(rawData);
    }

    public static StainedClayItemData create(DyeColor color) {
        return create(color, Bukkit.getItemFactory().getItemMeta(Material.STAINED_CLAY));
    }

    public static StainedClayItemData create(DyeColor color, ItemMeta meta) {
        return new StainedClayItemData(Preconditions.checkNotNull(color, "Null color").getWoolData(), meta);
    }
}

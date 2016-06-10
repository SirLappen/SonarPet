package net.techcable.sonarpet.item;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.meta.ItemMeta;

public class WoolItemData extends ItemData implements ColoredItemData {

    protected WoolItemData(byte rawData, ItemMeta meta) {
        super(Material.WOOL, rawData, meta);
    }

    @SuppressWarnings("deprecation")
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getRawData());
    }

    @SuppressWarnings("deprecation")
    public WoolItemData withColor(DyeColor color) {
        return withRawData(Preconditions.checkNotNull(color, "Null color").getWoolData());
    }

    public WoolItemData withRawData(int rawData) {
        return (WoolItemData) super.withRawData(rawData);
    }

    public static WoolItemData create(DyeColor color) {
        return create(color, Bukkit.getItemFactory().getItemMeta(Material.WOOL));
    }

    public static WoolItemData create(DyeColor color, ItemMeta meta) {
        return new WoolItemData(Preconditions.checkNotNull(color, "Null color").getWoolData(), meta);
    }
}

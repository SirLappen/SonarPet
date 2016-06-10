package com.dsh105.echopet.compat.nms.v1_9_R2.item;

import java.util.Optional;

import com.dsh105.echopet.compat.nms.v1_9_R2.NMS;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_9_R2.EntityTypes;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.techcable.sonarpet.item.SpawnEggItemData;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;

public class NMSSpawnEggItemData extends SpawnEggItemData {
    public NMSSpawnEggItemData(byte rawData, ItemMeta meta) {
        this(rawData, meta, SpawnEggItemData.DEFAULT_TYPE);
    }

    public NMSSpawnEggItemData(byte rawData, ItemMeta meta, EntityType type) {
        super(rawData, createMetaWithEntityType(meta, type));
    }
    @Override
    public EntityType getSpawnedType() {
        return getSpawnEggEntityType(this.getMeta());
    }

    public static EntityType getSpawnEggEntityType(ItemMeta meta) {
        Preconditions.checkNotNull(meta, "Null meta");
        NBTTagCompound tag = NMS.getTagFromMeta(Material.MONSTER_EGG, meta);
        Preconditions.checkState(tag != null, "No nbt tag");
        Preconditions.checkState(tag.hasKeyOfType("EntityTag", 10), "No entity tag");
        NBTTagCompound entityTag = tag.getCompound("EntityTag");
        Preconditions.checkState(entityTag.hasKeyOfType("id", 8), "No internal name");
        String internalName = entityTag.getString("id");
        int id = EntityTypes.a(internalName);
        EntityType type = EntityType.fromId(id);
        if (type == null)
            throw new IllegalStateException("No entity found with internal name " + internalName + " and id " + id);
        return type;
    }

    public static Optional<EntityType> getSpawnEggEntityTypeIfPresent(ItemMeta meta) {
        try {
            return Optional.of(getSpawnEggEntityType(meta));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Optional.empty();
        }
    }

    public static ItemMeta createMetaWithEntityType(ItemMeta meta, EntityType entityType) {
        NBTTagCompound entityTag = new NBTTagCompound();
        String internalName = EntityTypes.getName(EntityTypes.a(entityType.getTypeId()));
        if (internalName == null) throw new AssertionError("Couldn't find internal name for type: " + entityType);
        entityTag.setString("id", internalName);
        NBTTagCompound tag = NMS.getTagFromMeta(Material.MONSTER_EGG, Preconditions.checkNotNull(meta, "Null meta"));
        if (tag == null) tag = new NBTTagCompound();
        tag.set("EntityTag", entityTag);
        return NMS.createMetaFromTag(Material.MONSTER_EGG, tag);
    }
}

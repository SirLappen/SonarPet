package com.dsh105.echopet.compat.nms.v1_9_R2.entity;

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySlimePet;
import com.dsh105.echopet.compat.api.util.Perm;

import net.minecraft.server.v1_9_R2.EntitySlime;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftSlime;

public interface EntitySlimePet extends EntityInsentientPet, IEntitySlimePet {

    @Override
    public EntitySlime getEntity();

    @Override
    public CraftSlime getBukkitEntity();

    @Override
    public EntitySlimePetData getNmsData();

    @Override
    public default void initiateEntityPet() {
        EntityInsentientPet.super.initiateEntityPet();
        if (!Perm.hasDataPerm(getPet().getOwner(), false, getPet().getPetType(), PetData.MEDIUM, false)) {
            if (!Perm.hasDataPerm(getPet().getOwner(), false, getPet().getPetType(), PetData.SMALL, false)) {
                this.setSize(4);
            } else {
                this.setSize(1);
            }
        } else {
            this.setSize(2);
        }
    }

    @Override
    public void setSize(int i);

    public default int getSize() {
        return getBukkitEntity().getSize();
    }

    @Override
    public default SizeCategory getSizeCategory() {
        switch (getSize()) {
            case 1:
                return SizeCategory.TINY;
            case 4:
                return SizeCategory.LARGE;
            default:
                return SizeCategory.REGULAR;
        }
    }

}

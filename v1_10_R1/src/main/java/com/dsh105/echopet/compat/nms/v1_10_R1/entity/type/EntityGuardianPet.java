package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import lombok.*;

import java.util.Random;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPetData;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.SoundEffect;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftGuardian;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityGuardian implements EntityInsentientPet, IEntityGuardianPet {

    @Override
    public Sound getIdleSound() {
        // Different ambient sounds for if we are are land or are an elder guardian
        if (isElder()) {
            return isInWater() ? Sound.ENTITY_ELDER_GUARDIAN_AMBIENT : Sound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND;
        } else {
            return isInWater() ? Sound.ENTITY_GUARDIAN_AMBIENT : Sound.ENTITY_GUARDIAN_AMBIENT_LAND;
        }
    }

    @Override
    public Sound getDeathSound() {
        // Different death sounds for if we are are land or are an elder guardian
        if (isElder()) {
            return isInWater() ? Sound.ENTITY_ELDER_GUARDIAN_DEATH : Sound.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
        } else {
            return isInWater() ? Sound.ENTITY_GUARDIAN_DEATH : Sound.ENTITY_GUARDIAN_DEATH_LAND;
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isElder() ? SizeCategory.GIANT : SizeCategory.LARGE;
    }

    @Override
    public boolean isElder() {
        return getBukkitEntity().isElder();
    }

    @Override
    public void setElder(boolean flag) {
        getBukkitEntity().setElder(flag);
    }

    // EntityInsentientPet Implementations

    @Override
    public EntityGuardian getEntity() {
        return this;
    }

    @Getter
    private IPet pet;
    @Getter
    private final EntityInsentientPetData nmsData = new EntityInsentientPetData(this);

    @Override
    public void m() {
        super.m();
        onLive();
    }

    public void g(float sideMot, float forwMot) {
        move(sideMot, forwMot, super::g);
    }

    public EntityGuardianPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftGuardian getBukkitEntity() {
        return (CraftGuardian) super.getBukkitEntity();
    }

    // Access helpers

    @Override
    public Random random() {
        return this.random;
    }

    @Override
    public SoundEffect bV() {
        return EntityInsentientPet.super.bV();
    }

    @Override
    public void a(BlockPosition blockposition, Block block) {
        super.a(blockposition, block);
        onStep(blockposition, block);
    }

    @Override
    public SoundEffect G() {
        return EntityInsentientPet.super.G();
    }

    @Override
    public void setYawPitch(float f, float f1) {
        super.setYawPitch(f, f1);
    }
}

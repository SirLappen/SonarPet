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

package com.dsh105.echopet.compat.nms.v1_9_R2.entity.type;

import lombok.*;

import java.util.Random;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import com.dsh105.echopet.compat.nms.v1_9_R2.entity.EntityInsentientPet;
import com.dsh105.echopet.compat.nms.v1_9_R2.entity.EntityInsentientPetData;
import com.dsh105.echopet.compat.nms.v1_9_R2.metadata.MetadataKey;
import com.dsh105.echopet.compat.nms.v1_9_R2.metadata.MetadataType;

import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.EntityEnderman;
import net.minecraft.server.v1_9_R2.SoundEffect;
import net.minecraft.server.v1_9_R2.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEnderman;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityEnderman implements EntityInsentientPet, IEntityEndermanPet {

    public static final MetadataKey<Boolean> ENDERMAN_IS_SCREAMING_METADATA = new MetadataKey<>(12, MetadataType.BOOLEAN);

    @Override
    public void setScreaming(boolean flag) {
        getDatawatcher().set(ENDERMAN_IS_SCREAMING_METADATA, flag);
    }

    public boolean isScreaming() {
        return getDatawatcher().get(ENDERMAN_IS_SCREAMING_METADATA);
    }

    @Override
    public Sound getIdleSound() {
        return this.isScreaming() ? Sound.ENTITY_ENDERMEN_SCREAM : Sound.ENTITY_ENDERMEN_AMBIENT;
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_ENDERMEN_DEATH;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    // EntityInsentientPet Implementations

    @Override
    public EntityEnderman getEntity() {
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

    public EntityEndermanPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftEnderman getBukkitEntity() {
        return (CraftEnderman) super.getBukkitEntity();
    }

    // Access helpers

    @Override
    public Random random() {
        return this.random;
    }

    @Override
    public SoundEffect bS() {
        return EntityInsentientPet.super.bS();
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

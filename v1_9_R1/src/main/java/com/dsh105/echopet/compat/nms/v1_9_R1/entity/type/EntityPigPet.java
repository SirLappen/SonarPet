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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import lombok.*;

import java.util.Random;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPigPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.NMS;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityAgeablePetData;
import com.dsh105.echopet.compat.nms.v1_9_R1.metadata.MetadataKey;
import com.dsh105.echopet.compat.nms.v1_9_R1.metadata.MetadataType;

import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.EntityMushroomCow;
import net.minecraft.server.v1_9_R1.EntityPig;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPig;

@EntitySize(width = 0.9F, height = 0.9F)
@EntityPetType(petType = PetType.PIG)
public class EntityPigPet extends EntityPig implements EntityAgeablePet, IEntityPigPet {

    public boolean hasSaddle() {
        return getBukkitEntity().hasSaddle();
    }

    @Override
    public void setSaddled(boolean flag) {
        getBukkitEntity().setSaddle(flag);
    }

    @Override
    public void makeStepSound() {
        this.playSound(Sound.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public Sound getIdleSound() {
        return Sound.ENTITY_PIG_AMBIENT;
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_PIG_DEATH;
    }

    // EntityAgeablePet Implementations

    @Override
    public EntityPig getEntity() {
        return this;
    }

    @Getter
    private IPet pet;
    @Getter
    private final EntityAgeablePetData nmsData = new EntityAgeablePetData(this);

    @Override
    public void m() {
        super.m();
        onLive();
    }

    public void g(float sideMot, float forwMot) {
        move(sideMot, forwMot, super::g);
    }

    public EntityPigPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftPig getBukkitEntity() {
        return (CraftPig) super.getBukkitEntity();
    }

    // Access helpers

    @Override
    public Random random() {
        return this.random;
    }

    @Override
    public SoundEffect bS() {
        return EntityAgeablePet.super.bS();
    }

    @Override
    public void a(BlockPosition blockposition, Block block) {
        super.a(blockposition, block);
        onStep(blockposition, block);
    }

    @Override
    public SoundEffect G() {
        return EntityAgeablePet.super.G();
    }

    @Override
    public void setYawPitch(float f, float f1) {
        super.setYawPitch(f, f1);
    }
}

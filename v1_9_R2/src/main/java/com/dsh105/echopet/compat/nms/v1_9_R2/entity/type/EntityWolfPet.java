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
import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.nms.v1_9_R2.NMS;
import com.dsh105.echopet.compat.nms.v1_9_R2.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_9_R2.entity.EntityAgeablePetData;
import com.dsh105.echopet.compat.nms.v1_9_R2.metadata.MetadataKey;
import com.dsh105.echopet.compat.nms.v1_9_R2.metadata.MetadataType;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.EntityVillager;
import net.minecraft.server.v1_9_R2.EntityWolf;
import net.minecraft.server.v1_9_R2.EnumParticle;
import net.minecraft.server.v1_9_R2.MathHelper;
import net.minecraft.server.v1_9_R2.SoundEffect;
import net.minecraft.server.v1_9_R2.World;

import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftWolf;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityWolf implements EntityAgeablePet, IEntityWolfPet {

    private boolean wet;
    private boolean shaking;
    private float shakeCount;

    @Override
    public void initiateEntityPet() {
        setTamed(true); // Tame
    }

    public boolean isTamed() {
        return getBukkitEntity().isTamed();
    }

    @Override
    public void setTamed(boolean flag) {
        super.setTamed(flag);

        if (!flag) {
            getPet().getPetData().remove(PetData.TAMED);
        } else if (!getPet().getPetData().contains(PetData.TAMED)) {
            this.getPet().getPetData().add(PetData.TAMED);
        }

        if (isAngry() && flag) {
            setAngry(false);
        }
    }

    @Override
    public void setAngry(boolean flag) {
        if (flag) {
            if (!getPet().getPetData().contains(PetData.ANGRY)) {
                this.getPet().getPetData().add(PetData.ANGRY);
            }
            if (isTamed()) {
                setTamed(false);
            }
        } else {
            getPet().getPetData().remove(PetData.ANGRY);
        }

        super.setAngry(flag);
    }

    public boolean isAngry() {
        return super.isAngry();
    }

    @Override
    public void setCollarColor(DyeColor dc) {
        getBukkitEntity().setCollarColor(dc);
    }

    @Override
    public void onLive() {
        EntityAgeablePet.super.onLive();
        if (this.inWater) {
            this.wet = true;
            this.shaking = false;
            this.shakeCount = 0.0F;
        } else if ((this.wet || this.shaking) && this.shaking) {
            if (this.shakeCount == 0.0F) {
                this.playSound(Sound.ENTITY_WOLF_SHAKE, NMS.getVolume(this), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.shakeCount += 0.05F;
            if (this.shakeCount - 0.05F >= 2.0F) {
                this.wet = false;
                this.shaking = false;
                this.shakeCount = 0.0F;
            }

            if (this.shakeCount > 0.4F) {
                float f = (float) this.getBoundingBox().b;
                int i = (int) (MathHelper.sin((this.shakeCount - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    @Override
    public Sound getIdleSound() {
        if (this.isAngry()) {
            return Sound.ENTITY_WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {
            if (this.isTamed() && getHealth() < 10) {
                return Sound.ENTITY_WOLF_WHINE;
            } else {
                return Sound.ENTITY_WOLF_PANT;
            }
        } else {
            return Sound.ENTITY_WOLF_AMBIENT;
        }
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_WOLF_DEATH;
    }

    // EntityAgeablePet Implementations

    @Override
    public EntityWolf getEntity() {
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

    public EntityWolfPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftWolf getBukkitEntity() {
        return (CraftWolf) super.getBukkitEntity();
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

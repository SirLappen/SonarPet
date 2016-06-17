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

package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import lombok.*;

import java.util.Random;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPetData;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityWither;
import net.minecraft.server.v1_10_R1.SoundEffect;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftWither;

@EntitySize(width = 0.9F, height = 4.0F)
@EntityPetType(petType = PetType.WITHER)
public class EntityWitherPet extends EntityWither implements EntityInsentientPet, IEntityWitherPet {

    public void setShielded(boolean flag) {
        this.setInvulnerable(flag);
        this.setHealth((float) (flag ? 150 : 300));
    }

    @Override
    public Sound getIdleSound() {
        return Sound.ENTITY_WITHER_AMBIENT;
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_WITHER_DEATH;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }


    // EntityInsentientPet Implementations

    @Override
    public EntityWitherPet getEntity() {
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

    public EntityWitherPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftWither getBukkitEntity() {
        return (CraftWither) super.getBukkitEntity();
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

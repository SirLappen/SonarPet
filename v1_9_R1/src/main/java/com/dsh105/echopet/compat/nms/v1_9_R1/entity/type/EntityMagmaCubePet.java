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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityMagmaCubePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntitySlimePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntitySlimePetData;

import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.EntityMagmaCube;
import net.minecraft.server.v1_9_R1.EntitySlime;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftSlime;

@EntitySize(width = 0.6F, height = 0.6F)
@EntityPetType(petType = PetType.MAGMACUBE)
public class EntityMagmaCubePet extends EntityMagmaCube implements EntitySlimePet, IEntityMagmaCubePet {

    @Override
    public Sound getIdleSound() {
        return null;
    }

    @Override
    public Sound getDeathSound() {
        return this.getSize() > 1 ? Sound.ENTITY_MAGMACUBE_DEATH : Sound.ENTITY_SMALL_MAGMACUBE_DEATH;
    }

    // EntitySlimePet Implementations

    @Override
    public EntityMagmaCube getEntity() {
        return this;
    }

    @Getter
    private IPet pet;
    @Getter
    private final EntitySlimePetData nmsData = new EntitySlimePetData(this);

    @Override
    public void m() {
        super.m();
        onLive();
    }

    public void g(float sideMot, float forwMot) {
        move(sideMot, forwMot, super::g);
    }

    public EntityMagmaCubePet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftSlime getBukkitEntity() {
        return (CraftSlime) super.getBukkitEntity();
    }

    // Access helpers

    @Override
    public Random random() {
        return this.random;
    }

    @Override
    public SoundEffect bS() {
        return EntitySlimePet.super.bS();
    }

    @Override
    public void a(BlockPosition blockposition, Block block) {
        super.a(blockposition, block);
        onStep(blockposition, block);
    }

    @Override
    public SoundEffect G() {
        return EntitySlimePet.super.G();
    }

    @Override
    public void setYawPitch(float f, float f1) {
        super.setYawPitch(f, f1);
    }

    @Override
    public void setSize(int i) {
        super.setSize(i);
    }
}

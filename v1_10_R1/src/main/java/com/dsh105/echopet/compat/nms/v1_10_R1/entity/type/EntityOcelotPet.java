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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityOcelotPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityAgeablePetData;
import com.google.common.base.Optional;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityOcelot;
import net.minecraft.server.v1_10_R1.SoundEffect;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftOcelot;
import org.bukkit.entity.Ocelot;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.OCELOT)
public class EntityOcelotPet extends EntityOcelot implements EntityAgeablePet, IEntityOcelotPet {

    @Override
    public void initiateEntityPet() {
        setCatType(Ocelot.Type.BLACK_CAT.getId());
        setOwnerUUID(pet.getOwnerUUID());
    }

    @Override
    public Sound getIdleSound() {
        return (this.random().nextInt(4) == 0 ? Sound.ENTITY_CAT_PURREOW : Sound.ENTITY_CAT_AMBIENT); // Play puring sounds instead of default mojang sounds
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_CAT_DEATH;
    }

    // EntityAgeablePet Implementations

    @Override
    public EntityOcelot getEntity() {
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

    public EntityOcelotPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftOcelot getBukkitEntity() {
        return (CraftOcelot) super.getBukkitEntity();
    }

    // Access helpers

    @Override
    public Random random() {
        return this.random;
    }

    @Override
    public SoundEffect bV() {
        return EntityAgeablePet.super.bV();
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

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
import java.util.UUID;
import java.util.function.BiConsumer;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.HorseArmour;
import com.dsh105.echopet.compat.api.entity.HorseMarking;
import com.dsh105.echopet.compat.api.entity.HorseType;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import com.dsh105.echopet.compat.nms.v1_10_R1.NMS;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityAgeablePetData;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.metadata.MetadataKey;
import com.dsh105.echopet.compat.nms.v1_10_R1.metadata.MetadataType;
import com.google.common.base.Optional;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityChicken;
import net.minecraft.server.v1_10_R1.EntityHorse;
import net.minecraft.server.v1_10_R1.EnumHorseType;
import net.minecraft.server.v1_10_R1.SoundEffect;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftHorse;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.HORSE)
public class EntityHorsePet extends EntityHorse implements EntityAgeablePet, IEntityHorsePet {

    private int rearingCounter = 0;
    int stepSoundCount = 0;

    public EntityHorsePet(World world) {
        super(world);
    }

    @Override
    public void initiateEntityPet() {
        getBukkitEntity().setOwnerUUID(pet.getOwnerUUID());
    }

    @Override
    public void setSaddled(boolean flag) {
        getBukkitEntity().getInventory().setSaddle(flag ? new ItemStack(Material.SADDLE, 1) : null);
    }

    @Override
    public void setHorseType(HorseType t) {
        if (t != HorseType.NORMAL) {
            this.setArmour(HorseArmour.NONE);
        }
        getBukkitEntity().setVariant(t.getBukkitVariant());
    }

    @Override
    public void setVariant(HorseVariant v, HorseMarking m) {
        getBukkitEntity().setColor(v.getBukkitColour());
        getBukkitEntity().setStyle(m.getBukkitStyle());
    }

    @Override
    public void setArmour(HorseArmour a) {
        getBukkitEntity().getInventory().setArmor(a.getMaterial() == null ? null : new ItemStack(a.getMaterial(), 1));
    }

    @Override
    public void setChested(boolean flag) {
        getBukkitEntity().setCarryingChest(flag);
    }

    private void setRearing(boolean b) {
        this.v(b); // setRearing (flag with id 64)
    }

    @Override
    public boolean attack(Entity entity) {
        boolean flag = EntityAgeablePet.super.attack(entity);
        if (flag) {
            this.setRearing(true);
            switch (getType()) {
                case DONKEY:
                    playSound(Sound.ENTITY_DONKEY_ANGRY, 1, 1);
                    break;
                default:
                    playSound(Sound.ENTITY_HORSE_ANGRY, 1, 1);
                    break;
            }
        }
        return flag;
    }

    @Override
    public Sound getIdleSound() {
        switch (this.getType()) {
            case DONKEY:
                return Sound.ENTITY_DONKEY_AMBIENT;
            case HORSE:
                return Sound.ENTITY_HORSE_AMBIENT;
            case MULE:
                return Sound.ENTITY_MULE_AMBIENT;
            case SKELETON:
                return Sound.ENTITY_SKELETON_AMBIENT;
            case ZOMBIE:
                return Sound.ENTITY_SKELETON_AMBIENT;
            default:
                throw new AssertionError("No idle sound for type: " + getType());
        }
    }

    @Override
    public void makeStepSound(int i, int j, int k, Block block) {
        NMS.SoundData soundData = NMS.SoundData.getFromBlock(block);

        if (this.world.getType(new BlockPosition(i, j + 1, k)) == Blocks.SNOW) {
            soundData = NMS.SoundData.getFromBlock(Blocks.SNOW);
        }

        if (!block.getBlockData().getMaterial().isLiquid()) {

            if (!this.passengers.isEmpty() && getType() != EnumHorseType.HORSE && getType() != EnumHorseType.MULE) {
                ++this.stepSoundCount;
                if (this.stepSoundCount > 5 && this.stepSoundCount % 3 == 0) {
                    this.playSound(Sound.ENTITY_HORSE_GALLOP, soundData.getVolume() * 0.15F, soundData.getPitch());
                    if (getType() == EnumHorseType.HORSE && this.random.nextInt(10) == 0) {
                        this.playSound(Sound.ENTITY_HORSE_BREATHE, soundData.getVolume() * 0.6F, soundData.getPitch());
                    }
                } else if (this.stepSoundCount <= 5) {
                    this.playSound(Sound.ENTITY_HORSE_STEP_WOOD, soundData.getVolume() * 0.15F, soundData.getPitch());
                }
            } else if (soundData.equals(NMS.SoundData.WOOD)) {
                this.playSound(Sound.ENTITY_HORSE_STEP_WOOD, soundData.getVolume() * 0.15F, soundData.getPitch());
            } else {
                this.playSound(Sound.ENTITY_HORSE_STEP, soundData.getVolume() * 0.15F, soundData.getPitch());
            }
        }
    }

    @Override
    public void move(float sideMot, float forwMot, BiConsumer<Float, Float> superMoveFunction) {
        EntityAgeablePet.super.move(sideMot, forwMot, superMoveFunction);
        if (forwMot <= 0.0F) {
            this.stepSoundCount = 0;
        }
    }

    @Override
    public Sound getDeathSound() {
        switch (getType()) {
            case DONKEY:
                return Sound.ENTITY_DONKEY_DEATH;
            case HORSE:
                return Sound.ENTITY_HORSE_DEATH;
            case MULE:
                return Sound.ENTITY_MULE_DEATH;
            case SKELETON:
                return Sound.ENTITY_SKELETON_HORSE_DEATH;
            case ZOMBIE:
                return Sound.ENTITY_ZOMBIE_HORSE_DEATH;
            default:
                throw new AssertionError("No death sound for type: " + getType());
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.LARGE;
        }
    }

    @Override
    public void onLive() {
        EntityAgeablePet.super.onLive();
        if (rearingCounter > 0 && ++rearingCounter > 20) {
            setRearing(false);
        }
    }

    @Override
    public void doJumpAnimation() {
        this.playSound(Sound.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
        this.rearingCounter = 1;
        setRearing(true);
    }


    // EntityAgeablePet Implementations

    @Override
    public EntityHorse getEntity() {
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

    public EntityHorsePet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    @Override
    public CraftHorse getBukkitEntity() {
        return (CraftHorse) super.getBukkitEntity();
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

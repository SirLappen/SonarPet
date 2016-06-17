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

package com.dsh105.echopet.compat.nms.v1_10_R1.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalLookAtPlayer;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityInsentientPet;

import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityHuman;

public class PetGoalLookAtPlayer extends APetGoalLookAtPlayer {

    private EntityInsentientPet pet;
    protected Entity player;
    private float range;
    private int ticksLeft;
    private float chance;
    private Class clazz;

    public PetGoalLookAtPlayer(EntityInsentientPet pet, Class c) {
        this.pet = pet;
        this.range = 8.0F;
        this.chance = 0.02F;
        this.clazz = c;
    }

    public PetGoalLookAtPlayer(EntityInsentientPet pet, Class c, float f, float f1) {
        this.pet = pet;
        this.range = f;
        this.chance = f1;
        this.clazz = c;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.TWO;
    }

    @Override
    public String getDefaultKey() {
        return "LookAtPlayer";
    }

    @Override
    public boolean shouldStart() {
        if (this.pet.random().nextFloat() >= this.chance) {
            return false;
        } else if (!this.pet.getEntity().passengers.isEmpty()) {
            return false;
        } else {
            if (this.clazz == EntityHuman.class) {
                this.player = this.pet.getEntity().world.findNearbyPlayer(this.pet.getEntity(), (double) this.range);
            } else {
                this.player = this.pet.getEntity().world.a(this.clazz, this.pet.getEntity().getBoundingBox().grow((double) this.range, 3.0D, (double) this.range), this.pet.getEntity());
            }
            return this.player != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.player.isAlive()
                && (this.pet.getEntity().h(this.player) <= (double) (this.range * this.range) // pet.distanceSquared(player) < range squared
                && this.ticksLeft > 0);
    }

    @Override
    public void start() {
        this.ticksLeft = 40 + this.pet.random().nextInt(40);
    }

    @Override
    public void finish() {
        this.player = null;
    }

    @Override
    public void tick() {
        /*
         * Make the pet look at the owner.
         * Changing up to 10 yaw in the pet's facing (max) per tick.
         * The speed to change the pitch at is requested from the entity.
         * Also done in PetGoalFollowOwner.tick()
         */
        this.pet.getEntity().getControllerLook().a(this.player, 10.0F, (float) this.pet.getEntity().N());
        --this.ticksLeft;
    }
}

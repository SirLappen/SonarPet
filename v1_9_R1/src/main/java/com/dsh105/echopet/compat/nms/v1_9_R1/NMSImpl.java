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

package com.dsh105.echopet.compat.nms.v1_9_R1;

import java.util.Objects;
import java.util.Optional;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.INMS;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityInsentientPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.item.NMSSpawnEggItemData;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutMount;
import net.minecraft.server.v1_9_R1.World;
import net.techcable.sonarpet.item.SpawnEggItemData;
import net.techcable.sonarpet.particles.Particle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.SpawnEgg;

public class NMSImpl implements INMS {

    @Override
    public SpawnEggItemData createSpawnEggData(byte rawData, ItemMeta meta) {
        EntityType entityType = NMSSpawnEggItemData.getSpawnEggEntityTypeIfPresent(meta).orElse(EntityType.fromId(rawData));
        if (entityType == null) entityType = SpawnEggItemData.DEFAULT_TYPE; // 'Fix' broken configs
        return new NMSSpawnEggItemData(rawData, meta, entityType);
    }

    @Override
    public SpawnEggItemData createSpawnEggData(EntityType entityType, ItemMeta meta) {
        Preconditions.checkNotNull(entityType, "Null entity type");
        Preconditions.checkNotNull(meta, "Null item meta");
        return new NMSSpawnEggItemData(new SpawnEgg(entityType).getData(), meta, entityType); // Pretend we use metadata to make the code happy
    }

    @Override
    public void mount(org.bukkit.entity.Entity bukkitRider, org.bukkit.entity.Entity bukkitVehicle) {
        Entity rider = NMS.getHandle(Objects.requireNonNull(bukkitRider, "Null rider"));
        if (bukkitVehicle == null) {
            Entity vehicle = rider.getVehicle(); // This is how you *really* get the vehicle :/
            if (rider instanceof EntityInsentientPet) {
                ((EntityInsentientPet) rider).reallyStopRiding(); // normally we block dismounting, but now we really do want to dismount
            } else {
                rider.stopRiding();
            }
            if (vehicle != null) {
                Packet packet = new PacketPlayOutMount(vehicle);
                for (EntityHuman human : rider.world.players) {
                    ((EntityPlayer) human).playerConnection.sendPacket(packet);
                }
            }
        } else {
            Preconditions.checkArgument(bukkitRider.getWorld().equals(bukkitVehicle.getWorld()), "Rider is in world %s, while vehicle is in world %s", bukkitRider.getWorld().getName(), bukkitVehicle.getWorld().getName());
            Entity vehicle = NMS.getHandle(bukkitVehicle);
            NMS.startRiding(rider, vehicle, true);
            Packet packet = new PacketPlayOutMount(vehicle);
            for (EntityHuman human : rider.world.players) {
                ((EntityPlayer) human).playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public EntityInsentientPet spawn(IPet pet, Player owner) {
        Location l = owner.getLocation();
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, l);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Pet spawn was cancelled externally.");
            EchoPet.getManager().removePet(pet, true);
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityInsentientPet entityPet = (EntityInsentientPet) pet.getPetType().getNewEntityPetInstance(mcWorld, pet);

        entityPet.getEntity().spawnIn(mcWorld);
        //noinspection RedundantCast - the version of craftbukkit we compile against doesn't have this method, but the version of bukkit does
        ((Creature) entityPet.getBukkitEntity()).setCollidable(false);
        entityPet.setLocation(new Location(mcWorld.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet.getEntity(), CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Failed to spawn pet entity.");
            EchoPet.getManager().removePet(pet, true);
        } else {
            Particle.MAGIC_RUNES.show(l);
        }
        return entityPet;
    }
}

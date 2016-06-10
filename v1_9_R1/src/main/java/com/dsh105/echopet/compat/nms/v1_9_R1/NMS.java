package com.dsh105.echopet.compat.nms.v1_9_R1;

import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.dsh105.echopet.compat.api.entity.HorseArmour;
import com.dsh105.echopet.compat.api.entity.HorseMarking;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.DataWatcherSerializer;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EntityTypes;
import net.minecraft.server.v1_9_R1.EnumHorseArmor;
import net.minecraft.server.v1_9_R1.Item;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.SoundEffectType;
import net.techcable.sonarpet.utils.reflection.Reflection;
import net.techcable.sonarpet.utils.reflection.SonarMethod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.CraftSound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class NMS {

    public static NBTTagCompound getTagFromMeta(Material type, ItemMeta meta) {
        Preconditions.checkNotNull(meta, "Null meta");
        Preconditions.checkNotNull(type, "Null type");
        Preconditions.checkArgument(Bukkit.getItemFactory().isApplicable(meta, type), "Meta %s isn't applicable to %s", meta, type);
        Item item = CraftMagicNumbers.getItem(type);
        ItemStack stack = new ItemStack(item);
        boolean worked = CraftItemStack.setItemMeta(stack, meta);
        if (!worked) throw new RuntimeException("Didn't work");
        return stack.getTag();
    }

    public static ItemMeta createMetaFromTag(Material type, NBTTagCompound tag) {
        Item item = CraftMagicNumbers.getItem(Preconditions.checkNotNull(type, "Null type"));
        ItemStack stack = new ItemStack(item);
        stack.setTag(Preconditions.checkNotNull(tag, "Null nbt tag"));
        return CraftItemStack.getItemMeta(stack);
    }

    //
    // Version Specific Code
    //

    public static Horse.Variant variantById(int id) {
        return Horse.Variant.values()[id];
    }

    public static int getId(Horse.Variant bukkitVariant) {
        return bukkitVariant.ordinal();
    }

    public static int getId(HorseArmour dsh) {
        return EnumHorseArmor.valueOf(dsh.name()).ordinal();
    }

    public static int getId(HorseVariant dshVariant, HorseMarking dshMarking) {
        return getId(dshVariant.getBukkitColour(), dshMarking.getBukkitStyle());
    }

    public static int getId(Horse.Color color, Horse.Style style) {
        return (color.ordinal() & 255) | (style.ordinal() << 8);
    }

    // Obfuscation helpers

    public static void jump(EntityInsentient e) {
        e.getControllerJump().a();
    }


    public static void playSound(Entity entity, SoundEffect nmsSound, float volume, float pitch) {
        entity.a(nmsSound, volume, pitch);
    }


    private static final MethodHandle getSoundEffectType;
    static {
        try {
            Field f = Block.class.getDeclaredField("stepSound");
            if (f.getType() != SoundEffectType.class) {
                throw new AssertionError("stepSound of unexpected type: " + f.getType());
            }
            f.setAccessible(true);
            getSoundEffectType = MethodHandles.lookup().unreflectGetter(f);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Unable to access sound effect type", e);
        }
    }

    public static final MethodHandle getVolumeMethod;

    static {
        try {
            Method m = EntityLiving.class.getDeclaredMethod("cd");
            if (m.getReturnType() != float.class) {
                throw new AssertionError("getVolume method " + m.getName() + " has unexpected return type: " + m.getReturnType());
            }
            m.setAccessible(true);
            getVolumeMethod = MethodHandles.lookup().unreflect(m);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError("Unable to access/find get volume method", e);
        }
    }

    @SneakyThrows // Should not throw exception
    public static float getVolume(EntityLiving e) {
        return (float) getVolumeMethod.invokeExact(e);
    }

    @SneakyThrows // No exception is ever thrown
    public static SoundEffectType getSoundEffectType(Block block) {
        return (SoundEffectType) getSoundEffectType.invokeExact(block);
    }

    /**
     * The sound data for a block
     */
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class SoundData {
        public static final SoundData WOOD = new SoundData(SoundEffectType.a);

        @Getter
        @NonNull
        private final SoundEffectType handle;

        public float getVolume() {
            return getHandle().a();
        }

        public float getPitch() {
            return getHandle().b();
        }

        public SoundEffect getStepSound() {
            return getHandle().d();
        }

        public static SoundData getFromBlock(Block block) {
            return new SoundData(getSoundEffectType(block));
        }
    }


    public static boolean startRiding(Entity rider, Entity vehicle, boolean force) {
        return rider.a(vehicle, force);
    }

    /**
     * Get the entities sideways movement.
     * <p>Positive numbers move to the left and negative to the right.</p>
     *
     * @param entity the entity to get the movement of
     * @return the sideways movement
     */
    public static float getSidewaysMotion(EntityLiving entity) {
        return entity.bd;
    }

    /**
     * Get the entities forward movement.
     * <p>Negative numbers will move backwards.</p>
     *
     * @param entity the entity to get the movement of
     * @return the forward movement
     */
    public static float getForwardsMotion(EntityLiving entity) {
        return entity.be;
    }

    //
    // Utilities for bukkit objects
    //

    // convertors

    public static final ImmutableMap<SoundEffect, Sound> BUKKIT_SOUNDS;
    static {
        ImmutableMap.Builder<SoundEffect, Sound> soundsBuilder = ImmutableMap.builder();
        for (Sound bukkitSound : Sound.values()) {
            SoundEffect mojangSound = fromBukkit(bukkitSound);
            soundsBuilder.put(mojangSound, bukkitSound);
        }
        BUKKIT_SOUNDS = soundsBuilder.build();
    }
    public static Sound toBukkit(SoundEffect mojangEffect) {
        Sound bukkitEffect = BUKKIT_SOUNDS.get(mojangEffect);
        if (bukkitEffect == null) throw new AssertionError("No bukkit sound effect for mojang effect: " + mojangEffect);
        return bukkitEffect;
    }

    public static SoundEffect fromBukkit(Sound bukkit) {
        return bukkit == null ? null : CraftSound.getSoundEffect(CraftSound.getSound(bukkit));
    }

    // unwrappers

    public static Entity getHandle(org.bukkit.entity.Entity e) {
        Preconditions.checkNotNull(e, "Null entity");
        if (e instanceof CraftEntity) {
            return ((CraftEntity) e).getHandle();
        } else {
            throw new IllegalArgumentException("Players must be Craft to get handle");
        }
    }

    public static EntityPlayer getHandle(Player p) {
        return (EntityPlayer) getHandle((org.bukkit.entity.Entity) p);
    }

}

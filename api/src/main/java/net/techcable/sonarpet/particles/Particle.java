package net.techcable.sonarpet.particles;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import net.techcable.sonarpet.utils.Versioning;
import net.techcable.sonarpet.utils.reflection.Reflection;
import net.techcable.sonarpet.utils.reflection.SonarField;

import org.bukkit.Location;

import static net.techcable.sonarpet.utils.reflection.Reflection.*;
import static net.techcable.sonarpet.utils.reflection.SonarField.findFieldWithType;

public enum Particle {
    DEATH_CLOUD(0, "explode", 0.1F, 10),
    EXPLOSION(1, "largeexplode", 0.1F, 1),
    HUGE_EXPLOSION(2, "hugeexplosion", 0.1F, 1),
    FIREWORKS_SPARK(3, "fireworksSpark", 0.1F, 50),
    BUBBLE(4, "bubble", 0.4F, 50),
    SPLASH(5, "splash", 1.0F, 40),
    WAKE(6, "wake", 0.1F, 50),
    SUSPENDED(7, "suspended", 0.0F, 50),
    DEPTH_SUSPEND(8, "depthsuspend", 0.0F, 100),
    CRITICAL(9, "crit", 0.1F, 100),
    MAGIC_CRITICAL(10, "magicCrit", 0.1F, 100),
    SMALL_SMOKE(11, "smoke", 0.05F, 100),
    SMOKE(12, "largesmoke", 0.2F, 20),
    SPELL(13, "spell", 0.05F, 50),
    INSTANT_SPELL(14, "instantSpell", 0.05F, 50),
    SPELL_MOB(15, "mobSpell", 1.0F, 100),
    SPELL_MOB_AMBIENT(16, "mobSpellAmbient", 1.0F, 100),
    WITCH_MAGIC(17, "witchMagic", 1.0F, 20),
    WATER_DRIP(18, "dripWater", 0.0F, 100),
    LAVA_DRIP(19, "dripLava", 0.0F, 100),
    ANGRY_VILLAGER(20, "angryVillager", 0.0F, 20),
    SPARKLE(21, "happyVillager", 0.0F, 20),
    VOID(22, "townaura", 1.0F, 100),
    NOTE(23, "note", 1.0F, 1),
    PORTAL(24, "portal", 1.0F, 100),
    MAGIC_RUNES(25, "enchantmenttable", 1.0F, 100),
    FIRE(26, "flame", 0.05F, 100),
    LAVA_SPARK(27, "lava", 0.0F, 4),
    FOOTSTEP(28, "footstep", 0.0F, 10),
    CLOUD(29, "cloud", 0.1F, 50),
    RED_SMOKE(30, "reddust", 0.0F, 40),
    RAINBOW_SMOKE(30, "reddust", 1.0F, 100),
    SNOWBALL(32, "snowballpoof", 1.0F, 20),
    SNOW_SHOVEL(32, "snowshovel", 0.02F, 30),
    SLIME_SPLAT(33, "slime", 1.0F, 30),
    HEART(34, "heart", 0.0F, 4),
    BARRIER(35, "barrier", 0.0F, 1),
    WATER_DROPLET(39, "droplet", 0.05F, 10),
    ITEM_TAKE(40, "take", 0.0F, 1),
    GUARDIAN_APPEARANCE(41, "mobappearance", 0.0F, 1),
    ICON_BREAK(36, "iconcrack", 0.1F, 100),
    BLOCK_BREAK(37, "blockcrack", 0.1F, 100),
    BLOCK_DUST(38, "blockdust", 0.1F, 100);

    private final int id;
    private final String name;
    private final float speed;
    private final int amount;
    private final org.bukkit.Particle bukkitParticle;

    private Particle(int id, String name, float speed, int amount) {
        Preconditions.checkNotNull(name, "Null name");
        this.id = id;
        this.name = name;
        Preconditions.checkArgument(isValidInternalName(name), "Invalid internal name: %s", name);
        if (Reflection.getClass("org.bukkit.Particle") != null) {
            this.bukkitParticle = getBukkitParticle(name);
        } else {
            this.bukkitParticle = null;
        }
        this.speed = speed;
        this.amount = amount;
    }

    public ParticleBuilder builder() {
        return ParticleBuilder.create(this, speed, amount);
    }

    public void show(Location l) {
        this.builder().at(l).show();
    }

    public int getId() {
        return this.id;
    }

    public String getInternalName() {
        return this.name;
    }

    public float getSpeed() {
        return this.speed;
    }


    public int getAmount() {
        return this.amount;
    }

    public org.bukkit.Particle getBukkitParticle() {
        assert bukkitParticle != null;
        return bukkitParticle;
    }

    // Dark magic. Do not touch
    private static Object getInternalObject(String internalName) {
        Preconditions.checkArgument(!internalName.endsWith("_"), "%s ends with an underscore.", internalName);
        // Normally i'd make these constants, but we can't as its called before fields are initalized
        Class<? extends Enum> enumParticleClass = getNmsClass("EnumParticle", Enum.class);
        SonarField<String> enumParticleInternalNameField = findFieldWithType(enumParticleClass, String.class);
        for (Enum particle : enumParticleClass.getEnumConstants()) {
            String particleInternalName = enumParticleInternalNameField.getValue(particle);
            // 1.8 has some crazy stuff
            if (particleInternalName.endsWith("_")) {
                particleInternalName = particleInternalName.substring(0, particleInternalName.length() - 1);
            }
            if (particleInternalName.equals(internalName)) {
                return particle;
            }
        }
        return null;
    }

    private static boolean isValidInternalName(String internalName) {
        return getInternalObject(internalName) != null;
    }

    private static org.bukkit.Particle getBukkitParticle(String internalName) {
        Enum e = (Enum) getInternalObject(internalName);
        Preconditions.checkArgument(e != null, "Invalid internal name: %s", internalName);
        org.bukkit.Particle p = org.bukkit.Particle.valueOf(e.name());
        Preconditions.checkArgument(p != null, "Particle with internal name '%s' has no bukkit counterpart.", internalName);
        return p;
    }
}

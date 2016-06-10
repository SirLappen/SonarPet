package net.techcable.sonarpet.item;

import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;

import net.techcable.sonarpet.utils.PlayerProfile;
import net.techcable.sonarpet.utils.ProfileUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullItemData extends ItemData {
    private final PlayerProfile profile;

    public Optional<PlayerProfile> getProfile() {
        if (getSkullType() == SkullType.HUMAN_SKULL && getMeta().hasOwner()) {
            String ownerName = getMeta().getOwner();
            Preconditions.checkState(profile.getName().equals(ownerName), "Profile %s doesn't match owner %s", profile, ownerName);
            return Optional.of(profile);
        } else {
            Preconditions.checkState(profile == null, "Profile %s is present but meta has no owner", profile);
            return Optional.empty();
        }
    }


    protected SkullItemData(byte rawData, ItemMeta meta) {
        this(rawData, meta, ((SkullMeta) meta).hasOwner() ? ProfileUtils.lookupOptimistically(((SkullMeta) meta).getOwner()) : null);
    }

    protected SkullItemData(byte rawData, ItemMeta meta, PlayerProfile profile) {
        super(Material.SKULL_ITEM, rawData, meta);
        Preconditions.checkArgument(profile == null || profile.getName().equals(((SkullMeta) meta).getOwner()));
        this.profile = profile;
    }

    public SkullItemData withOwner(UUID owner) {
        Preconditions.checkNotNull(owner, "Null owner uuid");
        if (getProfile().isPresent() && getProfile().get().getId().equals(owner)) {
            return this;
        } else {
            return withOwner(ProfileUtils.lookupOptimistically(owner));
        }
    }

    public SkullItemData withOwner(String ownerName) {
        Preconditions.checkNotNull(ownerName, "Null owner name");
        if (getProfile().isPresent() && getProfile().get().getName().equals(ownerName)) {
            return this;
        } else {
            return withOwner(ProfileUtils.lookupOptimistically(ownerName));
        }
    }

    public SkullItemData withOwner(PlayerProfile profile) {
        return profile.equals(this.profile) ? this : createHuman(profile, this.getMeta());
    }

    public Optional<UUID> getOwner() {
        return getProfile().map(PlayerProfile::getId);
    }


    public Optional<String> getOwnerName() {
        return getProfile().map(PlayerProfile::getName);
    }

    public boolean hasOwner() {
        return getProfile().isPresent();
    }

    public static SkullItemData createHuman() {
        return create(SkullType.HUMAN_SKULL);
    }

    public static SkullItemData createHuman(PlayerProfile owner) {
        return createHuman(owner, Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM));
    }

    public static SkullItemData createHuman(PlayerProfile owner, ItemMeta rawMeta) {
        Preconditions.checkNotNull(rawMeta, "Null meta");
        Preconditions.checkNotNull(owner, "Null owner");
        SkullMeta meta;
        if (rawMeta instanceof SkullMeta) {
            meta = (SkullMeta) rawMeta;
        } else {
            meta = (SkullMeta) Bukkit.getItemFactory().asMetaFor(rawMeta, Material.SKULL_ITEM);
        }
        meta = meta.clone(); // Don't modify their junk
        meta.setOwner(owner.getName());
        return create(SkullType.HUMAN_SKULL, meta);
    }


    public static SkullItemData create(SkullType type) {
        return create(type, Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM));
    }

    public static SkullItemData create(SkullType type, ItemMeta meta) {
        return new SkullItemData(Preconditions.checkNotNull(type, "Null skull type").getId(), Preconditions.checkNotNull(meta, "Null meta"));
    }

    public SkullType getSkullType() {
        int data = getRawData();
        SkullType[] values = SkullType.values();
        if (data < 0) {
            throw new IllegalStateException("Can't get skull type from negative data: " + data);
        } else if (data >= values.length) {
            throw new IllegalStateException("Can't get skull type from too large data: " + data);
        } else {
            return values[data];
        }
    }

    public ItemData withSkullType(SkullType type) {
        return withRawData(Preconditions.checkNotNull(type, "Null type").ordinal());
    }

    @Override
    public SkullMeta getMeta() {
        return (SkullMeta) super.getMeta();
    }

    public enum SkullType {
        SKELETON_SKULL,
        WITHER_SKELETON_SKULL,
        ZOMBIE_SKULL,
        HUMAN_SKULL,
        CREEPER_HEAD;

        public static SkullType getById(int id) {
            SkullType[] values = values();
            if (id < 0) {
                throw new IllegalArgumentException("Negative id: " + id);
            } else if (id >= values.length) {
                throw new IllegalArgumentException("Invalid id: " + id);
            } else {
                return values[id];
            }
        }

        public byte getId() {
            return (byte) ordinal();
        }
    }
}

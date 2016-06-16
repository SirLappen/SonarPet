package net.techcable.sonarpet.particles;

import lombok.*;

import java.util.Optional;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.GeometryUtil;
import com.google.common.base.Preconditions;

import net.techcable.sonarpet.utils.Versioning;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Getter
public abstract class ParticleBuilder {
    private Particle type;
    private Location position;
    private Vector offset;
    private float speed;
    private int amount;
    @Getter(AccessLevel.NONE)
    private Optional<Material> blockType = Optional.empty();
    private int blockMeta;

    public Material getBlockType() {
        Preconditions.checkState(hasBlockData(), "Block data not present");
        return blockType.get();
    }

    public int getBlockMeta() {
        Preconditions.checkState(hasBlockData(), "Block data not present");
        return blockMeta;
    }

    public boolean hasBlockData() {
        return blockType.isPresent();
    }

    protected ParticleBuilder(Particle type, float speed, int amount) {
        Preconditions.checkNotNull(type, "Null particle");
        ofType(type);
        offset(GeneralUtil.random().nextFloat(), GeneralUtil.random().nextFloat(), GeneralUtil.random().nextFloat());
        atSpeed(speed);
        ofAmount(amount);
    }

    public abstract void show(Player player);

    public void show() {
        for (Player player : GeometryUtil.getNearbyPlayers(this.position, 50)) {
            this.show(player);
        }

    }

    public ParticleBuilder ofType(Particle type) {
        Preconditions.checkNotNull(type, "Null type");
        this.type = type;
        return this;
    }

    public ParticleBuilder at(Location position) {
        Preconditions.checkNotNull(position, "Null position");
        this.position = position.clone();
        return this;
    }

    public ParticleBuilder at(World world, float x, float y, float z) {
        this.position = new Location(world, (double)x, (double)y, (double)z);
        return this;
    }

    public ParticleBuilder offset(Vector offset) {
        Preconditions.checkNotNull(offset, "Null offset");
        this.offset = offset.clone();
        return this;
    }

    public ParticleBuilder offset(float x, float y, float z) {
        this.offset(new Vector(x, y, z));
        return this;
    }

    public ParticleBuilder atSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public ParticleBuilder ofAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ParticleBuilder ofBlockType(Material material) {
        return this.ofBlockType(material, 0);
    }

    public ParticleBuilder ofBlockType(Material material, int metadata) {
        Preconditions.checkNotNull(material, "Null material");
        this.blockType = Optional.of(material);
        this.blockMeta = metadata;
        return this;
    }

    public Location getPosition() {
        Preconditions.checkState(position != null, "Position not present");
        return this.position.clone(); // defensive copy
    }

    public Vector getOffset() {
        assert offset != null : "Offset not present";
        return this.offset.clone();
    }

    public ParticleBuilder clone() throws CloneNotSupportedException {
        return (ParticleBuilder)super.clone();
    }

    //ToDo: Version Fix Here
    public static ParticleBuilder create(Particle type, float speed, int amount) {
        switch (Versioning.MAJOR_VERSION) {
            case 10:
            case 9:
                return new BukkitParticleBuilder(type, speed, amount);
            case 8:
                return new v18ParticleBuilder(type, speed, amount);
            case 7:
            case 6:
                return new AncientParticleBuilder(type, speed, amount);
            default:
                throw new AssertionError("Unsupported version: " + Versioning.NMS_VERSION);
        }
    }
}

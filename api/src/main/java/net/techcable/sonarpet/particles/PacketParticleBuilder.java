package net.techcable.sonarpet.particles;

import com.captainbern.minecraft.protocol.PacketType;
import com.captainbern.minecraft.wrapper.WrappedPacket;
import com.comphenix.protocol.ProtocolLibrary;
import com.dsh105.commodus.ServerUtil;
import com.google.common.base.Preconditions;

import net.techcable.sonarpet.utils.reflection.ReflectionConstants;

import org.bukkit.entity.Player;

public abstract class PacketParticleBuilder extends ParticleBuilder {
    public PacketParticleBuilder(Particle type, float speed, int amount) {
        super(type, speed, amount);
    }

    public void show(Player player) {
        WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.WORLD_PARTICLES);
        setupPacket(packet);
        Preconditions.checkState(getPosition() != null, "Position not set");
        Preconditions.checkState(getPosition() != null, "Offset not set");
        ReflectionConstants.sendPacket(player, packet.getHandle());
    }

    protected abstract Object getNMSParticleType();

    protected void setupPacket(WrappedPacket packet) {
        packet.getAccessor().write(0, this.getNMSParticleType());
        packet.getFloats().write(0, (float) getPosition().getX());
        packet.getFloats().write(1, (float) getPosition().getY());
        packet.getFloats().write(2, (float) getPosition().getZ());
        packet.getFloats().write(3, (float) getOffset().getX());
        packet.getFloats().write(4, (float) getOffset().getY());
        packet.getFloats().write(5, (float) getOffset().getZ());
        packet.getFloats().write(6, getSpeed());
        packet.getIntegers().write(0, getAmount());
    }
}

package net.techcable.sonarpet.particles;

import org.bukkit.Material;

public class AncientParticleBuilder extends PacketParticleBuilder {
    public AncientParticleBuilder(Particle type, float speed, int amount) {
        super(type, speed, amount);
    }

    @Override
    public ParticleBuilder ofBlockType(Material material, int metadata) {
        return this; // Do nothing, its not needed
    }

    @Override
    protected Object getNMSParticleType() {
        return getType().getInternalName();
    }
}

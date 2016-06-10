package com.dsh105.echopet.compat.nms.v1_9_R2.entity;

public class EntitySlimePetData extends EntityInsentientPetData {
    public EntitySlimePetData(EntitySlimePet pet) {
        super(pet);
    }

    @Override
    public EntitySlimePet getPet() {
        return (EntitySlimePet) super.getPet();
    }
}

package com.dsh105.echopet.compat.nms.v1_9_R2.entity;

public class EntityAgeablePetData extends EntityInsentientPetData {

    public EntityAgeablePetData(EntityAgeablePet pet) {
        super(pet);
    }

    @Override
    public EntityAgeablePet getPet() {
        return (EntityAgeablePet) super.getPet();
    }

}

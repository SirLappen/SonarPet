package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityInsentientPetData;

public class EntityNoClipPetData extends EntityInsentientPetData {

    public EntityNoClipPetData(EntityNoClipPet pet) {
        super(pet);
    }

    @Override
    public EntityAgeablePet getPet() {
        return (EntityAgeablePet) super.getPet();
    }

}

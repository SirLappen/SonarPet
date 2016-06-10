package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import lombok.*;

import com.dsh105.echopet.compat.api.ai.PetGoalSelector;

@RequiredArgsConstructor
public class EntityInsentientPetData {
    @Getter
    private final EntityInsentientPet pet;

    public boolean fireProof;
    public double jumpHeight;
    public float rideSpeed;
    public boolean shouldVanish;
    public PetGoalSelector petGoalSelector;

}

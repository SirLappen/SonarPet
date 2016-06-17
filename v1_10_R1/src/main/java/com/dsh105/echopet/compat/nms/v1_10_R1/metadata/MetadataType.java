package com.dsh105.echopet.compat.nms.v1_10_R1.metadata;

import lombok.*;

import java.util.UUID;

import com.dsh105.powermessage.libs.com.google.gson.internal.Primitives;
import com.google.common.base.Optional;

import net.minecraft.server.v1_10_R1.IBlockData;

@Getter
public enum MetadataType {
    BYTE(0, byte.class),
    VAR_INT(1, int.class),
    FLOAT(2, float.class),
    BOOLEAN(6, boolean.class),
    OPTIONAL_UUID(11, Optional.class) {
        @Override
        public void checkCast(Object obj) {
            checkOptionalCast(obj, UUID.class);
        }
    },
    OPTIONAL_BLOCK_DATA(12, Optional.class) {
        @Override
        public void checkCast(Object obj) {
            checkOptionalCast(obj, IBlockData.class);
        }
    };

    private final int index;
    private final Class<?> type;

    MetadataType(int index, Class<?> type) {
        this.index = index;
        this.type = Primitives.wrap(type);
    }

    public void checkCast(Object obj) {
        if (!type.isInstance(obj)) {
            throw new ClassCastException("Must be a " + type + " not a " + obj.getClass());
        }
    }

    private static void checkOptionalCast(Object obj, Class<?> optionalType) {
        if (obj instanceof Optional) {
            if (((Optional<?>) obj).isPresent() && !optionalType.isInstance(((Optional<?>) obj).get())) {
                throw new ClassCastException("The value in the optional must be " + optionalType + " not a " + obj.getClass());
            }
            ;
        } else {
            throw new ClassCastException("Must be a java.util.Optional not a " + obj.getClass());
        }
    }
}

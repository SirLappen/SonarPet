package com.dsh105.echopet.compat.nms.v1_9_R2.metadata;

import lombok.*;

import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.DataWatcherSerializer;

@Getter
public class MetadataKey<T> {
    @Getter(AccessLevel.PROTECTED)
    private final DataWatcherObject<T> handle;
    private final MetadataType type;

    @SuppressWarnings("unchecked") // Generics enums plz
    public MetadataKey(int index, MetadataType type) {
        handle = new DataWatcherObject<>(index, (DataWatcherSerializer<T>) DataWatcherRegistry.a(type.getIndex()));
        this.type = type;
    }

    @SuppressWarnings("unchecked") // We are checked....
    public T cast(Object obj) {
        type.checkCast(obj);
        return (T) obj;
    }

    public int getIndex() {
        return handle.a();
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || o != null
                && getClass() == o.getClass()
                && getHandle().equals(((MetadataKey) o).getHandle())
                && getType() == ((MetadataKey) o).getType();

    }

    @Override
    public int hashCode() {
        int result = getHandle().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }
}

package net.techcable.sonarpet.utils.reflection;

import lombok.*;

import com.google.common.primitives.Primitives;

import static net.techcable.sonarpet.utils.Versioning.NMS_PACKAGE;
import static net.techcable.sonarpet.utils.Versioning.OBC_PACKAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reflection {
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> Class<? extends T> getClass(String name, Class<T> clazz) {
        try {
            return Class.forName(name).asSubclass(clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getObcClass(String id) {
        return getClass(OBC_PACKAGE + "." + id);
    }

    public static Class<?> getNmsClass(String id) {
        return getClass(NMS_PACKAGE + "." + id);
    }

    public static <T> Class<? extends T> getNmsClass(String id, Class<T> clazz) {
        return getClass(NMS_PACKAGE + "." + id, clazz);
    }

    public static boolean isLenientlyAssignableFrom(Class<?> first, Class<?> second) {
        if (first.isPrimitive()) first = Primitives.wrap(first);
        if (second.isPrimitive()) second = Primitives.wrap(second);
        return first.isAssignableFrom(second);
    }
}

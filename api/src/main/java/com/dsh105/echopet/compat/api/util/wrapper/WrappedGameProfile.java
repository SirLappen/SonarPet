/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.util.wrapper;

import com.dsh105.echopet.compat.api.reflection.ReflectionConstants;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import net.techcable.sonarpet.utils.UUIDUtils;

public class WrappedGameProfile extends AbstractWrapper {

    private static final Class<?> GAME_PROFILE_CLASS;
    private static final Constructor STRING_CONSTURCTOR, UUID_CONSTRUCTOR;
    static {
        Class<?> gameProfileClass = null;
        try {
            gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
        } catch (ClassNotFoundException e) {
            try {
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e1) {
                throw new RuntimeException("Unable to find GameProfile");
            }
        }
        GAME_PROFILE_CLASS = gameProfileClass;
        Constructor stringConstructor, uuidConstructor;
        try {
            uuidConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
        } catch (NoSuchMethodException e) {
            uuidConstructor = null;
        }
        try {
            stringConstructor = gameProfileClass.getConstructor(String.class, String.class);
        } catch (NoSuchMethodException e) {
            stringConstructor = null;
        }
        STRING_CONSTURCTOR = stringConstructor;
        UUID_CONSTRUCTOR = uuidConstructor;
    }

    public WrappedGameProfile(UUID id, String name) {
        final Object handle;
        try {
            if (UUID_CONSTRUCTOR != null) {
                handle = UUID_CONSTRUCTOR.newInstance(id, name);
            } else if (STRING_CONSTURCTOR != null) {
                handle = STRING_CONSTURCTOR.newInstance(id.toString(), name);
            } else {
                throw new RuntimeException("Unable to find GameProfile constructor");
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to call gameprofile constructor", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("GameProfile constructor throw exception", e.getCause());
        }
        setHandle(handle);
    }

    public WrappedGameProfile(String ident, String name) {
        this(UUIDUtils.fromString(ident), name);
    }

    public static WrappedGameProfile getNewProfile(WrappedGameProfile old, String newName) {
        return new WrappedGameProfile(old.getUniqueId(), newName);
    }

    private static final Method UNIQUE_ID_METHOD = ReflectionUtil.getMethod(GAME_PROFILE_CLASS, ReflectionConstants.GAMEPROFILE_FUNC_ID.getName());

    public UUID getUniqueId() {
        Object o = ReflectionUtil.invokeMethod(UNIQUE_ID_METHOD, getHandle());
        if (o instanceof UUID) {
            return (UUID) o;
        } else if (o instanceof String) {
            return UUIDUtils.fromString((String) o);
        } else {
            throw new RuntimeException("Unable to parse unique id returned by gameprofile");
        }
    }
}

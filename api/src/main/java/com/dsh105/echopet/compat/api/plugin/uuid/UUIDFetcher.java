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

package com.dsh105.echopet.compat.api.plugin.uuid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableList;

import net.techcable.sonarpet.utils.PlayerProfile;
import net.techcable.sonarpet.utils.ProfileUtils;
import net.techcable.sonarpet.utils.UUIDUtils;

/**
 * A wrapper around {@link net.techcable.sonarpet.utils.ProfileUtils} for backwards compatibility
 */
public class UUIDFetcher implements Callable<Map<String, UUID>> {

    private static final double PROFILES_PER_REQUEST = 100;
    private final List<String> names;

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = ImmutableList.copyOf(names);
    }

    public UUIDFetcher(List<String> names) {
        this(names, true);
    }

    public static byte[] toBytes(UUID uuid) {
        return UUIDUtils.toBytes(uuid);
    }

    public static UUID fromBytes(byte[] array) {
        return UUIDUtils.fromBytes(array);
    }

    public static UUID getUUIDOf(String name) throws Exception {
        return ProfileUtils.lookup(name).map(PlayerProfile::getId).get();
    }

    public Map<String, UUID> call() throws Exception {
        Map<String, UUID> uuidMap = new HashMap<>();
        for (PlayerProfile profile : ProfileUtils.lookupAll(names)) {
            uuidMap.put(profile.getName(), profile.getId());
        }
        return uuidMap;
    }
}
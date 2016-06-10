package net.techcable.sonarpet.utils;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Techcable
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Utilities to lookup player names and uuids from mojang
 * This caches results so you won't have issues with the rate limit
 *
 * <p>
 * <b>DONT Rely on Bukkit.getOfflinePlayer()</b>
 * It doesn't cache and is a workaround solution
 *
 * evilmidgets fetchers are a fair solution, but they don't cache so you can run into ratelimits
 *
 * @author Techcable
 */
public class ProfileUtils {
    private ProfileUtils() {}

    public static final int PROFILES_PER_REQUEST = 100;
    private static Cache<String, PlayerProfile> nameCache = new Cache<>();

    /**
     * Lookup a profile with the given name
     *
     * The reuturned player profile doesn't include properties
     * If properties are neaded, proceed to use a uuid lookup
     *
     * @param name look for a profile with this name
     * @return a profile with the given name
     */
    public static Optional<PlayerProfile> lookup(String name) {
        if (Bukkit.getPlayerExact(name) != null) {
            return Optional.of(fromPlayer(Bukkit.getPlayerExact(name))) ;
        }
        if (nameCache.contains(name)) Optional.of(nameCache.get(name));
        List<PlayerProfile> response = postNames(ImmutableList.of(name));
        if (response == null) return Optional.empty();
        if (response.isEmpty()) return Optional.empty();
        return Optional.of(response.get(0));
    }


    public static ImmutableList<PlayerProfile> lookupAll(Iterable<String> iterable) {
        ImmutableList<String> names = ImmutableList.copyOf(Preconditions.checkNotNull(iterable, "Null collection"));
        PlayerProfile[] profiles = new PlayerProfile[names.size()];
        int profilesSize = 0;
        int requests = MathMagic.divideRoundUp(names.size(), PROFILES_PER_REQUEST);
        int nameIndex = 0;
        List<String> toRequest = new ArrayList<>(PROFILES_PER_REQUEST);
        for (int requestId = 0; requestId < requests; requestId++) {
            toRequest.clear();
            for (int start = nameIndex; nameIndex < names.size() && nameIndex < start + 100; nameIndex++) {
                String name = names.get(nameIndex);
                PlayerProfile profile;
                if ((profile = nameCache.get(name)) != null) {
                    profiles[profilesSize++] = profile;
                } else {
                    toRequest.add(name);
                }
            }
            for (PlayerProfile profile : postNames(toRequest)) {
                profiles[profilesSize++] = profile;
            }
        }
        profiles = Arrays.copyOf(profiles, profilesSize); // Trim
        return ImmutableList.copyOf(profiles);
    }

    /**
     * Lookup a profile with the given uuid
     *
     * The reuturned player profile may or may not include properties
     *
     * @param id look for a profile with this uuid
     * @return a profile with the given id
     */
    public static Optional<PlayerProfile> lookup(UUID id) {
        if (Bukkit.getPlayer(id) != null) {
            return Optional.of(fromPlayer(Bukkit.getPlayer(id)));
        }
        return lookupProperties(id);
    }



    /**
     * Lookup a profile with the given name, throwing an error if the profile is not found
     *
     * The returned player profile may or may not include properties
     *
     * @param name look for a profile with this name
     * @throws IllegalArgumentException if no profile with the given id is found
     * @return a profile with the given name
     */
    public static PlayerProfile lookupOptimistically(String name) {
        return lookup(name).orElseThrow(() -> new IllegalArgumentException("No player named " + name + " is found."));
    }


    /**
     * Lookup a profile with the given uuid, throwing an error if the profile is not found
     *
     * The returned player profile may or may not include properties
     *
     * @param id look for a profile with this uuid
     * @throws IllegalArgumentException if no profile with the given id is found
     * @return a profile with the given id
     */
    public static PlayerProfile lookupOptimistically(UUID id) {
        return lookup(id).orElseThrow(() -> new IllegalArgumentException("No player with the uuid " + id + " is found."));
    }


    /**
     * Lookup the players properties
     *
     * @param id player to lookup
     *
     * @return the player's profile with properties
     */
    public static Optional<PlayerProfile> lookupProperties(UUID id) {
        if (idCache.contains(id)) return Optional.of(idCache.get(id));
        Object rawResponse = getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
        if (rawResponse == null || !(rawResponse instanceof JSONObject)) return Optional.empty();
        JSONObject response = (JSONObject) rawResponse;
        PlayerProfile profile = deserializeProfile(response);
        if (profile == null) return Optional.empty();
        idCache.put(id, profile);
        return Optional.of(profile);
    }


    private static Cache<UUID, PlayerProfile> idCache = new Cache<>();

    private static List<PlayerProfile> postNames(List<String> names) { //This one doesn't cache
        JSONArray request = names.stream().collect(Collectors.toCollection(JSONArray::new));
        Object rawResponse = postJson("https://api.mojang.com/profiles/minecraft", request);
        if (!(rawResponse instanceof JSONArray)) return null;
        JSONArray response = (JSONArray) rawResponse;
        List<PlayerProfile> profiles = new ArrayList<>();
        for (Object rawEntry : response) {
            if (!(rawEntry instanceof JSONObject)) return null;
            JSONObject entry = (JSONObject) rawEntry;
            PlayerProfile profile = deserializeProfile(entry);
            if (profile != null) profiles.add(profile);
        }
        return profiles;
    }

    //Json Serialization

    private static PlayerProfile deserializeProfile(JSONObject json) {
        if (!json.containsKey("name") || !json.containsKey("id")) return null;
        if (!(json.get("name") instanceof String) || !(json.get("id") instanceof String)) return null;
        String name = (String) json.get("name");
        if (json.get("id") == null) return null;
        UUID id = UUIDUtils.fromString((String) json.get("id"));
        PlayerProfile profile = new PlayerProfile(id, name);
        if (json.containsKey("properties") && json.get("properties") instanceof JSONArray) {
            profile.properties = (JSONArray) json.get("properties");
        }
        return profile;
    }

    //Utilities

    private static String toString(UUID id) {
        return id.toString().replace("-", "");
    }

    private static JSONParser PARSER = new JSONParser();

    private static Object getJson(String rawUrl) {
        BufferedReader reader = null;
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return PARSER.parse(result.toString());
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Object postJson(String url, JSONArray body) {
        String rawResponse = post(url, body.toJSONString());
        if (rawResponse == null) return null;
        try {
            return PARSER.parse(rawResponse);
        } catch (Exception e) {
            return null;
        }
    }

    private static String post(String rawUrl, String body) {
        BufferedReader reader = null;
        OutputStream out = null;

        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            out = connection.getOutputStream();
            out.write(body.getBytes());
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return result.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) out.close();
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class Cache<K, V> {

        private long expireTime = 1000 * 60 * 5; //default 5 min
        private Map<K, CachedEntry<V>> map = new HashMap<>();

        public boolean contains(K key) {
            return map.containsKey(key) && get(key) != null;
        }

        public V get(K key) {
            CachedEntry<V> entry = map.get(key);
            if (entry == null) return null;
            if (entry.isExpired()) {
                map.remove(key);
                return null;
            } else {
                return entry.getValue();
            }
        }

        public void put(K key, V value) {
            map.put(key, new CachedEntry(value, expireTime));
        }

        private static class CachedEntry<V> {

            public CachedEntry(V value, long expireTime) {
                this.value = new SoftReference(value);
                this.expires = expireTime + System.currentTimeMillis();
            }

            private final SoftReference<V> value; //Caching is low memory priortiy
            private final long expires;

            public V getValue() {
                if (isExpired()) {
                    return null;
                }
                return value.get();
            }

            public boolean isExpired() {
                if (value.get() == null) return true;
                return expires != -1 && expires > System.currentTimeMillis();
            }
        }
    }

    private static PlayerProfile fromPlayer(Player player) {
        return new PlayerProfile(player.getUniqueId(), player.getName());
    }
}

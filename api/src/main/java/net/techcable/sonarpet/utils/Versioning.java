package net.techcable.sonarpet.utils;

import lombok.*;

import java.util.Arrays;

import org.bukkit.Bukkit;

public class Versioning {
    public static final String NMS_VERSION_STRING;
    public static final NmsVersion NMS_VERSION;
    public static final int MAJOR_VERSION;
    public static final String NMS_PACKAGE;
    public static final String OBC_PACKAGE;

    static {
        String[] parts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (parts.length > 3) {
            NMS_VERSION_STRING = parts[3]; // We can determine NMS version from the package name
            NMS_PACKAGE = "net.minecraft.server." + NMS_VERSION_STRING;
            OBC_PACKAGE = "org.bukkit.craftbukkit." + NMS_VERSION_STRING;
        } else {
            // Oh boy, the package doesn't have a version string
            NMS_PACKAGE = "net.minecraft.server";
            OBC_PACKAGE = "org.bukkit.craftbukkit";
            NMS_VERSION_STRING = NmsVersion.LATEST.toString();
        }
        NMS_VERSION = NmsVersion.getVersion(NMS_VERSION_STRING);
        MAJOR_VERSION = parseMajorVersion(NMS_VERSION_STRING);
    }

    private static int parseMajorVersion(String s) {
        return Integer.parseInt(s.split("_")[1]);
    }

    public static boolean isSupported() {
        return NMS_VERSION != null;
    }

    //ToDo: Version Fix Here
    @Getter
    public enum NmsVersion {
        v1_6_R3,
        v1_7_R1,
        v1_7_R2,
        v1_7_R3,
        v1_7_R4,
        v1_8_R1,
        v1_8_R2,
        v1_9_R1,
        v1_9_R2,
        v1_10_R1;

        public static final NmsVersion LATEST;

        static {
            NmsVersion[] sorted = values();
            Arrays.sort(sorted, (firstVersion, secondVersion) -> firstVersion.name().compareTo(secondVersion.name()));
            LATEST = sorted[sorted.length - 1];
        }

        public static NmsVersion getVersion(String s) {
            try {
                return valueOf(s);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        private final int majorVersion = parseMajorVersion(name());
    }
}

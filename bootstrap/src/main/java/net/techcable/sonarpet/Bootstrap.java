package net.techcable.sonarpet;

import lombok.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.dsh105.echopet.EchoPetPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Bootstrap extends JavaPlugin {
    private static final Field PLUGINS_BY_NAME_FIELD;

    static {
        Field pluginsByNameField = null;
        try {
            pluginsByNameField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
            pluginsByNameField.setAccessible(true);
        } catch (NoSuchFieldException ignored) {}
        PLUGINS_BY_NAME_FIELD = pluginsByNameField;
    }

    private BootstrapedPlugin plugin;


    @Override
    @SuppressWarnings("Since15") // We should be java 8 compatible after we check for JDK/JRE 8 >:)
    public void onLoad() {
        int javaVersion;
        try {
            javaVersion = Integer.parseInt(System.getProperty("java.version").split("\\.")[1]);
        } catch (NumberFormatException e) {
            getLogger().warning("Unable to parse java version " + System.getProperty("java.version"));
            getLogger().warning("Assuming Java 8.");
            javaVersion = 8;
        }
        if (javaVersion < 8) {
            getLogger().severe("Your server is running java " + javaVersion + ", but java 7 is required to run SonarPets!");
            getLogger().severe("Either update your java or uninstall SonarPets!");
            getLogger().severe("Shutting down.");
            getServer().shutdown();
            return;
        }
        plugin = new EchoPetPlugin(this);
        // Inject plugin as 'EchoPet' for backwards compatibility
        try {
            if (PLUGINS_BY_NAME_FIELD != null) {
                @SuppressWarnings("unchecked")
                Map<String, Plugin> pluginsByName = (Map<String, Plugin>) PLUGINS_BY_NAME_FIELD.get(getServer().getPluginManager());
                Plugin old = pluginsByName.putIfAbsent("EchoPet", plugin);
                if (old != null) {
                    getLogger().severe("EchoPet was detected on your server.");
                    getLogger().severe("EchoPet is incompatible with SonarPets and you should uninstall it.");
                    Bukkit.shutdown();
                    return;
                }
            } else {
                getLogger().warning("Unable to inject SonarPets backwards compatibility into the plugin list!");
                getLogger().warning("Can't find field 'lookupNames' in " + Bukkit.getPluginManager().getClass().getTypeName());
                getLogger().warning("This is non fatal but may break backwards compatibility with EchoPets.");
            }
        } catch (IllegalAccessException e) {
            getLogger().log(Level.WARNING, "Unable to inject SonarPets backwards compatibility into the plugin list!", e);
            getLogger().warning("This is non fatal but may break backwards compatibility with EchoPets.");
        }
        plugin.onLoad();
    }

    @Override
    public void onEnable() {
        plugin.onEnable();
    }

    @Override
    @SuppressWarnings("Since15") // We should be java 8 compatible after since we'ved check for JDK/JRE 8 >:)
    public void onDisable() {
        plugin.onDisable();
        // Inject plugin as 'EchoPet' for backwards compatibility
        try {
            if (PLUGINS_BY_NAME_FIELD != null) {
                @SuppressWarnings("unchecked")
                Map<String, Plugin> pluginsByName = (Map<String, Plugin>) PLUGINS_BY_NAME_FIELD.get(getServer().getPluginManager());
                pluginsByName.remove("EchoPet");
            } else {
                getLogger().warning("Unable to remove SonarPets backwards compatibility into the plugin list!");
                getLogger().warning("Can't find field 'lookupNames' in " + Bukkit.getPluginManager().getClass().getTypeName());
                getLogger().warning("This is non fatal but may break backwards compatibility with EchoPets.");
            }
        } catch (IllegalAccessException e) {
            getLogger().log(Level.WARNING, "Unable to remove SonarPets backwards compatibility into the plugin list!", e);
            getLogger().warning("This is non fatal but may break backwards compatibility with EchoPets.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return plugin.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return plugin.onTabComplete(sender, command, alias, args);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return plugin.getDefaultWorldGenerator(worldName, id);
    }
}

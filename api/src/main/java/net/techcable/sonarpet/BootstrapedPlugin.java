package net.techcable.sonarpet;

import lombok.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import com.avaje.ebean.EbeanServer;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

@Getter
@RequiredArgsConstructor
public abstract class BootstrapedPlugin implements Plugin {
    @Nonnull
    private final Plugin delegate;

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

    @Override
    public void onLoad() {}

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }


    //
    // Delegates
    //

    @Override
    public File getDataFolder() {
        return delegate.getDataFolder();
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return delegate.getDescription();
    }

    @Override
    public FileConfiguration getConfig() {
        return delegate.getConfig();
    }

    @Override
    public InputStream getResource(String filename) {
        return delegate.getResource(filename);
    }

    @Override
    public void saveConfig() {
        delegate.saveConfig();
    }

    @Override
    public void saveDefaultConfig() {
        delegate.saveDefaultConfig();
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        delegate.saveResource(resourcePath, replace);
    }

    @Override
    public void reloadConfig() {
        delegate.reloadConfig();
    }

    @Override
    public PluginLoader getPluginLoader() {
        return delegate.getPluginLoader();
    }

    @Override
    public Server getServer() {
        return delegate.getServer();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public boolean isNaggable() {
        return delegate.isNaggable();
    }

    @Override
    public void setNaggable(boolean canNag) {
        delegate.setNaggable(canNag);
    }

    @Override
    public EbeanServer getDatabase() {
        return delegate.getDatabase();
    }

    @Override
    public Logger getLogger() {
        return delegate.getLogger();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}

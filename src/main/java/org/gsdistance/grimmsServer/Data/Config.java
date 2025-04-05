package org.gsdistance.grimmsServer.Data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.util.logging.Logger;

public class Config {
    public final JavaPlugin plugin;
    public final Logger logger;
    public final FileConfiguration config;
    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.config = plugin.getConfig();
    }

    public void saveConfig() {
        try {
            config.save(plugin.getDataFolder().getPath() + File.pathSeparatorChar + "config.yml");
        } catch (Exception e) {
            logger.warning("Failed to save config file");
            logger.warning(e.getMessage());
        }
    }
}

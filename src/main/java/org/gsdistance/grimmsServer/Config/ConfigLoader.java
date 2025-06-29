package org.gsdistance.grimmsServer.Config;

import com.google.common.base.Stopwatch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;

public class ConfigLoader {
    private static FileConfiguration config;

    public static void initialize(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void loadConfigFromFile() {
        Stopwatch sw = Stopwatch.createStarted();
        GrimmsServer.logger.info("Loading GrimmsServer config...");
        if (config == null) {
            GrimmsServer.logger.warning("Config somehow not initialized. (" + sw.stop() + ")");
            return;
        }

        for (ConfigKey key : ConfigKey.values()) {
            Object value = config.get(key.getKey(), key.getDefaultValue());
            ActiveConfig.setConfigValue(key, value);
        }

        GrimmsServer.logger.info("GrimmsServer config loaded successfully. (" + sw.stop() + ")");
    }
}
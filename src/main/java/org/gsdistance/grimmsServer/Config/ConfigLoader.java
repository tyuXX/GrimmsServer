package org.gsdistance.grimmsServer.Config;

import com.google.common.base.Stopwatch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    private static FileConfiguration config;

    public ConfigLoader() {
    }

    public static void initialize(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            generateDefaultConfig(configFile);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private static void generateDefaultConfig(File configFile) {
        YamlConfiguration yamlConfig = new YamlConfiguration();
        for (ConfigKey key : ConfigKey.values()) {
            yamlConfig.set(key.getKey(), key.getDefaultValue());
        }
        try {
            yamlConfig.save(configFile);
            GrimmsServer.logger.info("Generated default config.yml from ConfigKey enum");
        } catch (IOException e) {
            GrimmsServer.logger.severe("Failed to generate default config.yml: " + e.getMessage());
        }
    }

    public static void loadConfigFromFile() {
        Stopwatch sw = Stopwatch.createStarted();
        GrimmsServer.logger.info("Loading GrimmsServer config...");
        if (config == null) {
            GrimmsServer.logger.warning("Config somehow not initialized. (" + sw.stop() + ")");
        } else {
            for (ConfigKey key : ConfigKey.values()) {
                Object value = config.get(key.getKey(), key.getDefaultValue());
                ActiveConfig.setConfigValue(key, value);
            }

            GrimmsServer.logger.info("GrimmsServer config loaded successfully. (" + sw.stop() + ")");
        }
    }
}

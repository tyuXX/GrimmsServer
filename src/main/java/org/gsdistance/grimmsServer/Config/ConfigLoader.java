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
    private static File configFile;

    public ConfigLoader() {
    }

    public static void initialize(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            generateDefaultConfig(configFile);
        } else {
            migrateConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private static void migrateConfig() {
        YamlConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);
        String currentVersion = GrimmsServer.instance.getDescription().getVersion();
        String configVersion = existingConfig.getString(ConfigKey.CONFIG_VERSION.getKey());

        if (configVersion == null || !configVersion.equals(currentVersion)) {
            boolean needsUpdate = false;
            for (ConfigKey key : ConfigKey.values()) {
                if (!existingConfig.contains(key.getKey())) {
                    existingConfig.set(key.getKey(), key.getDefaultValue());
                    needsUpdate = true;
                    GrimmsServer.logger.info("Added missing config field: " + key.getKey());
                }
            }
            existingConfig.set(ConfigKey.CONFIG_VERSION.getKey(), currentVersion);

            try {
                existingConfig.save(configFile);
                if (needsUpdate) {
                    GrimmsServer.logger.info("Config migrated to version " + currentVersion + " with new fields");
                } else {
                    GrimmsServer.logger.info("Config version updated to " + currentVersion);
                }
            } catch (IOException e) {
                GrimmsServer.logger.severe("Failed to migrate config: " + e.getMessage());
            }
        }
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
        if (configFile == null) {
            GrimmsServer.logger.warning("Config file not initialized. (" + sw.stop() + ")");
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
            for (ConfigKey key : ConfigKey.values()) {
                Object value = config.get(key.getKey(), key.getDefaultValue());
                ActiveConfig.setConfigValue(key, value);
            }

            GrimmsServer.logger.info("GrimmsServer config loaded successfully. (" + sw.stop() + ")");
        }
    }
}

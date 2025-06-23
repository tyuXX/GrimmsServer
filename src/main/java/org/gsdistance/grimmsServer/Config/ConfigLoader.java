package org.gsdistance.grimmsServer.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.util.List;

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
        GrimmsServer.logger.info("Loading GrimmsServer config...");
        if (config == null) {
            GrimmsServer.logger.warning("Config somehow not initialized.");
            return;
        }
        ActiveConfig.updateConfig(
                config.getBoolean("module_Jobs", true),
                config.getBoolean("module_Factions", true),
                config.getBoolean("module_Market", true),
                config.getBoolean("module_Leaderboard", true),
                config.getStringList("disabledCommands"),
                config.getBoolean("module_Chat", true),
                config.getBoolean("module_Homes", true),
                config.getBoolean("module_Leveling", true),
                config.getBoolean("module_Titles", true),
                config.getBoolean("module_Relics", true),
                config.getBoolean("module_Events", true),
                config.getBoolean("module_Utils", true),
                config.getBoolean("module_Ranks", true),
                config.getBoolean("joinMessage", true),
                config.getBoolean("module_Dimensions", true)
        );
        GrimmsServer.logger.info("GrimmsServer config loaded successfully.");
    }
}

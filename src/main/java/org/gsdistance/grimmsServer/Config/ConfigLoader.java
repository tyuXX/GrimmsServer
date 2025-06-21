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

        boolean module_Jobs = config.getBoolean("module_Jobs", true);
        boolean module_Factions = config.getBoolean("module_Factions", true);
        boolean module_Market = config.getBoolean("module_Market", true);
        boolean module_Leaderboard = config.getBoolean("module_Leaderboard", true);
        List<String> disabledCommands = config.getStringList("disabledCommands");
        boolean module_Chat = config.getBoolean("module_Chat", true);
        boolean module_Homes = config.getBoolean("module_Homes", true);
        boolean module_Leveling = config.getBoolean("module_Leveling", true);
        boolean module_Titles = config.getBoolean("module_Titles", true);
        boolean module_Relics = config.getBoolean("module_Relics", true);
        boolean module_Events = config.getBoolean("module_Events", true);
        boolean module_Utils = config.getBoolean("module_Utils", true);
        boolean module_Ranks = config.getBoolean("module_Ranks", true);

        ActiveConfig.updateConfig(module_Jobs, module_Factions, module_Market, module_Leaderboard, disabledCommands, module_Chat, module_Homes, module_Leveling, module_Titles, module_Relics, module_Events, module_Utils, module_Ranks);
        GrimmsServer.logger.info("GrimmsServer config loaded successfully.");
    }
}

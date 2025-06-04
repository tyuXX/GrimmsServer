package org.gsdistance.grimmsServer.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class ConfigLoader {
    private static FileConfiguration config;
    private static File configFile;

    public static void initialize(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void loadConfigFromFile() {
        if (config == null) return;

        boolean module_Jobs = config.getBoolean("module_Jobs", true);
        boolean module_Factions = config.getBoolean("module_Factions", true);
        boolean module_Market = config.getBoolean("module_Market", true);
        boolean module_Leaderboard = config.getBoolean("module_Leaderboard", true);
        List<String> disabledCommands = config.getStringList("disabledCommands");

        ActiveConfig.updateConfig(module_Jobs, module_Factions, module_Market, module_Leaderboard, disabledCommands);
    }

    public static void saveConfigFromActive() {
        if (config == null) return;

        config.set("module_Jobs", ActiveConfig.module_Jobs);
        config.set("module_Factions", ActiveConfig.module_Factions);
        config.set("module_Market", ActiveConfig.module_Market);
        config.set("module_Leaderboard", ActiveConfig.module_Leaderboard);
        config.set("disabledCommands", List.of(ActiveConfig.disabledCommands));

        try {
            config.save(configFile);
        } catch (IOException e) {
            GrimmsServer.logger.log(Level.WARNING, "Failed to save config file: " + e.getMessage());
        }
    }
}

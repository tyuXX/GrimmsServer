package org.gsdistance.grimmsServer.Data;

import org.gsdistance.grimmsServer.Config.ConfigKey;

import java.util.Map;

import static org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue;

public class ConfigRequirements {
    public static final Map<String, String> CommandRequirements = Map.ofEntries(
            Map.entry("logLeaderboard", "leaderboard"),
            Map.entry("sendMoney", "market"),
            Map.entry("makeItemLevelable", "leveling"),
            Map.entry("setPlayerStat", "stats"),
            Map.entry("logEnchantmentCosts", "market"),
            Map.entry("logJobs", "job"),
            Map.entry("takeJob", "job"),
            Map.entry("addTitle", "titles"),
            Map.entry("removeTitle", "titles"),
            Map.entry("executePlayer", "titles"),
            Map.entry("withdrawMoney", "market"),
            Map.entry("depositMoney", "market"),
            Map.entry("home", "homes"),
            Map.entry("nick", "chat"),
            Map.entry("market", "market"),
            Map.entry("gDim", "dimensions")
    );

    public static boolean isCommandEnabled(String command) {
        return switch (CommandRequirements.getOrDefault(command, "default")) {
            case "market" -> getConfigValue(ConfigKey.MODULE_MARKET,Boolean.class);
            case "leaderboard" -> getConfigValue(ConfigKey.MODULE_LEADERBOARD, Boolean.class);
            case "job" -> getConfigValue(ConfigKey.MODULE_JOBS, Boolean.class);
            case "stats" -> getConfigValue(ConfigKey.MODULE_LEVELING, Boolean.class);
            case "titles" -> getConfigValue(ConfigKey.MODULE_TITLES, Boolean.class);
            case "chat" -> getConfigValue(ConfigKey.MODULE_CHAT, Boolean.class);
            case "homes" -> getConfigValue(ConfigKey.MODULE_HOMES, Boolean.class);
            case "utils" -> getConfigValue(ConfigKey.MODULE_UTILS, Boolean.class);
            case "ranks" -> getConfigValue(ConfigKey.MODULE_RANKS, Boolean.class);
            case "factions" -> getConfigValue(ConfigKey.MODULE_FACTIONS, Boolean.class);
            case "relics" -> getConfigValue(ConfigKey.MODULE_RELICS, Boolean.class);
            case "events" -> getConfigValue(ConfigKey.MODULE_EVENTS, Boolean.class);
            case "dimensions" -> getConfigValue(ConfigKey.MODULE_DIMENSIONS, Boolean.class);
            case "default" -> true; // Default commands are always enabled
            default -> true; // Unknown command category
        };
    }
}

package org.gsdistance.grimmsServer.Data;

import org.gsdistance.grimmsServer.Config.ActiveConfig;

import java.util.Map;

public class ConfigRequirements {
    public static final Map<String, String> CommandRequirements = Map.ofEntries(
            Map.entry("sellItem", "market"),
            Map.entry("buyItem", "market"),
            Map.entry("getMarketStock", "market"),
            Map.entry("getMarket", "market"),
            Map.entry("logLeaderboard", "leaderboard"),
            Map.entry("sendMoney", "market"),
            Map.entry("buyRipoff", "market"),
            Map.entry("makeItemLevelable", "leveling"),
            Map.entry("setPlayerStat", "stats"),
            Map.entry("logPlayerStats", "stats"),
            Map.entry("logSelfStats", "stats"),
            Map.entry("logWorldStats", "stats"),
            Map.entry("buyEnchantment", "market"),
            Map.entry("logEnchantmentCosts", "market"),
            Map.entry("logJobs", "job"),
            Map.entry("takeJob", "job"),
            Map.entry("logSelfTitles", "titles"),
            Map.entry("logPlayerTitles", "titles"),
            Map.entry("addTitle", "titles"),
            Map.entry("removeTitle", "titles"),
            Map.entry("executePlayer", "titles"),
            Map.entry("grimmsServerCommands", "default"),
            Map.entry("withdrawMoney", "market"),
            Map.entry("depositMoney", "market"),
            Map.entry("acceptRequest", "default"),
            Map.entry("buyTp", "market"),
            Map.entry("reloadGrimmsConfig", "default"),
            Map.entry("home", "homes"),
            Map.entry("nick", "chat")
    );
    public static boolean isCommandEnabled(String command) {
        return switch (CommandRequirements.getOrDefault(command, "default")) {
            case "market" -> ActiveConfig.module_Market;
            case "leaderboard" -> ActiveConfig.module_Leaderboard;
            case "job" -> ActiveConfig.module_Jobs;
            case "stats" -> ActiveConfig.module_Leveling;
            case "titles" -> ActiveConfig.module_Titles;
            case "chat" -> ActiveConfig.module_Chat;
            case "homes" -> ActiveConfig.module_Homes;
            case "utils" -> ActiveConfig.module_Utils;
            case "ranks" -> ActiveConfig.module_Ranks;
            case "factions" -> ActiveConfig.module_Factions;
            case "relics" -> ActiveConfig.module_Relics;
            case "events" -> ActiveConfig.module_Events;
            case "default" -> true; // Default commands are always enabled
            default -> true; // Unknown command category
        };
    }
}

package org.gsdistance.grimmsServer.Config;

import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.List;

public class ActiveConfig {
    public static boolean module_Jobs = true;
    public static boolean module_Factions = true;
    public static boolean module_Market = true;
    public static boolean module_Leaderboard = true;
    public static String[] disabledCommands = new String[]{};
    public static boolean module_Chat = true;
    public static boolean module_Homes = true;
    public static boolean module_Leveling = true;
    public static boolean module_Titles = true;
    public static boolean module_Relics = true;
    public static boolean module_Events = true;
    public static boolean module_Utils = true;
    public static boolean module_Ranks = true;

    public static void updateConfig(boolean module_Jobs, boolean module_Factions, boolean module_Market, boolean module_Leaderboard, List<String> disabledCommands, boolean module_Chat, boolean module_Homes, boolean module_Leveling, boolean module_Titles, boolean module_Relics, boolean module_Events, boolean module_Utils, boolean module_Ranks) {
        ActiveConfig.module_Jobs = module_Jobs;
        ActiveConfig.module_Factions = module_Factions;
        ActiveConfig.module_Market = module_Market;
        ActiveConfig.module_Leaderboard = module_Leaderboard;
        ActiveConfig.disabledCommands = disabledCommands.toArray(new String[0]);
        ActiveConfig.module_Chat = module_Chat;
        ActiveConfig.module_Homes = module_Homes;
        ActiveConfig.module_Leveling = module_Leveling;
        ActiveConfig.module_Titles = module_Titles;
        ActiveConfig.module_Relics = module_Relics;
        ActiveConfig.module_Events = module_Events;
        ActiveConfig.module_Utils = module_Utils;
        ActiveConfig.module_Ranks = module_Ranks;

        // Log the status of each module
        if (!module_Jobs) {
            GrimmsServer.logger.info("Jobs module is disabled by config.");
        }
        if (!module_Factions) {
            GrimmsServer.logger.info("Factions module is disabled by config.");
        }
        if (!module_Market) {
            GrimmsServer.logger.info("Market module is disabled by config.");
        }
        if (!module_Leaderboard) {
            GrimmsServer.logger.info("Leaderboard module is disabled by config.");
        }
        if (!module_Chat) {
            GrimmsServer.logger.info("Chat module is disabled by config.");
        }
        if (!module_Homes) {
            GrimmsServer.logger.info("Homes module is disabled by config.");
        }
        if (!module_Leveling) {
            GrimmsServer.logger.info("Leveling module is disabled by config.");
        }
        if (!module_Titles) {
            GrimmsServer.logger.info("Titles module is disabled by config.");
        }
        if (!module_Relics) {
            GrimmsServer.logger.info("Relics module is disabled by config.");
        }
        if (!module_Events) {
            GrimmsServer.logger.info("Events module is disabled by config.");
        }
        if (!module_Utils) {
            GrimmsServer.logger.info("Utils module is disabled by config.");
        }
        if (disabledCommands.isEmpty()) {
            GrimmsServer.logger.info("No commands are disabled by config.");
        } else {
            GrimmsServer.logger.info("Disabled commands: " + String.join(", ", disabledCommands));
        }
        if (!module_Ranks) {
            GrimmsServer.logger.info("Ranks module is disabled by config.");
        }
    }
}

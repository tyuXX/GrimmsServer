package org.gsdistance.grimmsServer.Config;

import java.util.List;

public class ActiveConfig {
    public static boolean module_Jobs = true;
    public static boolean module_Factions = true;
    public static boolean module_Market = true;
    public static boolean module_Leaderboard = true;
    public static String[] disabledCommands = new String[] {};

    public static void updateConfig(boolean module_Jobs, boolean module_Factions,
                                     boolean module_Market, boolean module_Leaderboard, List<String> disabledCommands) {
        ActiveConfig.module_Jobs = module_Jobs;
        ActiveConfig.module_Factions = module_Factions;
        ActiveConfig.module_Market = module_Market;
        ActiveConfig.module_Leaderboard = module_Leaderboard;
        ActiveConfig.disabledCommands = disabledCommands.toArray(new String[0]);
    }
}

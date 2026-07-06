package org.gsdistance.grimmsServer.Data;

import java.util.Map;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;

public class ConfigRequirements {
   public static final Map<String, String> CommandRequirements = Map.ofEntries(Map.entry("logLeaderboard", "leaderboard"), Map.entry("sendMoney", "market"), Map.entry("makeItemLevelable", "leveling"), Map.entry("setPlayerStat", "stats"), Map.entry("logEnchantmentCosts", "market"), Map.entry("logJobs", "job"), Map.entry("takeJob", "job"), Map.entry("addTitle", "titles"), Map.entry("removeTitle", "titles"), Map.entry("executePlayer", "titles"), Map.entry("withdrawMoney", "market"), Map.entry("depositMoney", "market"), Map.entry("home", "homes"), Map.entry("nick", "chat"), Map.entry("market", "market"), Map.entry("gDim", "dimensions"));

   public ConfigRequirements() {
   }

   public static boolean isCommandEnabled(String command) {
      return switch ((String)CommandRequirements.getOrDefault(command, "default")) {
         case "market" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_MARKET, Boolean.class);
         case "leaderboard" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_LEADERBOARD, Boolean.class);
         case "job" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_JOBS, Boolean.class);
         case "stats" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_LEVELING, Boolean.class);
         case "titles" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_TITLES, Boolean.class);
         case "chat" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_CHAT, Boolean.class);
         case "homes" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_HOMES, Boolean.class);
         case "utils" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_UTILS, Boolean.class);
         case "ranks" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_RANKS, Boolean.class);
         case "factions" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_FACTIONS, Boolean.class);
         case "relics" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_RELICS, Boolean.class);
         case "events" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_EVENTS, Boolean.class);
         case "dimensions" -> (Boolean)ActiveConfig.getConfigValue(ConfigKey.MODULE_DIMENSIONS, Boolean.class);
         case "default" -> true;
         default -> true;
      };
   }
}

package org.gsdistance.grimmsServer.Commands.GLogCommand;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogSelfStats {
   public LogSelfStats() {
   }

   public static boolean subCommand(Player player) {
      if (!player.hasPermission("grimmsserver.log.self")) {
         return false;
      } else {
         PlayerStats stats = PlayerStats.getPlayerStats(player);
         ArrayList<String> statsList = new ArrayList();
         statsList.add("__Your stats:");

         for(String stat : PlayerStats.StatOrder) {
            Object value = stats.getStat(stat, Object.class);
            if (value instanceof Double) {
               String var10001 = (String)PlayerStats.StatNames.get(stat);
               statsList.add("|" + var10001 + ": " + Shared.formatNumber((Double)value));
            } else {
               String var6 = (String)PlayerStats.StatNames.get(stat);
               statsList.add("|" + var6 + ": " + value.toString());
            }
         }

         GeneralChatHandler.sendArray(player, (String[])statsList.toArray(new String[0]));
         return true;
      }
   }
}

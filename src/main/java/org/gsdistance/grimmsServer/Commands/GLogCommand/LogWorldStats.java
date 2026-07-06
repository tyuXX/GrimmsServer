package org.gsdistance.grimmsServer.Commands.GLogCommand;

import java.util.Enumeration;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class LogWorldStats {
   public LogWorldStats() {
   }

   public static boolean subCommand(Player player) {
      WorldStats stats = WorldStats.getWorldStats(player.getWorld());
      player.sendMessage("__World stats:");
      Enumeration<String> keys = WorldStats.Stats.keys();

      while(keys.hasMoreElements()) {
         String stat = (String)keys.nextElement();
         Object value = stats.getStat(stat);
         String var10001 = (String)WorldStats.StatNames.get(stat);
         player.sendMessage("|" + var10001 + ": " + value.toString());
      }

      return true;
   }
}

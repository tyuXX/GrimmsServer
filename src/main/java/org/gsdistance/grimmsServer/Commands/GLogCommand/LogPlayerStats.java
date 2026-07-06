package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogPlayerStats {
   public LogPlayerStats() {
   }

   public static boolean subCommand(Player player, String[] args) {
      if (!player.hasPermission("grimmsserver.log.other")) {
         return false;
      } else if (args.length < 2) {
         player.sendMessage("Please specify a player name.");
         return false;
      } else {
         Player tplayer = GrimmsServer.instance.getServer().getPlayer(args[1]);
         if (tplayer == null) {
            player.sendMessage("Player not found.");
            return false;
         } else {
            PlayerStats stats = PlayerStats.getPlayerStats(tplayer);
            player.sendMessage("__" + tplayer.getDisplayName() + "'s stats:");

            for(String stat : PlayerStats.StatOrder) {
               Object value = stats.getStat(stat, Object.class);
               if (value instanceof Double) {
                  String var10001 = (String)PlayerStats.StatNames.get(stat);
                  player.sendMessage("|" + var10001 + ": " + Shared.formatNumber((Double)value));
               } else {
                  String var7 = (String)PlayerStats.StatNames.get(stat);
                  player.sendMessage("|" + var7 + ": " + value.toString());
               }
            }

            return true;
         }
      }
   }
}

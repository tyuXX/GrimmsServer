package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Unregister {
   public Unregister() {
   }

   public static boolean subCommand(Player player, String pass) {
      PlayerStats playerStats = PlayerStats.getPlayerStats(player);
      if (((String)playerStats.getStat("pass", String.class)).isEmpty()) {
         player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Nothing to delete.");
         return false;
      } else if (Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString().equals(playerStats.getStat("pass", String.class))) {
         playerStats.resetStat("pass");
         player.sendMessage(String.valueOf(ChatColor.GREEN) + "Deleted registration.");
         GAuthBaseCommand.login(player, false);
         return true;
      } else {
         player.sendMessage(String.valueOf(ChatColor.RED) + "Failed to login.");
         return false;
      }
   }
}

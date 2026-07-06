package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Login {
   public Login() {
   }

   public static boolean subCommand(Player player, String pass) {
      if (GAuthBaseCommand.isLoggedIn(player)) {
         player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Already logged in.");
         return true;
      } else {
         PlayerStats playerStats = PlayerStats.getPlayerStats(player);
         if (Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString().equals(playerStats.getStat("pass", String.class))) {
            GAuthBaseCommand.login(player, true);
            player.sendMessage(String.valueOf(ChatColor.GREEN) + "Logged in.");
            return true;
         } else {
            player.sendMessage(String.valueOf(ChatColor.RED) + "Failed to login.");
            return false;
         }
      }
   }
}

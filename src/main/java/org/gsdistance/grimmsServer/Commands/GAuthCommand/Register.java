package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Register {
   public Register() {
   }

   public static boolean subCommand(Player player, String pass) {
      PlayerStats playerStats = PlayerStats.getPlayerStats(player);
      if (!((String)playerStats.getStat("pass", String.class)).isEmpty()) {
         player.sendMessage(String.valueOf(ChatColor.RED) + "Unregister first.");
         return false;
      } else {
         playerStats.setStat("pass", Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString());
         GAuthBaseCommand.login(player, true);
         player.sendMessage(String.valueOf(ChatColor.GREEN) + "Successfully registered and logged in.");
         return true;
      }
   }
}

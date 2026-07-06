package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Version {
   public Version() {
   }

   public static boolean subCommand(Player player, String[] args) {
      String var10001 = GrimmsServer.instance.getDescription().getPrefix();
      player.sendMessage(var10001 + " GMSv" + GrimmsServer.instance.getDescription().getVersion());
      return true;
   }
}

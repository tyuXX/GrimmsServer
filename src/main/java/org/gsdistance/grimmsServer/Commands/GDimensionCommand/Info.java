package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Info {
   public Info() {
   }

   public static boolean subCommand(Player player, String[] args) {
      if (args.length < 2) {
         player.sendMessage("Usage: /gdim info <worldName>");
         return false;
      } else {
         String worldName = args[1];
         World world = GrimmsServer.instance.getServer().getWorld(worldName);
         if (world == null) {
            player.sendMessage("World '" + worldName + "' does not exist.");
            return false;
         } else {
            player.sendMessage("World Name: " + world.getName());
            player.sendMessage("World Type: " + String.valueOf(world.getWorldType()));
            player.sendMessage("Environment: " + String.valueOf(world.getEnvironment()));
            player.sendMessage("Seed: " + world.getSeed());
            return true;
         }
      }
   }
}

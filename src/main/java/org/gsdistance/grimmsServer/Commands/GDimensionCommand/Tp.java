package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import java.util.Objects;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Tp {
   public Tp() {
   }

   public static boolean subCommand(Player player, String[] args) {
      if (args.length < 2) {
         return false;
      } else {
         Player targetPlayer = player;
         String worldName;
         if (args.length > 3) {
            worldName = args[2];
            Player tmp = player.getServer().getPlayer(args[1]);
            targetPlayer = tmp != null ? tmp : player;
         } else {
            worldName = args[1];
         }

         if (GrimmsServer.instance.getServer().getWorlds().stream().noneMatch((world) -> world.getName().equalsIgnoreCase(worldName))) {
            player.sendMessage("World '" + worldName + "' does not exist.");
            return false;
         } else {
            targetPlayer.teleport(((World)Objects.requireNonNull(GrimmsServer.instance.getServer().getWorld(worldName))).getSpawnLocation());
            targetPlayer.sendMessage("Teleported to world '" + worldName + "'.");
            return true;
         }
      }
   }
}

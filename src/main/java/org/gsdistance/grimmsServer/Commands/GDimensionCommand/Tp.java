package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Objects;

public class Tp {
    public static boolean subCommand(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        String worldName;
        Player targetPlayer = player;
        if(args.length > 3) {
            worldName = args[2];
            Player tmp = player.getServer().getPlayer(args[1]);
            targetPlayer = tmp != null ? tmp : player;
        } else {
            worldName = args[1];
        }

        if (GrimmsServer.instance.getServer().getWorlds().stream().noneMatch(world -> world.getName().equalsIgnoreCase(worldName))) {
            player.sendMessage("World '" + worldName + "' does not exist.");
            return false;
        }

        targetPlayer.teleport(Objects.requireNonNull(GrimmsServer.instance.getServer().getWorld(worldName)).getSpawnLocation());
        targetPlayer.sendMessage("Teleported to world '" + worldName + "'.");
        return true;
    }
}

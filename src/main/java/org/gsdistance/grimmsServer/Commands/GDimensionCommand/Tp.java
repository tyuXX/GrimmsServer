package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Tp {
    public Tp() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
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
                    World world = GrimmsServer.instance.getServer().getWorld(worldName);
                    if (world != null) {
                        targetPlayer.teleport(world.getSpawnLocation());
                    }
                    targetPlayer.sendMessage("Teleported to world '" + worldName + "'.");
                    return true;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

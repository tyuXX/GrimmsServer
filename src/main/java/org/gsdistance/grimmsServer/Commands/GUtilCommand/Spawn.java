package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn {
    public Spawn() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Player target = player;
            if (args.length > 1) {
                if (!sender.hasPermission("grimmsserver.util.admin")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to teleport other players.");
                    return false;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found.");
                    return false;
                }
            }

            target.teleport(target.getWorld().getSpawnLocation());
            target.sendMessage(ChatColor.GREEN + "Teleported to spawn.");
            if (target != player) {
                sender.sendMessage(ChatColor.GREEN + "Teleported " + target.getName() + " to spawn.");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

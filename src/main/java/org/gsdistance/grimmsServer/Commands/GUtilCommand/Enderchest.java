package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Enderchest {
    public Enderchest() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            Player target = player;
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found.");
                    return false;
                }
            }

            Inventory enderchest = target.getEnderChest();
            player.openInventory(enderchest);

            if (target == player) {
                player.sendMessage(ChatColor.GREEN + "Opened your enderchest.");
            } else {
                player.sendMessage(ChatColor.GREEN + "Opened " + target.getName() + "'s enderchest.");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

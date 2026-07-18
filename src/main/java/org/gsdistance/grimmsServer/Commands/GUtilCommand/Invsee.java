package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Invsee {
    public Invsee() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gutil invsee <player>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found.");
            return false;
        }

        Inventory targetInventory = target.getInventory();
        player.openInventory(targetInventory);
        player.sendMessage(ChatColor.GREEN + "Opened " + target.getName() + "'s inventory.");
        return true;
    }
}

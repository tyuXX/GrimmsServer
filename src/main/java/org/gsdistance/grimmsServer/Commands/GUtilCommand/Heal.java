package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal {
    public Heal() {
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

            target.setHealth(target.getMaxHealth());
            target.setFoodLevel(20);
            target.setSaturation(20);
            target.setFireTicks(0);
            target.sendMessage(ChatColor.GREEN + "You have been healed.");
            if (target != player) {
                sender.sendMessage(ChatColor.GREEN + "Healed " + target.getName() + ".");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

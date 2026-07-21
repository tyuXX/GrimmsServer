package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sudo {
    public Sudo() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /gutil sudo <player> <command>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found.");
            return false;
        }

        StringBuilder commandBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            commandBuilder.append(args[i]);
            if (i < args.length - 1) {
                commandBuilder.append(" ");
            }
        }

        String command = commandBuilder.toString();
        
        try {
            boolean success = target.performCommand(command);
            if (success) {
                sender.sendMessage(ChatColor.GREEN + "Made " + target.getName() + " execute: /" + command);
            } else {
                sender.sendMessage(ChatColor.RED + "Failed to make " + target.getName() + " execute: /" + command);
            }
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error executing command: " + e.getMessage());
            return false;
        }
    }
}

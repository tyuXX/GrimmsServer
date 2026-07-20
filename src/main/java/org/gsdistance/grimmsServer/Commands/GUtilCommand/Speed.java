package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed {
    public Speed() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 3) {
                return false;
            }

            String type = args[1].toLowerCase();
            float speed;
            try {
                speed = Float.parseFloat(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid speed value. Must be a number between 1 and 10.");
                return false;
            }

            if (speed < 1 || speed > 10) {
                sender.sendMessage(ChatColor.RED + "Speed must be between 1 and 10.");
                return false;
            }

            // Convert 1-10 to actual Bukkit speed values (default is 0.2 for walk, 0.1 for fly)
            float actualSpeed = (speed - 1) * 0.1f + 0.1f;
            if (type.equals("walk")) {
                player.setWalkSpeed(actualSpeed);
                player.sendMessage(ChatColor.GREEN + "Walk speed set to " + speed + ".");
            } else if (type.equals("fly")) {
                player.setFlySpeed(actualSpeed);
                player.sendMessage(ChatColor.GREEN + "Fly speed set to " + speed + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /gutil speed <walk|fly> <1-10>");
                return false;
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

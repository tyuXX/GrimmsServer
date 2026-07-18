package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly {
    public Fly() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 1) {
                return false;
            } else {
                boolean enable;
                switch (args[0].toLowerCase()) {
                    case "on", "true" -> enable = true;
                    case "off", "false" -> enable = false;
                    default -> {
                        enable = !player.getAllowFlight();
                    }
                }

                player.setAllowFlight(enable);
                player.setFlying(enable);
                player.sendMessage(ChatColor.GREEN + "Fly mode " + (enable ? "enabled" : "disabled") + ".");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

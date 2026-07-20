package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class God {
    public God() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (sender instanceof Player player) {
            if (args.length < 2) {
                return false;
            } else {
                boolean enable;
                switch (args[1].toLowerCase()) {
                    case "on", "true" -> enable = true;
                    case "off", "false" -> enable = false;
                    default -> {
                        enable = player.isInvulnerable();
                    }
                }

                player.setInvulnerable(enable);
                player.sendMessage(ChatColor.GREEN + "God mode " + (enable ? "enabled" : "disabled") + ".");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
    }
}

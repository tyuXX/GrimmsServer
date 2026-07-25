package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly {

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        Player targetPlayer;
        int stateIndex;

        if (args.length >= 2) {
            Player potentialTarget = Bukkit.getPlayer(args[1]);
            if (potentialTarget != null) {
                targetPlayer = potentialTarget;
                stateIndex = 2;
            } else {
                if (sender instanceof Player player) {
                    targetPlayer = player;
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to use this command on yourself.");
                    return false;
                }
                stateIndex = 1;
            }
        } else {
            if (sender instanceof Player player) {
                targetPlayer = player;
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
                return false;
            }
            stateIndex = 1;
        }

        boolean enable;
        if (args.length > stateIndex) {
            switch (args[stateIndex].toLowerCase()) {
                case "on", "true" -> enable = true;
                case "off", "false" -> enable = false;
                default -> {
                    enable = !targetPlayer.getAllowFlight();
                }
            }
        } else {
            enable = !targetPlayer.getAllowFlight();
        }

        targetPlayer.setAllowFlight(enable);
        targetPlayer.setFlying(enable);

        if (targetPlayer == sender) {
            targetPlayer.sendMessage(ChatColor.GREEN + "Fly mode " + (enable ? "enabled" : "disabled") + ".");
        } else {
            targetPlayer.sendMessage(ChatColor.GREEN + "Fly mode " + (enable ? "enabled" : "disabled") + " by " + sender.getName() + ".");
            sender.sendMessage(ChatColor.GREEN + "Fly mode " + (enable ? "enabled" : "disabled") + " for " + targetPlayer.getName() + ".");
        }
        return true;
    }
}

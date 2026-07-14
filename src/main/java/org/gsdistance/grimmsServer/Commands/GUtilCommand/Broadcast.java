package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;

public class Broadcast {
    public Broadcast() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("grimmsserver.util.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gUtil broadcast <message>");
            return false;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }

        String message = messageBuilder.toString();
        String prefix = sender instanceof Player player ? ChatColor.GOLD + "[" + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + "] " : ChatColor.GOLD + "[CONSOLE] ";
        
        Shared.Broadcast(message, prefix);
        
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + "Broadcast sent successfully!");
        }
        
        return true;
    }
}

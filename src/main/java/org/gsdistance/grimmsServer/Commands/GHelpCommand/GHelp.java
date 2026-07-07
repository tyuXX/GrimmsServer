package org.gsdistance.grimmsServer.Commands.GHelpCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Data.HelpStrings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GHelp implements CommandExecutor {
    public GHelp() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.GOLD + "=== Grimm'sServer Commands ===");
            sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/ghelp <command>" + ChatColor.GRAY + " for detailed help on a specific command.");
            sender.sendMessage("");
            
            List<String> commands = new ArrayList<>(HelpStrings.helpStrings.keySet());
            commands.sort(String.CASE_INSENSITIVE_ORDER);
            
            StringBuilder commandList = new StringBuilder(ChatColor.GRAY + "Available commands: ");
            for (int i = 0; i < commands.size(); i++) {
                if (i > 0) {
                    commandList.append(ChatColor.GRAY).append(", ");
                }
                commandList.append(ChatColor.YELLOW).append(commands.get(i));
            }
            sender.sendMessage(commandList.toString());
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "Total commands: " + ChatColor.GREEN + commands.size());
        } else {
            String commandName = args[0].toLowerCase();
            String helpString = HelpStrings.getHelpString(commandName);
            String helpUsage = HelpStrings.getHelpUsage(commandName);
            
            if (helpString.equals("Not found.") && helpUsage.equals("Not found.")) {
                sender.sendMessage(ChatColor.RED + "Command '" + commandName + "' not found.");
                sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/ghelp" + ChatColor.GRAY + " to see all available commands.");
                return true;
            }
            
            sender.sendMessage(ChatColor.GOLD + "=== Help for /" + commandName + " ===");
            sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.WHITE + helpString);
            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.YELLOW + helpUsage);
        }
        return true;
    }
}

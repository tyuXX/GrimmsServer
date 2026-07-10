package org.gsdistance.grimmsServer.Commands.GLevelCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GLevelBaseCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /gLevel <prestige>");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "prestige":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }
                return Prestige.subCommand((Player) sender);
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /gLevel <prestige>");
                return false;
        }
    }
}

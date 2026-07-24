package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobBaseCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                return false;
            } else {
                return switch (args[0].toLowerCase()) {
                    case "log" -> Log.subCommand(player, args);
                    case "take" -> Take.subCommand(player, args);
                    case "buyedu" -> BuyEdu.subCommand(player);
                    default -> false;
                };
            }
        } else {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }
}

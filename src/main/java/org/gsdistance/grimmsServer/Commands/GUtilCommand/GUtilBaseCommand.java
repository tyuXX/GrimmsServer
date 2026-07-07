package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUtilBaseCommand implements CommandExecutor {
    public GUtilBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        } else if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "version" -> {
                    return Version.subCommand(player, args);
                }
                case "relic" -> {
                    return Relic.subCommand(player, args);
                }
                case "capability" -> {
                    return Capability.subCommand(player, args);
                }
                case "setting" -> {
                    return Setting.subCommand(player, args);
                }
                default -> {
                    return false;
                }
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}

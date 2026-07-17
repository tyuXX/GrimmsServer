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
        } else {
            switch (args[0].toLowerCase()) {
                case "version" -> {
                    return Version.subCommand(sender, args);
                }
                case "relic" -> {
                    if (sender instanceof Player player) {
                        return Relic.subCommand(player, args);
                    }
                    return false;
                }
                case "capability" -> {
                    if (sender instanceof Player player) {
                        return Capability.subCommand(player, args);
                    }
                    return false;
                }
                case "setting" -> {
                    if (sender instanceof Player player) {
                        return Setting.subCommand(player, args);
                    }
                    return false;
                }
                case "broadcast" -> {
                    return Broadcast.subCommand(sender, args);
                }
                case "inventoryrestore" -> {
                    if (sender instanceof Player player) {
                        return InventoryRestore.subCommand(player, args);
                    }
                    return false;
                }
                default -> {
                    return false;
                }
            }
        }
    }
}

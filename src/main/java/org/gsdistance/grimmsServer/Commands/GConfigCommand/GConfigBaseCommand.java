package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GConfigBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }
        return switch (args[0].toLowerCase()) {
            case "dump" -> Dump.subCommand((Player) sender);
            case "reload" -> Reload.subCommand();
            default -> false;
        };
    }
}

package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GDimBaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        return switch (args[0].toLowerCase()) {
            case "create" -> Create.subCommand(player, args);
            case "delete" -> Delete.subCommand(player, args);
            case "tp" -> Tp.subCommand(player, args);
            case "list" -> List.subCommand(player);
            case "info" -> Info.subCommand(player, args);
            default -> false;
        };
    }
}

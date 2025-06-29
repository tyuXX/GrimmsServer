package org.gsdistance.grimmsServer.Commands.GFactionCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GFactionBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }
        return switch (args[0].toLowerCase()) {
            case "invite" -> Invite.subCommand(player, args);
            case "modify" -> Modify.subCommand(player, args);
            case "join" -> Join.subCommand(player, args);
            case "leave" -> Leave.subCommand(player, args);
            case "kick" -> Kick.subCommand(player, args);
            case "info" -> Info.subCommand(player);
            case "new" -> New.subCommand(player, args);
            default -> false;
        };
    }
}

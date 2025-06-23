package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.HomeCommand.DelHome;
import org.gsdistance.grimmsServer.Commands.HomeCommand.Homes;
import org.gsdistance.grimmsServer.Commands.HomeCommand.Sethome;
import org.gsdistance.grimmsServer.Commands.HomeCommand.TpHome;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class GLogBaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        String sub = args[0].toLowerCase();
        return switch (sub) {
            case "self_stats" -> LogSelfStats.subCommand(player);
            case "other_stats" -> LogPlayerStats.subCommand(player, args);
            case "self_titles" -> LogSelfTitles.subCommand(player);
            case "other_titles" -> LogPlayerTitles.subCommand(player, args);
            case "world" -> LogWorldStats.subCommand(player);
            case "leaderboard" -> LogLeaderboard.subCommand(player);
            case "commands" -> player.performCommand("grimmsserver:grimmsServerCommands");
            default -> false;
        };
    }
}

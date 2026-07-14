package org.gsdistance.grimmsServer.Commands.GStatsCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GStatsBaseCommand implements CommandExecutor {
    public GStatsBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                return false;
            } else {
                boolean var10000;
                switch (args[0].toLowerCase()) {
                    case "self_history":
                        var10000 = SelfHistory.subCommand(player, args);
                        break;
                    case "others_history":
                        var10000 = OthersHistory.subCommand(player, args);
                        break;
                    case "commands":
                        var10000 = player.performCommand("grimmsserver:grimmsServerCommands");
                        break;
                    default:
                        var10000 = false;
                }

                return var10000;
            }
        } else {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }
}

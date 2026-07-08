package org.gsdistance.grimmsServer.Commands.GDecoCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GDecoBaseCommand implements CommandExecutor {
    public GDecoBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        } else if (sender instanceof Player player) {
            return switch (args[0].toLowerCase()) {
                case "selecttitle" -> {
                    if (args.length < 2) {
                        yield false;
                    }

                    yield SelectTitle.subCommand(player, args[1]);
                }
                case "cleartitle" -> SelectTitle.subCommand(player, "");
                default -> false;
            };
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}

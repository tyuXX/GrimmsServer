package org.gsdistance.grimmsServer.Commands.GDimensionCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GDimBaseCommand implements CommandExecutor {
    public GDimBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            boolean var10000;
            switch (args[0].toLowerCase()) {
                case "create" -> var10000 = Create.subCommand(sender, args);
                case "delete" -> var10000 = Delete.subCommand(sender, args);
                case "tp" -> var10000 = Tp.subCommand(sender, args);
                case "list" -> var10000 = List.subCommand(sender);
                case "info" -> var10000 = Info.subCommand(sender, args);
                default -> var10000 = false;
            }

            return var10000;
        }
    }
}

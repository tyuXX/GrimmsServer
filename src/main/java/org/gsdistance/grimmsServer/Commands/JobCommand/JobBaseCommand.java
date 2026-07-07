package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobBaseCommand implements CommandExecutor {
    public JobBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                return false;
            } else {
                boolean var10000;
                switch (args[0].toLowerCase()) {
                    case "log" -> var10000 = Log.subCommand(player, args);
                    case "take" -> var10000 = Take.subCommand(player, args);
                    case "buyedu" -> var10000 = BuyEdu.subCommand(player);
                    default -> var10000 = false;
                }

                return var10000;
            }
        } else {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }
}

package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeBaseCommand implements CommandExecutor {
    public HomeBaseCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                return false;
            } else {
                boolean var10000;
                switch (args[0].toLowerCase()) {
                    case "sethome" -> var10000 = Sethome.SubCommand(player, args);
                    case "tp" -> var10000 = TpHome.SubCommand(player, args);
                    case "homes" -> var10000 = Homes.SubCommand(player, args);
                    case "delhome" -> var10000 = DelHome.SubCommand(player, args);
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

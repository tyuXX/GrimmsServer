package org.gsdistance.grimmsServer.Commands.HomeCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeBaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        String sub = args[0].toLowerCase();
        return switch (sub) {
            case "sethome" -> Sethome.SubCommand(player, args);
            case "tp" -> TpHome.SubCommand(player, args);
            case "homes" -> Homes.SubCommand(player, args);
            case "delhome" -> DelHome.SubCommand(player, args);
            default -> false;
        };
    }
}
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
            player.sendMessage("Usage: /home <sethome|tp|homes|delhome> [name]");
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "sethome":
                return Sethome.SubCommand(player, args);
            case "tp":
                return TpHome.SubCommand(player, args);
            case "homes":
                return Homes.SubCommand(player, args);
            case "delhome":
                return DelHome.SubCommand(player, args);
            default:
                player.sendMessage("Unknown subcommand. Use /home <sethome|tp|homes|delhome> [name]");
                return true;
        }
    }
}
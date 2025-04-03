package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class ExecutePlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage("Usage /executePlayer <player>");
                return false;
            }
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            }
            if (PlayerTitles.getPlayerTitles((Player) sender).hasTitle("Executioner")) {
                player.sendMessage("You have been executed by " + ((Player) sender).getDisplayName());
                sender.sendMessage("You have executed " + player.getDisplayName());
                player.damage(player.getHealth() * 10);
            }
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}

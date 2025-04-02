package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class LogPlayerStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage("Please specify a player name.");
                return false;
            }
            Player player = GrimmsServer.instance.getServer().getPlayer(args[0]);
            PlayerStats stats = PlayerStats.getPlayerStats(player);
            sender.sendMessage("__" + player.getDisplayName() + "'s stats:");
            for (String stat : PlayerStats.StatOrder) {
                Object value = stats.getStat(stat);
                if (value instanceof Double) {
                    sender.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + Math.round((Double) value));
                } else {
                    sender.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + value.toString());
                }
            }

            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}

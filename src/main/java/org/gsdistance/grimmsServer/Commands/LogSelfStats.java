package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.Enumeration;

public class LogSelfStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            PlayerStats stats = PlayerStats.getPlayerStats(player);
            for (String stat : PlayerStats.StatOrder) {
                Object value = stats.getStat(stat);
                if (value instanceof Double) {
                    player.sendMessage(PlayerStats.StatNames.get(stat) + ": " + Math.round((Double) value));
                } else {
                    player.sendMessage(PlayerStats.StatNames.get(stat) + ": " + value.toString());
                }
            }

            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}

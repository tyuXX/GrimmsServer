package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.PlayerStats;

import java.util.Enumeration;

public class LogSelfStatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerStats stats = PlayerStats.getPlayerStats(player);
            for (Enumeration<String> keys = PlayerStats.Stats.keys(); keys.hasMoreElements();) {
                String stat = keys.nextElement();
                Object value = stats.getStat(stat, PlayerStats.Stats.get(stat));
                player.sendMessage(stat + ": " + value.toString());
            }

            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}

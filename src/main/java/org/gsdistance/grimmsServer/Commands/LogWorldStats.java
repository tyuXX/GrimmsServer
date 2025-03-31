package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.Enumeration;

public class LogWorldStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            WorldStats stats = WorldStats.getWorldStats(((Player) sender).getWorld());
            for (Enumeration<String> keys = WorldStats.Stats.keys(); keys.hasMoreElements();) {
                String stat = keys.nextElement();
                Object value = stats.getStat(stat);
                sender.sendMessage(WorldStats.StatNames.get(stat) + ": " + value.toString());
            }

            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
    }
}

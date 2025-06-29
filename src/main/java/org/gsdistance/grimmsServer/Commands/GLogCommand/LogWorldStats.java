package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.Enumeration;

public class LogWorldStats {
    public static boolean subCommand(Player player) {
        WorldStats stats = WorldStats.getWorldStats((player).getWorld());
        player.sendMessage("__World stats:");
        for (Enumeration<String> keys = WorldStats.Stats.keys(); keys.hasMoreElements(); ) {
            String stat = keys.nextElement();
            Object value = stats.getStat(stat);
            player.sendMessage("|" + WorldStats.StatNames.get(stat) + ": " + value.toString());
        }
        return true;
    }
}

package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import static org.gsdistance.grimmsServer.Shared.formatNumber;

public class LogSelfStats {
    public static boolean subCommand(Player player) {
        if (!player.hasPermission("grimmsserver.log.self")) {
            return false;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(player);
        player.sendMessage("__Your stats:");
        for (String stat : PlayerStats.StatOrder) {
            Object value = stats.getStat(stat, Object.class);
            if (value instanceof Double) {
                player.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + formatNumber((Double) value));
            } else {
                player.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + value.toString());
            }
        }
        return true;
    }
}

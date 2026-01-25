package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.ArrayList;

import static org.gsdistance.grimmsServer.Shared.formatNumber;

public class LogSelfStats {
    public static boolean subCommand(Player player) {
        if (!player.hasPermission("grimmsserver.log.self")) {
            return false;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(player);
        ArrayList<String> statsList = new ArrayList<>();
        statsList.add("__Your stats:");
        for (String stat : PlayerStats.StatOrder) {
            Object value = stats.getStat(stat, Object.class);
            if (value instanceof Double) {
                statsList.add("|" + PlayerStats.StatNames.get(stat) + ": " + formatNumber((Double) value));
            } else {
                statsList.add("|" + PlayerStats.StatNames.get(stat) + ": " + value.toString());
            }
        }
        GeneralChatHandler.sendArray(player, statsList.toArray(new String[0]));
        return true;
    }
}

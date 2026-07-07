package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.ArrayList;

public class LogSelfStats {
    public LogSelfStats() {
    }

    public static boolean subCommand(Player player) {
        if (!player.hasPermission("grimmsserver.log.self")) {
            return false;
        } else {
            PlayerStats stats = PlayerStats.getPlayerStats(player);
            ArrayList<String> statsList = new ArrayList();
            statsList.add("__Your stats:");

            for (String stat : PlayerStats.StatOrder) {
                Object value = stats.getStat(stat, Object.class);
                if (value instanceof Double) {
                    String var10001 = PlayerStats.StatNames.get(stat);
                    statsList.add("|" + var10001 + ": " + Shared.formatNumber((Double) value));
                } else {
                    String var6 = PlayerStats.StatNames.get(stat);
                    statsList.add("|" + var6 + ": " + value.toString());
                }
            }

            GeneralChatHandler.sendArray(player, statsList.toArray(new String[0]));
            return true;
        }
    }
}

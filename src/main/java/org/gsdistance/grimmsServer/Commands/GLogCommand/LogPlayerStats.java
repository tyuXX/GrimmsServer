package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.UUID;

public class LogPlayerStats {
    public LogPlayerStats() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.log.other")) {
            return false;
        } else if (args.length < 2) {
            player.sendMessage("Please specify a player name.");
            return false;
        } else {
            Player tplayer = GrimmsServer.instance.getServer().getPlayer(args[1]);
            PlayerStats stats;
            String displayName;

            if (tplayer != null) {
                stats = PlayerStats.getPlayerStats(tplayer);
                displayName = tplayer.getDisplayName();
            } else {
                UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                stats = PlayerStats.getOfflinePlayerStats(uuid);
                if (stats == null) {
                    player.sendMessage("Player not found.");
                    return false;
                }
                displayName = args[1];
            }

            player.sendMessage("__" + displayName + "'s stats:");

            for (String stat : PlayerStats.StatOrder) {
                Object value = stats.getStat(stat, Object.class);
                if (value instanceof Double) {
                    String var10001 = PlayerStats.StatNames.get(stat);
                    player.sendMessage("|" + var10001 + ": " + Shared.formatNumber((Double) value));
                } else {
                    String var7 = PlayerStats.StatNames.get(stat);
                    player.sendMessage("|" + var7 + ": " + value.toString());
                }
            }

            return true;
        }
    }
}

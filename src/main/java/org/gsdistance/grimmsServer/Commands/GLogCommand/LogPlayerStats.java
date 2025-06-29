package org.gsdistance.grimmsServer.Commands.GLogCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import static org.gsdistance.grimmsServer.Shared.formatNumber;

public class LogPlayerStats {
    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.log.other")) {
            return false;
        }
        if (args.length < 2) {
            player.sendMessage("Please specify a player name.");
            return false;
        }
        Player tplayer = GrimmsServer.instance.getServer().getPlayer(args[1]);
        if (tplayer == null) {
            player.sendMessage("Player not found.");
            return false;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(tplayer);
        player.sendMessage("__" + tplayer.getDisplayName() + "'s stats:");
        for (String stat : PlayerStats.StatOrder) {
            Object value = stats.getStat(stat);
            if (value instanceof Double) {
                player.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + formatNumber((Double) value));
            } else {
                player.sendMessage("|" + PlayerStats.StatNames.get(stat) + ": " + value.toString());
            }
        }
        return true;
    }
}

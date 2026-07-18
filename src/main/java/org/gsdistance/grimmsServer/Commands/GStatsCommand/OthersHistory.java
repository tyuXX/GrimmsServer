package org.gsdistance.grimmsServer.Commands.GStatsCommand;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.HistoricalStatsSnapshot;
import org.gsdistance.grimmsServer.Stats.PlayerHistoricalStats;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.List;
import java.util.UUID;

public class OthersHistory {
    public OthersHistory() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.stats.other")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to view other players' history.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /gStats others_history <player> [timeRange] [stat]");
            return true;
        }

        String targetPlayerName = args[1];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);

        if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player '" + targetPlayerName + "' not found.");
            return true;
        }

        UUID targetUUID = targetPlayer.getUniqueId();
        PlayerHistoricalStats historicalStats = PlayerHistoricalStats.getPlayerHistoricalStats(targetUUID);

        if (historicalStats.allTimeSnapshots.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No historical data available for " + targetPlayerName + ".");
            player.sendMessage(ChatColor.YELLOW + "Data is recorded when players join, quit, and hourly while online.");
            return true;
        }

        String timeRange = "all";
        String specificStat = null;
        boolean largeGraph = false;

        if (args.length >= 3) {
            timeRange = args[2].toLowerCase();
        }
        if (args.length >= 4) {
            String arg = args[3].toLowerCase();
            if (arg.equals("largegraph")) {
                largeGraph = true;
            } else {
                specificStat = arg;
            }
        }
        if (args.length >= 5) {
            String arg = args[4].toLowerCase();
            if (arg.equals("largegraph")) {
                largeGraph = true;
            } else if (specificStat == null) {
                specificStat = arg;
            }
        }

        List<HistoricalStatsSnapshot> snapshots = SelfHistory.filterSnapshotsByTime(historicalStats.allTimeSnapshots, timeRange);

        if (snapshots.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No data available for the specified time range.");
            return true;
        }

        player.sendMessage(ChatColor.GOLD + "=== " + targetPlayerName + "'s Stats History ===");

        if (specificStat != null && PlayerStats.Stats.get(specificStat) != null) {
            SelfHistory.displaySingleStatGraph(player, snapshots, specificStat, timeRange, largeGraph);
        } else {
            SelfHistory.displayAllStatsGraph(player, snapshots, timeRange, largeGraph);
        }

        return true;
    }
}

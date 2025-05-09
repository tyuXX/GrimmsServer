package org.gsdistance.grimmsServer.Events;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class ServerTickEvent {
    public static long ticks = 0;

    public static void Event() {
        ticks++;
        if (ticks % 1000 == 0) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStatLeaderBoard.getPlayerStatLeaderBoard().checkPlayer(player);
                PlayerTitleManager.checkForMoney(player);
                PlayerTitleManager.checkTitles(player);
                PlayerTitleManager.checkForBlockBreaks(player);
                PlayerTitleManager.checkForTotalKills(player);
            }
        }
        if (ticks % 24000 == 0) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                String jobTitleId = (String) playerStats.getStat("jobTitle");
                if (jobTitleId != null && !jobTitleId.isEmpty()) {
                    double payCheck = JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(jobTitleId, null).paycheckSize;
                    playerStats.setStat("money", (Double) playerStats.getStat("money") + payCheck);
                    player.sendMessage("You have received your paycheck: " + payCheck);
                }
            }
        }
    }
}

package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class ServerTickEvent {
    public static long ticks = 0;

    public static void Event() {
        for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
            PlayerTickEvent.Event(player);
        }
        PlayerTickEvent.processMagnets();
        ticks++;
        //Player tick
        if (ticks % 1000 == 0) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStatLeaderBoard.getPlayerStatLeaderBoard().checkPlayer(player);
                PlayerTitleChecker.checkForMoney(player);
                PlayerTitleChecker.checkTitles(player);
                PlayerTitleChecker.checkForBlockBreaks(player);
                PlayerTitleChecker.checkForTotalKills(player);
                // Check for auth
                if(!GAuthBaseCommand.isLoggedIn(player)) {
                    player.kickPlayer("Not logged in for too long.");
                }
            }
        }
        // Paycheck
        if (ticks % 24000 == 0) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                String jobTitleId = playerStats.getStat("jobTitle", String.class);
                if (jobTitleId != null && !jobTitleId.isEmpty()) {
                    double multiplier = 1 + (Math.pow(playerStats.getStat("level", Integer.class), 2)) / 100;
                    double payCheck = Math.ceil(JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(jobTitleId, null).paycheckSize() * multiplier);
                    playerStats.setStat("money", playerStats.getStat("money", Double.class) + payCheck);
                    player.sendMessage("You have received your paycheck: " + Shared.formatNumber(payCheck));
                }
            }
        }
    }
}

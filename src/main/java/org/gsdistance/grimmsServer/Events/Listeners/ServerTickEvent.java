package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.JobTitle;
import org.gsdistance.grimmsServer.Constructable.Market;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Data.Player.PlayerTitleChecker;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class ServerTickEvent {
    public static long ticks = 0L;

    public ServerTickEvent() {
    }

    public static void Event() {
        for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
            PlayerTickEvent.Event(player);
        }

        PlayerTickEvent.processMagnets();
        ++ticks;
        if (ticks % 1000L == 0L) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStatLeaderBoard.getPlayerStatLeaderBoard().checkPlayer(player);
                PlayerTitleChecker.checkForMoney(player);
                PlayerTitleChecker.checkTitles(player);
                PlayerTitleChecker.checkForBlockBreaks(player);
                PlayerTitleChecker.checkForTotalKills(player);
                if (!GAuthBaseCommand.isLoggedIn(player)) {
                    player.kickPlayer("Not logged in for too long.");
                }
            }

            Market market = Market.getMarket();
            market.reCalcNegMarketSaturation();
            market.saveMarket();
        }

        if (ticks % 24000L == 0L) {
            for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                String jobTitleId = playerStats.getStat("jobTitle", String.class);
                if (jobTitleId != null && !jobTitleId.isEmpty()) {
                    double multiplier = (double) 1.0F + Math.pow((double) playerStats.getStat("level", Integer.class), 2.0F) / (double) 100.0F;
                    double payCheck = Math.ceil(JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(jobTitleId, null).paycheckSize() * multiplier);
                    playerStats.setStat("money", playerStats.getStat("money", Double.class) + payCheck);
                    player.sendMessage("You have received your paycheck: " + Shared.formatNumber(payCheck));
                }
            }
        }

    }
}

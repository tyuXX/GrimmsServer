package org.gsdistance.grimmsServer.Events;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Commands.CommandRegistry;
import org.gsdistance.grimmsServer.Data.JobTitlesBaseValues;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStatLeaderBoard;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.Objects;
import java.util.logging.Level;

public class ServerStartupEvent {
    public static int ticks = 0;

    public static void Event() {
        CommandRegistry.registerCommands();
        GrimmsServer.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GrimmsServer.instance, () -> {
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
            if(ticks % 24000 == 0){
                for (Player player : GrimmsServer.instance.getServer().getOnlinePlayers()) {
                    PlayerStats playerStats = PlayerStats.getPlayerStats(player);
                    String jobTitleId = (String) playerStats.getStat("jobTitle");
                    double payCheck = JobTitlesBaseValues.jobTitleBaseValues.getOrDefault(jobTitleId, null).paycheckSize;
                    if(!Objects.equals(jobTitleId, "") && jobTitleId != null){
                        playerStats.setStat("money", (Double)playerStats.getStat("money") + payCheck);
                    }
                    player.sendMessage("You have received your paycheck: " + payCheck);
                }
            }
        }, 100L, 1L);
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully.");
    }
}

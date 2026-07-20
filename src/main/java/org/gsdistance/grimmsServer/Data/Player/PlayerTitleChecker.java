package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;
import org.gsdistance.grimmsServer.Stats.TitleCriteria;
import org.gsdistance.grimmsServer.Stats.TitleGenerator;

import java.util.List;

public class PlayerTitleChecker {
    private static final long CHECK_INTERVAL_TICKS = 600; // Check every 30 seconds (20 ticks = 1 second)
    private static int taskId = -1;

    public PlayerTitleChecker() {
    }

    // Start periodic title checking
    public static void startPeriodicChecking() {
        if (taskId == -1) {
            taskId = Bukkit.getScheduler().runTaskTimer(GrimmsServer.instance, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkAllTitles(player);
                }
            }, CHECK_INTERVAL_TICKS, CHECK_INTERVAL_TICKS).getTaskId();
        }
    }

    // Stop periodic title checking
    public static void stopPeriodicChecking() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    // Manual title awards (not auto-checked)
    public static void awardTitle(Player player, String titleId) {
        PlayerTitles.getPlayerTitles(player).addTitle(titleId);
    }

    // Event-specific triggers that increment stats only
    public static void killedPlayer(Player player) {
        PlayerStats.getPlayerStats(player).changeStat("total_kill_count", 1);
    }

    public static void gotKilledByPlayer(Player player) {
        PlayerStats.getPlayerStats(player).changeStat("death_count", 1);
    }

    public static void killedDragon(Player player) {
        // Dragon kills not tracked in persistent stats
    }

    public static void gotOnLeaderboard(Player player) {
        // Leaderboard status not tracked in persistent stats
    }

    public static void joinedGame(Player player) {
        checkAllTitles(player);
    }

    private static void checkAllTitles(Player player) {
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        
        // Check static criteria titles
        for (TitleCriteria criteria : TitleCriteria.values()) {
            if (!playerTitles.hasTitle(criteria.getTitleId()) && criteria.test(player)) {
                playerTitles.addTitle(criteria.getTitleId());
            }
        }
        
        // Check dynamic money titles (all qualifying titles like achievements)
        double money = playerStats.getStat("money", Double.class);
        List<String> moneyTitles = TitleGenerator.getMoneyTitles(money);
        for (String title : moneyTitles) {
            if (!playerTitles.hasTitle(title)) {
                playerTitles.addTitle(title);
            }
        }
        
        // Check dynamic block break titles (all qualifying titles like achievements)
        long blockBreaks = playerStats.getStat("block_break_count", Long.class);
        List<String> blockBreakTitles = TitleGenerator.getBlockBreakTitles(blockBreaks);
        for (String title : blockBreakTitles) {
            if (!playerTitles.hasTitle(title)) {
                playerTitles.addTitle(title);
            }
        }
        
        // Check dynamic kill titles (all qualifying titles like achievements)
        long totalKills = getTotalKills(player);
        List<String> killTitles = TitleGenerator.getKillTitles(totalKills);
        for (String title : killTitles) {
            if (!playerTitles.hasTitle(title)) {
                playerTitles.addTitle(title);
            }
        }
    }
    
    private static long getTotalKills(Player player) {
        Object totalKillsObj = PlayerStats.getPlayerStats(player).getStat("total_kill_count", Object.class);
        if (totalKillsObj instanceof Integer) {
            return ((Integer) totalKillsObj).longValue();
        } else if (totalKillsObj instanceof Long) {
            return (Long) totalKillsObj;
        }
        return 0;
    }

    // Legacy method for backward compatibility
    public static void checkTitles(Player player) {
        checkAllTitles(player);
    }

    // Legacy methods for backward compatibility
    public static void checkForMoney(Player player) {
        checkAllTitles(player);
    }

    public static void checkForBlockBreaks(Player player) {
        checkAllTitles(player);
    }

    public static void checkForTotalKills(Player player) {
        checkAllTitles(player);
    }
}

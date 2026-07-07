package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class PlayerTitleChecker {
    public PlayerTitleChecker() {
    }

    public static void killedPlayer(Player player) {
        PlayerTitles.getPlayerTitles(player).addTitle("Murderer");
    }

    public static void gotKilledByPlayer(Player player) {
        PlayerTitles.getPlayerTitles(player).addTitle("Victim");
    }

    public static void killedDragon(Player player) {
        PlayerTitles.getPlayerTitles(player).addTitle("DragonSlayer");
    }

    public static void gotOnLeaderboard(Player player) {
        PlayerTitles.getPlayerTitles(player).addTitle("Leader");
    }

    public static void joinedGame(Player player) {
        PlayerTitles.getPlayerTitles(player).addTitle("Newbie");
    }

    public static void checkTitles(Player player) {
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (playerTitles.getTitles().length > 5) {
            playerTitles.addTitle("Titlemaxxer");
        }

    }

    public static void checkForMoney(Player player) {
        double money = PlayerStats.getPlayerStats(player).getStat("money", Double.class);
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (money > (double) 100000.0F) {
            playerTitles.addTitle("Richie");
        }

        if (money > (double) 1000000.0F) {
            playerTitles.addTitle("Millionaire");
        }

        if (money > (double) 1.0E9F) {
            playerTitles.addTitle("Billionaire");
        }

        if (money > 1.0E12) {
            playerTitles.addTitle("Trillionaire");
        }

        if (money > 1.0E15) {
            playerTitles.addTitle("Trillionaire");
        }

    }

    public static void checkForBlockBreaks(Player player) {
        long blockBreakCount = PlayerStats.getPlayerStats(player).getStat("block_break_count", Long.class);
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (blockBreakCount > 1000L) {
            playerTitles.addTitle("Miner");
        }

        if (blockBreakCount > 10000L) {
            playerTitles.addTitle("Anti-block");
        }

        if (blockBreakCount > 100000L) {
            playerTitles.addTitle("UltimateMiner");
        }

    }

    public static void checkForTotalKills(Player player) {
        Object totalKillsObj = PlayerStats.getPlayerStats(player).getStat("total_kill_count", Object.class);
        long totalKills;
        if (totalKillsObj instanceof Integer) {
            totalKills = ((Integer) totalKillsObj).longValue();
        } else {
            totalKills = (Long) totalKillsObj;
        }

        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (totalKills > 100L) {
            playerTitles.addTitle("KillingMachine");
        }

        if (totalKills > 1000L) {
            playerTitles.addTitle("ProGamer");
        }

        if (totalKills > 10000L) {
            playerTitles.addTitle("Hitman");
        }

    }
}

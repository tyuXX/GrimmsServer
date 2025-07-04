package org.gsdistance.grimmsServer.Data.Player;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class PlayerTitleChecker {
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
        if (money > 100000.0) {
            playerTitles.addTitle("Richie");
        }
        if (money > 1000000.0) {
            playerTitles.addTitle("Millionaire");
        }
        if (money > 1000000000.0) {
            playerTitles.addTitle("Billionaire");
        }
        if (money > 1000000000000.0) {
            playerTitles.addTitle("Trillionaire");
        }
    }

    public static void checkForBlockBreaks(Player player) {
        long blockBreakCount = PlayerStats.getPlayerStats(player).getStat("block_break_count", Long.class);
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (blockBreakCount > 1000) {
            playerTitles.addTitle("Miner");
        }
        if (blockBreakCount > 10000) {
            playerTitles.addTitle("Anti-block");
        }
        if (blockBreakCount > 100000) {
            playerTitles.addTitle("UltimateMiner");
        }
    }

    public static void checkForTotalKills(Player player) {
        // TODO - This is a workaround for the fact that the total_kill_count is wongly stored as an Integer in the player for now.
        Object totalKillsObj = PlayerStats.getPlayerStats(player).getStat("total_kill_count", Object.class);
        long totalKills;
        if (totalKillsObj instanceof Integer) {
            totalKills = ((Integer) totalKillsObj).longValue();
        } else {
            totalKills = (Long) totalKillsObj;
        }
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if (totalKills > 100) {
            playerTitles.addTitle("KillingMachine");
        }
        if (totalKills > 1000) {
            playerTitles.addTitle("ProGamer");
        }
        if (totalKills > 10000) {
            playerTitles.addTitle("Hitman");
        }
    }
}

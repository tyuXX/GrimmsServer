package org.gsdistance.grimmsServer.Data;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.PlayerTitles;

public class PlayerTitleManager {
    public static void killedPlayer(Player player){
        PlayerTitles.getPlayerTitles(player).addTitle("Murderer");
    }
    public static void gotKilledByPlayer(Player player){
        PlayerTitles.getPlayerTitles(player).addTitle("Victim");
    }
    public static void killedDragon(Player player){
        PlayerTitles.getPlayerTitles(player).addTitle("DragonSlayer");
    }
    public static void gotOnLeaderboard(Player player){
        PlayerTitles.getPlayerTitles(player).addTitle("Leader");
    }
    public static void joinedGame(Player player){
        PlayerTitles.getPlayerTitles(player).addTitle("Newbie");
    }
    public static void checkTitles(Player player){
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if(playerTitles.getTitles().length > 5){
            playerTitles.addTitle("Titlemaxxer");
        }
    }
    public static void checkForMoney(Player player){
        double money = (double) PlayerStats.getPlayerStats(player).getStat("money");
        PlayerTitles playerTitles = PlayerTitles.getPlayerTitles(player);
        if(money >= 1000000.0){
            playerTitles.addTitle("Millionaire");
        }
        if(money >= 1000000000.0){
            playerTitles.addTitle("Billionaire");
        }
        if(money >= 1000000000000.0){
            playerTitles.addTitle("Trillionaire");
        }
    }
}

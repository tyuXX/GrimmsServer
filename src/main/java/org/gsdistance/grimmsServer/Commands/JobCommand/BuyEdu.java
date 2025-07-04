package org.gsdistance.grimmsServer.Commands.JobCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class BuyEdu {
    public static boolean subCommand(Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        int intelligence = playerStats.getStat("intelligence", Integer.class);
        double cost = Math.pow(intelligence, 2) * 25;
        double money = playerStats.getStat("money", Double.class);
        if (money < cost) {
            player.sendMessage("You do not have enough money to buy this education. You need " + Shared.formatNumber(cost) + " but you only have " + Shared.formatNumber(money) + ".");
            return false;
        }
        playerStats.setStat("money", money - cost);
        playerStats.changeStat("intelligence", 1);
        player.sendMessage("You have bought an education for " + Shared.formatNumber(cost) + ". Your intelligence has been increased by 1.");
        return true;
    }
}

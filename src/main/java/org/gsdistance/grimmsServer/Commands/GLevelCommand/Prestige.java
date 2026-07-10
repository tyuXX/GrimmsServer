package org.gsdistance.grimmsServer.Commands.GLevelCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Manage.GeneralChatHandler;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

public class Prestige {
    public static boolean subCommand(Player player) {
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        PlayerLevelHandler levelHandler = PlayerLevelHandler.getLevelHandler(player);

        int currentLevel = levelHandler.getLevel();
        int currentPrestige = playerStats.getStat("prestige", Integer.class);
        long requiredLevel = 15 + (currentPrestige) * 10L;

        if (currentLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "You must be at least level " + requiredLevel + " to prestige.");
            return false;
        }

        int newPrestige = currentPrestige + 1;
        long currentPrestigePoints = playerStats.getStat("prestigePoints", Long.class);
        
        playerStats.setStat("prestige", newPrestige);
        playerStats.setStat("prestigePoints", currentPrestigePoints + newPrestige);
        playerStats.setStat("level", 0);
        playerStats.setStat("xp", 0.0);
        playerStats.setStat("xp_required", levelHandler.getXpToLevel());

        player.sendMessage(ChatColor.GREEN + "You have prestiged to prestige " + newPrestige + "!");
        player.sendMessage(ChatColor.GOLD + "You received " + newPrestige + " prestige points. Total: " + (currentPrestigePoints + newPrestige));
        Shared.Broadcast(ChatColor.DARK_PURPLE + "[" + player.getName() + "] has prestiged to " + newPrestige + "!", null);

        return true;
    }
}

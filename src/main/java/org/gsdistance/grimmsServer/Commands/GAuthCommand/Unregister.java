package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.nio.charset.StandardCharsets;

public class Unregister {
    public static boolean subCommand(Player player, String pass){
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        if(playerStats.getStat("pass", String.class).isEmpty()){
            player.sendMessage(ChatColor.YELLOW + "Nothing to delete.");
            return false;
        }
        if(Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString().equals(playerStats.getStat("pass", String.class))){
            playerStats.resetStat("pass");
            player.sendMessage(ChatColor.GREEN + "Deleted registration.");
            GAuthBaseCommand.login(player, false);
            return true;
        }
        player.sendMessage(ChatColor.RED + "Failed to login.");
        return false;
    }
}

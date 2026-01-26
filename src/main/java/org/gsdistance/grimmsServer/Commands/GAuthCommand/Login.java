package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.nio.charset.StandardCharsets;

public class Login {
    public static boolean subCommand(Player player, String pass){
        if(GAuthBaseCommand.isLoggedIn(player)){
            player.sendMessage(ChatColor.YELLOW + "Already logged in.");
            return  true;
        }
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        if(Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString().equals(playerStats.getStat("pass", String.class))){
            GAuthBaseCommand.login(player,true);
            player.sendMessage(ChatColor.GREEN + "Logged in.");
            return true;
        }
        player.sendMessage(ChatColor.RED + "Failed to login.");
        return false;
    }
}

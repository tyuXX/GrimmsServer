package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.nio.charset.StandardCharsets;

public class Register {
    public static boolean subCommand(Player player, String pass){
        PlayerStats playerStats = PlayerStats.getPlayerStats(player);
        if(!playerStats.getStat("pass", String.class).isEmpty()){
            player.sendMessage(ChatColor.RED + "Unregister first.");
            return false;
        }
        playerStats.setStat("pass", Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString());
        GAuthBaseCommand.login(player, true);
        player.sendMessage(ChatColor.GREEN + "Successfully registered and logged in.");
        return true;
    }
}

package org.gsdistance.grimmsServer.Commands.GAuthCommand;

import com.google.common.hash.Hashing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.nio.charset.StandardCharsets;

public class Login {
    public Login() {
    }

    public static boolean subCommand(Player player, String pass) {
        if (GAuthBaseCommand.isLoggedIn(player)) {
            player.sendMessage(ChatColor.YELLOW + "Already logged in.");
            return true;
        } else {
            String playerKey = player.getUniqueId() + "-login";
            
            Long cooldownEnd = PerSessionDataStorage.getData(playerKey + "-cooldown", Long.class);
            if (cooldownEnd != null && System.currentTimeMillis() < cooldownEnd) {
                long remainingSeconds = (cooldownEnd - System.currentTimeMillis()) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before trying again.");
                return false;
            }

            PlayerStats playerStats = PlayerStats.getPlayerStats(player);
            if (Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString().equals(playerStats.getStat("pass", String.class))) {
                GAuthBaseCommand.login(player, true);
                PerSessionDataStorage.softSave(0, Integer.class, playerKey + "-attempts");
                PerSessionDataStorage.softSave(null, Long.class, playerKey + "-cooldown");
                player.setInvulnerable(false);
                player.sendMessage(ChatColor.GREEN + "Logged in.");
                return true;
            } else {
                Integer failedAttempts = PerSessionDataStorage.getData(playerKey + "-attempts", Integer.class);
                if (failedAttempts == null) failedAttempts = 0;
                failedAttempts++;
                PerSessionDataStorage.softSave(failedAttempts, Integer.class, playerKey + "-attempts");

                if (failedAttempts >= 3) {
                    Integer cooldownMultiplier = PerSessionDataStorage.getData(playerKey + "-multiplier", Integer.class);
                    if (cooldownMultiplier == null) cooldownMultiplier = 1;
                    
                    long cooldownMillis = 60000L * cooldownMultiplier;
                    cooldownEnd = System.currentTimeMillis() + cooldownMillis;
                    PerSessionDataStorage.softSave(cooldownEnd, Long.class, playerKey + "-cooldown");
                    PerSessionDataStorage.softSave(cooldownMultiplier * 2, Integer.class, playerKey + "-multiplier");
                    PerSessionDataStorage.softSave(0, Integer.class, playerKey + "-attempts");
                    
                    player.kickPlayer(ChatColor.RED + "Too many failed login attempts. Wait " + (cooldownMillis / 60000) + " minute(s) before rejoining.");
                    return false;
                }

                player.sendMessage(ChatColor.RED + "Failed to login. Attempts: " + failedAttempts + "/3");
                return false;
            }
        }
    }
}

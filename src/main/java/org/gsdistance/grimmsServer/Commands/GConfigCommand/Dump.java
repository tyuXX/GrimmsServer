package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;

public class Dump {
    public Dump() {
    }

    public static boolean subCommand(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Config Dump ===");

        for (ConfigKey key : ConfigKey.values()) {
            String var10001 = key.getKey();
            player.sendMessage(ChatColor.GRAY + "|" + ChatColor.YELLOW + var10001 + ChatColor.GRAY + ":" + ChatColor.WHITE + ActiveConfig.getConfigValue(key, String.class));
        }

        return true;
    }
}

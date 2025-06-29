package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ConfigKey;

import static org.gsdistance.grimmsServer.Config.ActiveConfig.getConfigValue;

public class Dump {
    public static boolean subCommand(Player player) {
        player.sendMessage("Config Dump:");
        for (ConfigKey key : ConfigKey.values()) {
            player.sendMessage("|" + key.getKey() + ":" + getConfigValue(key, String.class));
        }
        return true;
    }
}

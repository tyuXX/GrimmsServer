package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigKey;

public class Dump {
    public Dump() {
    }

    public static boolean subCommand(Player player) {
        player.sendMessage("Config Dump:");

        for (ConfigKey key : ConfigKey.values()) {
            String var10001 = key.getKey();
            player.sendMessage("|" + var10001 + ":" + ActiveConfig.getConfigValue(key, String.class));
        }

        return true;
    }
}

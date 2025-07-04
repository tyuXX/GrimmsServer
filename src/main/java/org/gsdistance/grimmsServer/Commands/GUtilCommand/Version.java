package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Version {
    public static boolean subCommand(Player player, String[] args) {
        player.sendMessage(GrimmsServer.instance.getDescription().getPrefix() + " GMSv" + GrimmsServer.instance.getDescription().getVersion());
        return true;
    }
}

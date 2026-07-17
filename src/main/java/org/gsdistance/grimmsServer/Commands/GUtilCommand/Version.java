package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Version {
    public Version() {
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        String var10001 = GrimmsServer.instance.getDescription().getPrefix();
        sender.sendMessage(var10001 + " GMSv" + GrimmsServer.instance.getDescription().getVersion());
        return true;
    }
}

package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.GrimmsServer;

public class Version {

    public static boolean subCommand(CommandSender sender, String[] args) {
        String name = GrimmsServer.instance.getDescription().getPrefix();
        sender.sendMessage(name + " v" + GrimmsServer.instance.getDescription().getVersion());
        return true;
    }
}

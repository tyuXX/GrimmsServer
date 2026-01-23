package org.gsdistance.grimmsServer.Commands.GHelpCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Data.HelpStrings;

public class GHelp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1){
            return false;
        }
        sender.sendMessage("Help string for command " + args[0] + ":");
        sender.sendMessage(HelpStrings.getHelpString(args[0]));
        sender.sendMessage(HelpStrings.getHelpUsage(args[0]));
        return true;
    }
}

package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Map;

public class LogGrimmsServerCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Map<String, Map<String, Object>> commands = GrimmsServer.instance.getDescription().getCommands();
        sender.sendMessage("__Available commands:");
        for (String cmd : commands.keySet()) {
            sender.sendMessage("|" + cmd + ": " + GrimmsServer.instance.getCommand(cmd).getDescription());
            sender.sendMessage("|Usage: " + GrimmsServer.instance.getCommand(cmd).getUsage());
        }
        return true;
    }
}
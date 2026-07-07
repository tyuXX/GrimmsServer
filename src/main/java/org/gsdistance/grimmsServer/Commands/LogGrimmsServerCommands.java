package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LogGrimmsServerCommands implements CommandExecutor {
    public LogGrimmsServerCommands() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<String, Map<String, Object>> commands = GrimmsServer.instance.getDescription().getCommands();
        sender.sendMessage("__Available commands:");

        for (String cmd : commands.keySet()) {
            Command cmdObj = GrimmsServer.instance.getCommand(cmd);
            if (cmdObj != null) {
                sender.sendMessage("|" + cmd + ": " + cmdObj.getDescription());
                sender.sendMessage("|Usage: " + cmdObj.getUsage());
            }
        }

        return true;
    }
}

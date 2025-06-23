package org.gsdistance.grimmsServer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.Config.ConfigLoader;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.logging.Level;

public class ReloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Reloading config...");
        ConfigLoader.loadConfigFromFile();
        GrimmsServer.logger.log(Level.INFO, "Config reloaded.");
        sender.sendMessage("Config reloaded successfully.");
        return true;
    }
}

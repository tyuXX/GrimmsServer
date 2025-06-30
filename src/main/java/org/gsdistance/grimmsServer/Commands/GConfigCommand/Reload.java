package org.gsdistance.grimmsServer.Commands.GConfigCommand;

import org.gsdistance.grimmsServer.Config.ActiveConfig;
import org.gsdistance.grimmsServer.Config.ConfigLoader;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

import java.util.logging.Level;

public class Reload {
    public static boolean subCommand() {
        Shared.Broadcast("Reloading config...", null);
        ActiveConfig.configValues.clear();
        ConfigLoader.loadConfigFromFile();
        GrimmsServer.logger.log(Level.INFO, "Config reloaded.");
        Shared.Broadcast("Config reloaded successfully.", null);
        return true;
    }
}

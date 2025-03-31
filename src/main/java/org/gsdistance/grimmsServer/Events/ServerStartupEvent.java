package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Commands.CommandRegistry;
import org.gsdistance.grimmsServer.Commands.LogSelfStatsCommand;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.logging.Level;

public class ServerStartupEvent {

    public static void Event() {
        CommandRegistry.registerCommands();
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully.");
    }
}

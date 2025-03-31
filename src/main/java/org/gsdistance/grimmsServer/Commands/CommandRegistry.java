package org.gsdistance.grimmsServer.Commands;

import org.gsdistance.grimmsServer.GrimmsServer;

public class CommandRegistry {
    public static void registerCommands() {
        // Register all commands here
        GrimmsServer.instance.getCommand("logselfstats").setExecutor(new LogSelfStatsCommand());
        GrimmsServer.instance.getCommand("logworldstats").setExecutor(new LogWorldStats());
    }
}

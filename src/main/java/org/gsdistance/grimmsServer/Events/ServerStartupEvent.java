package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Commands.LogSelfStatsCommand;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ServerStartupEvent {

    public static void Event() {
        GrimmsServer.instance.getCommand("logselfstats").setExecutor(new LogSelfStatsCommand());
        System.out.println("Grimm's Server has been loaded.");
    }
}

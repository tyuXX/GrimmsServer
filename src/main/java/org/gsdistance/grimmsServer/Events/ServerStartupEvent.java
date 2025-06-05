package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.CommandRegistry;

import java.util.logging.Level;

public class ServerStartupEvent {

    public static void Event() {
        CommandRegistry.registerCommands();
        GrimmsServer.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GrimmsServer.instance, ServerTickEvent::Event, 100L, 1L);
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully.");
    }
}

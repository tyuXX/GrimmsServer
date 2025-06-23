package org.gsdistance.grimmsServer.Events;

import org.bukkit.Bukkit;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;
import org.gsdistance.grimmsServer.Manage.CommandRegistry;

import java.util.logging.Level;

public class ServerStartupEvent {

    public static void Event() {
        CommandRegistry.registerCommands();
        GrimmsServer.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GrimmsServer.instance, ServerTickEvent::Event, 100L, 1L);
        Bukkit.getScheduler().runTask(GrimmsServer.instance, () -> {
            try {
                DynamicDimensionGen.loadWorlds();
                GrimmsServer.logger.log(Level.INFO, "Worlds loaded successfully.");
            } catch (Exception e) {
                GrimmsServer.logger.severe("Failed to load worlds: " + e.getMessage());
            }
        });
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully.");
    }
}

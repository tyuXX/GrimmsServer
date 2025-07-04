package org.gsdistance.grimmsServer.Events.Listeners;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;
import org.gsdistance.grimmsServer.Manage.CommandRegistry;

import java.util.logging.Level;

public class ServerStartupEvent {
    public static void Event() {
        Stopwatch sw = Stopwatch.createStarted();
        CommandRegistry.registerCommands();
        GrimmsServer.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GrimmsServer.instance, ServerTickEvent::Event, 100L, 1L);
        Bukkit.getScheduler().runTask(GrimmsServer.instance, () -> {
            Stopwatch swLoad = Stopwatch.createStarted();
            try {
                DynamicDimensionGen.loadWorlds();
                GrimmsServer.logger.log(Level.INFO, "Worlds loaded successfully. (" + swLoad.stop() + ")");
            } catch (Exception e) {
                GrimmsServer.logger.severe("Failed to load worlds: " + e.getMessage() + "(" + swLoad.stop() + ")");
            }
            DynamicDimensionGen.unLoadWorlds();
        });
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully. (" + sw.stop() + ")");
    }
}

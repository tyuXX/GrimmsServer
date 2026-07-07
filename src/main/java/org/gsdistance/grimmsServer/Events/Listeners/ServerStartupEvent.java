package org.gsdistance.grimmsServer.Events.Listeners;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;
import org.gsdistance.grimmsServer.Indexers.ItemIndexer;
import org.gsdistance.grimmsServer.Manage.CommandRegistry;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartupEvent {
    public ServerStartupEvent() {
    }

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
                Logger var10000 = GrimmsServer.logger;
                String var10001 = e.getMessage();
                var10000.severe("Failed to load worlds: " + var10001 + "(" + swLoad.stop() + ")");
            }

            DynamicDimensionGen.unLoadWorlds();
            Bukkit.getScheduler().runTask(GrimmsServer.instance, ItemIndexer::indexWholeRegistry);
        });
        GrimmsServer.logger.log(Level.INFO, "GrimmsServer has started successfully. (" + sw.stop() + ")");
    }
}

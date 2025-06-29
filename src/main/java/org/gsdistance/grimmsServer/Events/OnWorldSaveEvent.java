package org.gsdistance.grimmsServer.Events;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.Map;

public class OnWorldSaveEvent {
    public static int i = 0;

    public static void Event(WorldSaveEvent event) {
        i++;
        if (i >= Bukkit.getWorlds().size()) {
            saveData();
            i = 0; // Reset counter after all worlds triggered the save event
        }
    }

    public static void saveData() {
        Stopwatch sw = Stopwatch.createStarted();
        // Save all temporary data
        GrimmsServer.logger.info("Saving temporary data...");
        for (Map<Object, Type> data : PerSessionDataStorage.dataStore.values()) {
            Type type = data.values().iterator().next().getClass();
            if (type.equals((PlayerMetadata.class))) {
                ((PlayerMetadata) data.keySet().iterator().next()).saveToPDS();
            }
        }
        GrimmsServer.logger.info("Done (" + sw.stop() + ")");
    }
}

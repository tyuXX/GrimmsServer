package org.gsdistance.grimmsServer.Events;

import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.lang.reflect.Type;
import java.util.Map;

public class OnWorldSaveEvent {
    public static void Event(WorldSaveEvent event) {
        // Save all temporary data
        GrimmsServer.logger.info("Saving temporary data...");
        for (Map<Object, Type> data : PerSessionDataStorage.dataStore.values()) {
            Type type = data.values().iterator().next().getClass();
            if (type.equals((PlayerMetadata.class))) {
                ((PlayerMetadata) data.keySet().iterator().next()).saveToPDS();
            }
        }
        GrimmsServer.logger.info("Done");
    }
}

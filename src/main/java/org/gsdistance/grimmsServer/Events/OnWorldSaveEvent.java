package org.gsdistance.grimmsServer.Events;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Faction;
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
        for (Data<Object, Type> data : PerSessionDataStorage.dataStore.values()) {
            Type type = data.value.getClass();
            if (type.equals((PlayerMetadata.class))) {
                ((PlayerMetadata) data.key).saveToPDS();
            } else if (type.equals(Faction.class)) {
                ((Faction) data.key).saveToFile();
            }
        }
        GrimmsServer.logger.info("Done (" + sw.stop() + ")");
    }
}

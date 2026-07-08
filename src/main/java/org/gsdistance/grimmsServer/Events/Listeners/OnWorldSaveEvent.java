package org.gsdistance.grimmsServer.Events.Listeners;

import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldSaveEvent;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Constructable.Entity.EntityMetadata;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Manage.CustomEntityManager;

import java.lang.reflect.Type;

public class OnWorldSaveEvent {
    public static int i = 0;

    public OnWorldSaveEvent() {
    }

    public static void Event(WorldSaveEvent event) {
        ++i;
        if (i >= Bukkit.getWorlds().size()) {
            saveData();
            i = 0;
        }

    }

    public static void saveData() {
        Stopwatch sw = Stopwatch.createStarted();
        GrimmsServer.logger.info("Saving temporary data...");

        for (Data<Object, Type> data : PerSessionDataStorage.dataStore.values()) {
            Type type = data.value().getClass();
            if (type.equals(PlayerMetadata.class)) {
                ((PlayerMetadata) data.key()).saveToPDS();
            } else if (type.equals(Faction.class)) {
                ((Faction) data.key()).saveToFile();
            } else if (type.equals(EntityMetadata.class)) {
                ((EntityMetadata) data.key()).saveToFile();
            }
        }

        CustomEntityManager.saveToFile();

        GrimmsServer.logger.info("Done (" + sw.stop() + ")");
    }
}

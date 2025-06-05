package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.WorldMetadata;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

public class WorldLoadEvent {
    public static void Event(org.bukkit.event.world.WorldLoadEvent event) {
        (new PluginDataStorage(GrimmsServer.instance)).saveData(new WorldMetadata(event.getWorld()), WorldMetadata.class, event.getWorld().getName() + ".json", "worldMetadata");
    }
}

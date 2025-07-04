package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.World.WorldMetadata;
import org.gsdistance.grimmsServer.GrimmsServer;

public class WorldLoadEvent {
    public static void Event(org.bukkit.event.world.WorldLoadEvent event) {
        GrimmsServer.pds.saveData(new WorldMetadata(event.getWorld()), WorldMetadata.class, event.getWorld().getName() + ".json", "worldMetadata");
    }
}

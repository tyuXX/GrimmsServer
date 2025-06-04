package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.WorldChunk;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ChunkLoadEvent {
    public static void Event(org.bukkit.event.world.ChunkLoadEvent event) {
        PluginDataStorage pluginDataStorage = new PluginDataStorage(GrimmsServer.instance);
        if (event.isNewChunk()){
            pluginDataStorage.saveData(new WorldChunk(event.getChunk(),null), WorldChunk.class, event.getChunk().getWorld().getName() + "_" + event.getChunk().getX() + "_" + event.getChunk().getZ() + ".json",
                "chunks");
        }
    }
}

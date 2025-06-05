package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;

public class ChunkLoadEvent {
    public static void Event(org.bukkit.event.world.ChunkLoadEvent event) {
        PluginDataStorage pluginDataStorage = new PluginDataStorage(GrimmsServer.instance);
        String name = event.getChunk().getWorld().getName() + "_" + event.getChunk().getX() + "_" + event.getChunk().getZ() + ".json";
        if (!pluginDataStorage.exists(name, "chunkMetadata")) {
            pluginDataStorage.saveData(new ChunkMetadata(event.getChunk(), null), ChunkMetadata.class, name,
                    "chunkMetadata");
        }
    }
}

package org.gsdistance.grimmsServer.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;

import java.io.File;

public class ChunkLoadEvent {
    public static void Event(org.bukkit.event.world.ChunkLoadEvent event) {
        PluginDataStorage pluginDataStorage = new PluginDataStorage(GrimmsServer.instance);
        String name = event.getChunk().getX() + "_" + event.getChunk().getZ() + ".json";

        if (!pluginDataStorage.exists(name, "chunkMetadata" + File.separatorChar + event.getWorld().getName())) {
            Player player = Shared.getClosestPlayer(new Location(event.getWorld(), event.getChunk().getX(), 0, event.getChunk().getZ()), event.getWorld().getPlayers());
            pluginDataStorage.saveData(new ChunkMetadata(event.getChunk(), null, player), ChunkMetadata.class, name, "chunkMetadata" + File.separatorChar + event.getWorld().getName());
        }
    }
}

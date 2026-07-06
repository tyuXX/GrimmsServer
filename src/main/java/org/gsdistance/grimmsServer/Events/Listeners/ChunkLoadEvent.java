package org.gsdistance.grimmsServer.Events.Listeners;

import java.io.File;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Shared;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;

public class ChunkLoadEvent {
   public ChunkLoadEvent() {
   }

   public static void Event(org.bukkit.event.world.ChunkLoadEvent event) {
      PluginDataStorage pluginDataStorage = new PluginDataStorage(GrimmsServer.instance);
      int var10000 = event.getChunk().getX();
      String name = var10000 + "_" + event.getChunk().getZ() + ".json";
      char var10002 = File.separatorChar;
      if (!pluginDataStorage.exists(name, "chunkMetadata" + var10002 + event.getWorld().getName())) {
         Player player = Shared.getClosestPlayer(new Location(event.getWorld(), (double)event.getChunk().getX(), (double)0.0F, (double)event.getChunk().getZ()), event.getWorld().getPlayers());
         ChunkMetadata chunkMetadata = new ChunkMetadata(event.getChunk(), (UUID)null, player);
         chunkMetadata.saveToFile();
      }

   }
}

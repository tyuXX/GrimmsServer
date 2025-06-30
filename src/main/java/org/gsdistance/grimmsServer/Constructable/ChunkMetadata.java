package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

import javax.annotation.Nullable;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChunkMetadata {
    public final int x;
    public final int z;
    public String world;
    public UUID factionUUID;
    public final String timestamp;
    public final UUID discoveredBy;

    public ChunkMetadata(Chunk chunk, @Nullable UUID factionUUID, @Nullable Player player) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.factionUUID = factionUUID;
        this.timestamp = LocalDateTime.now().toString();
        this.discoveredBy = player != null ? player.getUniqueId() : null;
        this.world = chunk.getWorld().getName();
    }

    public static ChunkMetadata getChunkMetadata(Chunk chunk) {
        return GrimmsServer.pds.retrieveData(chunk.getX() + "_" + chunk.getZ() + ".json", "chunkMetadata" + File.separator + chunk.getWorld().getName(), ChunkMetadata.class);
    }

    public void saveToFile() {
        GrimmsServer.pds.saveData(this, ChunkMetadata.class, x + "_" + z + ".json", "chunkMetadata" + File.separator + world);
    }
}

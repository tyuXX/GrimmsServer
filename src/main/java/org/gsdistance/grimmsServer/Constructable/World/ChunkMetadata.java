package org.gsdistance.grimmsServer.Constructable.World;

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
        String worldName = chunk.getWorld().getName();
        if (!isValidWorldName(worldName)) {
            GrimmsServer.logger.warning("Invalid world name: " + worldName);
            return null;
        }
        return GrimmsServer.pds.retrieveData(chunk.getX() + "_" + chunk.getZ() + ".json", "chunkMetadata" + File.separator + worldName, ChunkMetadata.class);
    }

    public void saveToFile() {
        if (!isValidWorldName(this.world)) {
            GrimmsServer.logger.warning("Invalid world name for chunk metadata: " + this.world);
            return;
        }
        GrimmsServer.pds.saveData(this, ChunkMetadata.class, this.x + "_" + this.z + ".json", "chunkMetadata" + File.separator + this.world);
    }

    private static boolean isValidWorldName(String worldName) {
        if (worldName == null || worldName.isEmpty()) {
            return false;
        }
        // Prevent path traversal attacks
        if (worldName.contains("..") || worldName.contains("/") || worldName.contains("\\") || worldName.contains(":")) {
            return false;
        }
        // Only allow alphanumeric characters, underscores, and hyphens
        return worldName.matches("^[a-zA-Z0-9_-]+$");
    }
}

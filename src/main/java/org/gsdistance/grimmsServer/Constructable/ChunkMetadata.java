package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChunkMetadata {
    public final int x;
    public final int y;
    public UUID factionUUID;
    public final String timestamp;
    public final UUID discoveredBy;

    public ChunkMetadata(Chunk chunk, @Nullable UUID factionUUID, @Nullable Player player) {
        this.x = chunk.getX();
        this.y = chunk.getZ();
        this.factionUUID = factionUUID;
        this.timestamp = LocalDateTime.now().toString();
        this.discoveredBy = player != null ? player.getUniqueId() : null;
    }
}

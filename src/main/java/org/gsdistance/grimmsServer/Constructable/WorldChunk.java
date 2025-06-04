package org.gsdistance.grimmsServer.Constructable;

import org.bukkit.Chunk;

import java.util.UUID;

public class WorldChunk {
    public int x;
    public int y;
    public UUID factionUUID;

    public WorldChunk(Chunk chunk, UUID factionUUID) {
        this.x = chunk.getX();
        this.y = chunk.getZ();
        this.factionUUID = factionUUID;
    }
}

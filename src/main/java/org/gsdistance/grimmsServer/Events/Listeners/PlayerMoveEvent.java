package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.boss.KeyedBossBar;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

public class PlayerMoveEvent {
    public static void Event(org.bukkit.event.player.PlayerMoveEvent event) {
        if (event.getTo() == null) {
            return; // Do nothing if the destination is null
        }
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            return; // Do nothing if the player hasn't moved to a new chunk
        }

        // Remove the player from the previous chunk's boss bar
        ChunkMetadata fromChunkMetadata = ChunkMetadata.getChunkMetadata(event.getFrom().getChunk());
        if (fromChunkMetadata != null && fromChunkMetadata.factionUUID != null) {
            Faction fromFaction = Faction.getFaction(fromChunkMetadata.factionUUID);
            if (fromFaction != null) {
                KeyedBossBar fromBossBar = fromFaction.getBossBar();
                if (fromBossBar != null) {
                    fromBossBar.removePlayer(event.getPlayer());
                }
            }
        }

        // Add the player to the new chunk's boss bar
        ChunkMetadata toChunkMetadata = ChunkMetadata.getChunkMetadata(event.getTo().getChunk());
        if (toChunkMetadata != null && toChunkMetadata.factionUUID != null) {
            Faction toFaction = Faction.getFaction(toChunkMetadata.factionUUID);
            if (toFaction != null) {
                KeyedBossBar toBossBar = toFaction.getBossBar();
                if (toBossBar != null) {
                    toBossBar.addPlayer(event.getPlayer());
                }
            }
        }
    }
}
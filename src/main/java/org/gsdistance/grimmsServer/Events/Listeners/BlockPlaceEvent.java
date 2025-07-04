package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

public class BlockPlaceEvent {
    public static void Event(org.bukkit.event.block.BlockPlaceEvent event) {
        // Interrupt if claimed
        ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getBlock().getChunk());
        if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass")) {
            if (chunkMetadata.factionUUID != null) {
                Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
                if (faction != null) {
                    if (!faction.isMember(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage("§cYou cannot place blocks in this area, it is claimed by " + faction.name + "!");
                        event.setCancelled(true);
                    }
                }
            }
        }
        // Continue with the event
    }
}

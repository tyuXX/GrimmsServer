package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.ChunkMetadata;
import org.gsdistance.grimmsServer.Constructable.Faction;

public class PlayerInteractEvent {
    public static void Event(org.bukkit.event.player.PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getClickedBlock().getChunk());
            if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass")) {
                if (chunkMetadata.factionUUID != null) {
                    Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
                    if (faction != null) {
                        if (!faction.isMember(event.getPlayer().getUniqueId())) {
                            event.getPlayer().sendMessage("§cYou cannot interact with blocks in this area, it is claimed by " + faction.name + "!");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        // Handle other interactions here if needed
    }
}

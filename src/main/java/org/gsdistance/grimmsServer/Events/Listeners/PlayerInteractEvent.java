package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

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
                        }
                    }
                }
            }
        }
        // Handle other interactions here if needed
    }
}

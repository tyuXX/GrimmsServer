package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.boss.KeyedBossBar;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

public class PlayerMoveEvent {
    public PlayerMoveEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerMoveEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (event.getTo() != null) {
            if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
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
    }
}

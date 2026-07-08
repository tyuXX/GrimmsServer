package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.boss.KeyedBossBar;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;

import java.util.Iterator;

public class PlayerMoveEvent {
    public PlayerMoveEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerMoveEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (event.getTo() != null) {
            boolean chunkChanged = !event.getFrom().getChunk().equals(event.getTo().getChunk());
            boolean worldChanged = !event.getFrom().getWorld().equals(event.getTo().getWorld());

            if (chunkChanged || worldChanged) {
                // Remove player from all faction bossbars
                for (Iterator<KeyedBossBar> it = Bukkit.getServer().getBossBars(); it.hasNext(); ) {
                    KeyedBossBar bossBar = it.next();
                    if (bossBar.getKey().getKey().startsWith("faction-")) {
                        bossBar.removePlayer(event.getPlayer());
                    }
                }

                // Add player to the new chunk's faction bossbar if applicable
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

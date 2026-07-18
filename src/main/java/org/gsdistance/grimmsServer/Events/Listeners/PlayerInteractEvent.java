package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Data.Player.AfkManager;

public class PlayerInteractEvent {
    public PlayerInteractEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerInteractEvent event) {
        if (!GAuthBaseCommand.isLoggedIn(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must login with /gAuth login <password> to perform this action.");
            event.setCancelled(true);
            return;
        }

        // Record activity for AFK tracking
        AfkManager.recordActivity(event.getPlayer());

        if (event.getClickedBlock() != null) {
            ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getClickedBlock().getChunk());
            if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass") && chunkMetadata.factionUUID != null) {
                Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
                if (faction != null && !faction.isMember(event.getPlayer().getUniqueId())) {
                    event.getPlayer().sendMessage("§cYou cannot interact with blocks in this area, it is claimed by " + faction.name + "!");
                    event.setCancelled(true);
                }
            }
        }

    }
}

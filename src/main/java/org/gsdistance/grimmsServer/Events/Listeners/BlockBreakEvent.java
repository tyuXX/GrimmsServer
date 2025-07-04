package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class BlockBreakEvent {
    public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
        // Interrupt if claimed
        ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getBlock().getChunk());
        if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass")) {
            if (chunkMetadata.factionUUID != null) {
                Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
                if (faction != null) {
                    if (!faction.isMember(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage("§cYou cannot break blocks in this area, it is claimed by " + faction.name + "!");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
        // Continue with the event
        if (event.getPlayer().getType() == org.bukkit.entity.EntityType.PLAYER) {
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("money", (int) Math.round(1 * PlayerLevelHandler.getLevelHandler(event.getPlayer()).getLesserMoneyMultiplier()));
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("tPoint", (int) Math.round(3 * PlayerLevelHandler.getLevelHandler(event.getPlayer()).getMoneyMultiplier()));
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("block_break_count", 1);
            PlayerLevelHandler.getLevelHandler(event.getPlayer()).addExp(3);
            if (ItemLevelHandler.isItemLevelable(event.getPlayer().getInventory().getItemInMainHand())) {
                ItemLevelHandler.getLevelHandler(event.getPlayer()).addXp(5);
            }
        }
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("block_break_count", 1);
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("wPoint", 3);
    }
}

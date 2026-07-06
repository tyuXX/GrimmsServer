package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.gsdistance.grimmsServer.Constructable.Faction;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Constructable.World.ChunkMetadata;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class BlockBreakEvent {
   public BlockBreakEvent() {
   }

   public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
      ChunkMetadata chunkMetadata = ChunkMetadata.getChunkMetadata(event.getBlock().getChunk());
      if (!event.getPlayer().hasPermission("grimmsserver.faction.bypass") && chunkMetadata.factionUUID != null) {
         Faction faction = Faction.getFaction(chunkMetadata.factionUUID);
         if (faction != null && !faction.isMember(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage("§cYou cannot break blocks in this area, it is claimed by " + faction.name + "!");
            event.setCancelled(true);
            return;
         }
      }

      if (event.getPlayer().getType() == EntityType.PLAYER) {
         PlayerStats.getPlayerStats(event.getPlayer()).changeStat("money", (int)Math.round((double)1.0F * PlayerLevelHandler.getLevelHandler(event.getPlayer()).getLesserMoneyMultiplier()));
         PlayerStats.getPlayerStats(event.getPlayer()).changeStat("tPoint", (int)Math.round((double)3.0F * PlayerLevelHandler.getLevelHandler(event.getPlayer()).getMoneyMultiplier()));
         PlayerStats.getPlayerStats(event.getPlayer()).changeStat("block_break_count", 1);
         PlayerLevelHandler.getLevelHandler(event.getPlayer()).addExp((double)3.0F);
         if (ItemLevelHandler.isItemLevelable(event.getPlayer().getInventory().getItemInMainHand())) {
            ItemLevelHandler.getLevelHandler(event.getPlayer()).addXp((double)5.0F);
         }
      }

      WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("block_break_count", 1);
      WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("wPoint", 3);
   }
}

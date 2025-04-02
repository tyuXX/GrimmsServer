package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Stats.LevelHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class BlockBreakEvent {
    public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
        if (event.getPlayer().getType() == org.bukkit.entity.EntityType.PLAYER) {
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("money", 1);
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("tPoint", 3);
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("block_break_count", 1);
            LevelHandler.getLevelHandler(event.getPlayer()).addExp(3);
        }
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("block_break_count", 1);
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("wPoint", 3);
    }
}

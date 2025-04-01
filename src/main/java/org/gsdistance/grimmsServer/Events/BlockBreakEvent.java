package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class BlockBreakEvent {
    public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
        if (event.getPlayer().getType() == org.bukkit.entity.EntityType.PLAYER) {
            PlayerStats.getPlayerStats(event.getPlayer()).setStat("money", (double) PlayerStats.getPlayerStats(event.getPlayer()).getStat("money") + 0.5D);
            PlayerStats.getPlayerStats(event.getPlayer()).setStat("tPoint", (double) PlayerStats.getPlayerStats(event.getPlayer()).getStat("tPoint") + 1D);
        }
        WorldStats.getWorldStats(event.getBlock().getWorld()).setStat("block_break_count", (long) WorldStats.getWorldStats(event.getBlock().getWorld()).getStat("block_break_count") + 1L);
        WorldStats.getWorldStats(event.getBlock().getWorld()).setStat("wPoint", (double) WorldStats.getWorldStats(event.getBlock().getWorld()).getStat("wPoint") + 3.0D);
    }
}

package org.gsdistance.grimmsServer.Events;

import Leveling.ItemLevelHandler;
import Leveling.PlayerLevelHandler;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.logging.Level;

public class BlockBreakEvent {
    public static void Event(org.bukkit.event.block.BlockBreakEvent event) {
        if (event.getPlayer().getType() == org.bukkit.entity.EntityType.PLAYER) {
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("money", 1);
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("tPoint", 3);
            PlayerStats.getPlayerStats(event.getPlayer()).changeStat("block_break_count", 1);
            PlayerLevelHandler.getLevelHandler(event.getPlayer()).addExp(3);
            if(ItemLevelHandler.isItemLevelable(event.getPlayer().getInventory().getItemInMainHand())){
                ItemLevelHandler.getLevelHandler(event.getPlayer()).addXp(5);
                GrimmsServer.logger.log(Level.INFO,"xCurrent level: " + ItemLevelHandler.getLevelHandler(event.getPlayer()).getLevel());
            }
        }
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("block_break_count", 1);
        WorldStats.getWorldStats(event.getBlock().getWorld()).changeStat("wPoint", 3);
    }
}

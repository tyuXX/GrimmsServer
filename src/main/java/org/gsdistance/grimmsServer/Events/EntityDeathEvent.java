package org.gsdistance.grimmsServer.Events;

import Leveling.PlayerLevelHandler;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

public class EntityDeathEvent {
    public static void Event(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            if (event.getEntity().getKiller().getType() == org.bukkit.entity.EntityType.PLAYER) {
                PlayerStats playerStats = PlayerStats.getPlayerStats(event.getEntity().getKiller());
                WorldStats worldStats = WorldStats.getWorldStats(event.getEntity().getWorld());

                playerStats.changeStat("total_kill_count", 1);
                playerStats.changeStat("money", 3);
                playerStats.changeStat("tPoint", 25);
                worldStats.changeStat("wPoint", 40);
                PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(50);

                if (event.getEntity().getType() == org.bukkit.entity.EntityType.PLAYER) {
                    playerStats.changeStat("money", 12);
                    playerStats.changeStat("tPoint", 50);
                    worldStats.changeStat("wPoint", 110);
                    PlayerLevelHandler.getLevelHandler(event.getEntity().getKiller()).addExp(150);
                }
            }
        }
        if (event.getEntity().getType() == org.bukkit.entity.EntityType.PLAYER) {
            PlayerStats.getPlayerStats((org.bukkit.entity.Player) event.getEntity()).changeStat("death_count", 1);
        }
        WorldStats.getWorldStats(event.getEntity().getWorld()).changeStat("death_count", 1);
        ServerStats.getServerStats().changeStat("death_count", 1);
    }
}

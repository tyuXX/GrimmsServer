package org.gsdistance.grimmsServer.Events;

import org.bukkit.plugin.java.JavaPlugin;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.Objects;
import java.util.logging.Level;

public class EntityDeathEvent {
    public static void Event(org.bukkit.event.entity.EntityDeathEvent event){
        if(!(event.getEntity().getKiller() == null)){
            if(event.getEntity().getKiller().getType() == org.bukkit.entity.EntityType.PLAYER){
                PlayerStats.getPlayerStats(event.getEntity().getKiller()).changeStat("total_kill_count", 1);
            }
        }
        if(event.getEntity().getType() == org.bukkit.entity.EntityType.PLAYER){
            PlayerStats.getPlayerStats((org.bukkit.entity.Player) event.getEntity()).changeStat("death_count", 1);
        }
        WorldStats.getWorldStats(event.getEntity().getWorld()).changeStat("death_count", 1);
        ServerStats.getServerStats().changeStat("death_count", 1);
    }
}

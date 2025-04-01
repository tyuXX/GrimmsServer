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
                PlayerStats playerStats = PlayerStats.getPlayerStats(event.getEntity().getKiller());
                WorldStats worldStats = WorldStats.getWorldStats(event.getEntity().getWorld());
                
                playerStats.setStat("total_kill_count", (int) playerStats.getStat("total_kill_count") + 1);
                playerStats.setStat("money", (double) playerStats.getStat("money") + 3);
                playerStats.setStat("tPoint", (double) playerStats.getStat("tPoint") + 25);
                worldStats.setStat("wPoint", (double) worldStats.getStat("wPoint") + 40);
                
                if(event.getEntity().getType() == org.bukkit.entity.EntityType.PLAYER){
                    playerStats.setStat("money", (double) playerStats.getStat("money") + 12);
                    playerStats.setStat("tPoint", (double) playerStats.getStat("tPoint") + 50);
                    worldStats.setStat("wPoint", (double) worldStats.getStat("wPoint") + 110);
                }
            }
        }
        if(event.getEntity().getType() == org.bukkit.entity.EntityType.PLAYER){
            PlayerStats.getPlayerStats((org.bukkit.entity.Player) event.getEntity()).setStat("death_count", (int) PlayerStats.getPlayerStats((org.bukkit.entity.Player) event.getEntity()).getStat("death_count") + 1);
        }
        WorldStats.getWorldStats(event.getEntity().getWorld()).setStat("death_count", (int) WorldStats.getWorldStats(event.getEntity().getWorld()).getStat("death_count") + 1);
        ServerStats.getServerStats().setStat("death_count", (int) ServerStats.getServerStats().getStat("death_count") + 1);
    }
}

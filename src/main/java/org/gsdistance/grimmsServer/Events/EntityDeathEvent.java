package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;

import java.util.Objects;
import java.util.logging.Level;

public class EntityDeathEvent {
    public static void Event(org.bukkit.event.entity.EntityDeathEvent event){
        GrimmsServer.logger.log(Level.INFO, "Entity " + event.getEntity().getName() + " has died.");
        if(Objects.requireNonNull(event.getEntity().getKiller()).getType() == org.bukkit.entity.EntityType.PLAYER){
            PlayerStats.getPlayerStats(event.getEntity().getKiller()).changeStat("total_kill_count", 1);
            GrimmsServer.logger.log(Level.INFO, "Player " + event.getEntity().getKiller().getName() + " has killed " + event.getEntity().getName());
        }
    }
}

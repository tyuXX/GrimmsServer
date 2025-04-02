package org.gsdistance.grimmsServer.Events;

import Leveling.ItemLevelHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.logging.Level;

public class EntityDamageByEntityEvent {
    public static void Event(org.bukkit.event.entity.EntityDamageByEntityEvent event){
        if(event.getDamager().getType() == EntityType.PLAYER){
            if(ItemLevelHandler.isItemLevelable(((Player)event.getDamager()).getInventory().getItemInMainHand())){
                ItemLevelHandler.getLevelHandler((Player) event.getDamager()).addXp(event.getDamage());
                GrimmsServer.logger.log(Level.INFO,"xCurrent level: " + ItemLevelHandler.getLevelHandler((Player) event.getDamager()).getLevel());
            }
        }
    }
}

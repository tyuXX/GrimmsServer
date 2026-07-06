package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Data.StaticLists;

public class EntityDamageByEntityEvent {
   public EntityDamageByEntityEvent() {
   }

   public static void Event(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
      if (event.getDamager().getType() == EntityType.PLAYER && !StaticLists.xpBlacklist.contains(event.getEntityType()) && ItemLevelHandler.isItemLevelable(((Player)event.getDamager()).getInventory().getItemInMainHand())) {
         ItemLevelHandler.getLevelHandler((Player)event.getDamager()).addXp(event.getDamage());
      }

   }
}

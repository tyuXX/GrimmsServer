package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;
import org.gsdistance.grimmsServer.Data.StaticLists;
import org.gsdistance.grimmsServer.Events.Registers.CustomEntityDamageByEntityRegister;

public class CustomEntityDamageByEntityEvent {
    public CustomEntityDamageByEntityEvent() {
    }

    public static void Event(CustomEntityDamageByEntityRegister event) {
        // This event is only called for non-player entities being damaged
        org.bukkit.event.entity.EntityDamageByEntityEvent originalEvent = event.getOriginalEvent();

        // Item XP logic for damaging non-player entities
        if (originalEvent.getDamager().getType() == EntityType.PLAYER && !StaticLists.xpBlacklist.contains(originalEvent.getEntityType()) && ItemLevelHandler.isItemLevelable(((Player) originalEvent.getDamager()).getInventory().getItemInMainHand())) {
            ItemLevelHandler.getLevelHandler((Player) originalEvent.getDamager()).addXp(originalEvent.getDamage());
        }
    }
}

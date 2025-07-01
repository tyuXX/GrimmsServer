package org.gsdistance.grimmsServer.Events;

import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.ItemLevelHandler;

public class EntityDamageEvent {
    public static void Event(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player player) {
            ItemStack[] items = player.getInventory().getArmorContents();
            for (ItemStack item : items) {
                if (item != null && ItemLevelHandler.isItemLevelable(item)) { // Check for null before processing
                    ItemLevelHandler itemLevelHandler = ItemLevelHandler.getLevelHandler(item, player);
                    itemLevelHandler.addXp(event.getDamage());
                }
            }
        }
    }
}

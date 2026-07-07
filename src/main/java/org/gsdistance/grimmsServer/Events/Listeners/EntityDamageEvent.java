package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.ItemLevelHandler;

public class EntityDamageEvent {
    public EntityDamageEvent() {
    }

    public static void Event(org.bukkit.event.entity.EntityDamageEvent event) {
        Entity var2 = event.getEntity();
        if (var2 instanceof Player player) {
            ItemStack[] items = player.getInventory().getArmorContents();

            for (ItemStack item : items) {
                if (item != null && ItemLevelHandler.isItemLevelable(item)) {
                    ItemLevelHandler itemLevelHandler = ItemLevelHandler.getLevelHandler(item, player);
                    itemLevelHandler.addXp(event.getDamage());
                }
            }
        }

    }
}

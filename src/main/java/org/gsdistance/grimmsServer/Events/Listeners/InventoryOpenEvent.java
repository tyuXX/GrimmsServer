package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.inventory.InventoryHolder;
import org.gsdistance.grimmsServer.Shared;

public class InventoryOpenEvent {
    public InventoryOpenEvent() {
    }

    public static void Event(org.bukkit.event.inventory.InventoryOpenEvent event) {
        if (event.getPlayer() instanceof org.bukkit.entity.Player player) {
            InventoryHolder holder = event.getInventory().getHolder();
            if (!Shared.checkContainerAuth(player, holder, "access")) {
                event.setCancelled(true);
            }
        }
    }
}

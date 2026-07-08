package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.inventory.InventoryHolder;
import org.gsdistance.grimmsServer.Shared;

public class InventoryDragEvent {
    public InventoryDragEvent() {
    }

    public static void Event(org.bukkit.event.inventory.InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) {
            InventoryHolder holder = event.getInventory().getHolder();
            if (!Shared.checkContainerAuth(player, holder, "modify")) {
                event.setCancelled(true);
            }
        }
    }
}

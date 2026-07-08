package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.inventory.InventoryHolder;
import org.gsdistance.grimmsServer.Shared;

public class InventoryClickEvent {
    public InventoryClickEvent() {
    }

    public static void Event(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) {
            InventoryHolder holder = event.getInventory().getHolder();
            if (!Shared.checkContainerAuth(player, holder, "modify")) {
                event.setCancelled(true);
            }
        }
    }
}

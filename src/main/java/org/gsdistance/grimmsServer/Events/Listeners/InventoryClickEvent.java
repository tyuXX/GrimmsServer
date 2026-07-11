package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.gsdistance.grimmsServer.Commands.GLevelCommand.PrestigeShop;
import org.gsdistance.grimmsServer.Commands.GUtilCommand.SettingGUI;
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
            
            // Handle PrestigeShop GUI
            if (event.getView().getTitle().equals(PrestigeShop.GUI_TITLE)) {
                event.setCancelled(true);
                PrestigeShop shop = PrestigeShop.getGUI(player);
                if (shop != null) {
                    shop.handleClick(event.getSlot());
                }
                return;
            }
        }
    }
}

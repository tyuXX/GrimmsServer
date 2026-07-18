package org.gsdistance.grimmsServer.Commands.MarketComand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MarketGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        if (!event.getView().getTitle().equals(MarketGUI.GUI_TITLE)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player player)) return;

        MarketGUI gui = MarketGUI.getGUI(player);
        if (gui == null) return;

        gui.handleClick(event.getSlot(), event.isLeftClick(), event.isShiftClick());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(MarketGUI.GUI_TITLE)) {
            if (event.getPlayer() instanceof org.bukkit.entity.Player player) {
                MarketGUI.closeGUI(player);
            }
        }
    }
}

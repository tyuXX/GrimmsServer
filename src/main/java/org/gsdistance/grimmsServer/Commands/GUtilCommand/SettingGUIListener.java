package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class SettingGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        if (!event.getView().getTitle().equals(SettingGUI.GUI_TITLE)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player player)) return;

        SettingGUI gui = SettingGUI.getGUI(player);
        if (gui == null) return;

        gui.handleClick(event.getSlot());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(SettingGUI.GUI_TITLE)) {
            if (event.getPlayer() instanceof org.bukkit.entity.Player player) {
                SettingGUI.closeGUI(player);
            }
        }
    }
}

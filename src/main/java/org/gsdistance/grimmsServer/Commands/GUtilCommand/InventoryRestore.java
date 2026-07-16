package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.util.Map;

public class InventoryRestore {

    public InventoryRestore() {
    }

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.util.admin")) {
            player.sendMessage("You do not have permission to use this command.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage("Usage: /gutil inventoryrestore <player> [index]");
            return false;
        }

        String targetPlayerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null) {
            player.sendMessage("Player " + targetPlayerName + " is not online.");
            return false;
        }

        PlayerInventoryData inventoryData = PlayerInventoryData.getPlayerInventoryData(targetPlayer.getUniqueId());

        if (args.length == 2) {
            // Show list of historical inventories
            if (inventoryData.previousInventories.isEmpty()) {
                player.sendMessage("No historical inventories found for " + targetPlayerName + ".");
                return true;
            }

            player.sendMessage("Historical inventories for " + targetPlayerName + ":");
            for (int i = 0; i < inventoryData.previousInventories.size(); i++) {
                PlayerInventoryData.InventorySnapshot snapshot = inventoryData.previousInventories.get(i);
                player.sendMessage((i + 1) + ": " + snapshot.timestamp);
            }
            player.sendMessage("Use /gutil inventoryrestore " + targetPlayerName + " <index> to restore.");
            return true;
        }

        if (args.length == 3) {
            try {
                int index = Integer.parseInt(args[2]) - 1;
                if (index < 0 || index >= inventoryData.previousInventories.size()) {
                    player.sendMessage("Invalid index. Valid range: 1-" + inventoryData.previousInventories.size());
                    return false;
                }

                PlayerInventoryData.InventorySnapshot snapshot = inventoryData.previousInventories.get(index);
                
                // Restore inventory to target player
                targetPlayer.getInventory().clear();
                
                for (Map.Entry<Integer, ItemStack> entry : snapshot.inventoryContents.entrySet()) {
                    targetPlayer.getInventory().setItem(entry.getKey(), entry.getValue());
                }
                
                targetPlayer.getInventory().setArmorContents(snapshot.armorContents);
                targetPlayer.getInventory().setExtraContents(snapshot.extraContents);
                
                player.sendMessage("Restored inventory from " + snapshot.timestamp + " to " + targetPlayerName + ".");
                targetPlayer.sendMessage("Your inventory has been restored by an admin.");
                
                GrimmsServer.logger.info("Player " + player.getName() + " restored inventory for " + targetPlayerName + " from " + snapshot.timestamp);
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid index. Please provide a number.");
                return false;
            }
        }

        return false;
    }
}

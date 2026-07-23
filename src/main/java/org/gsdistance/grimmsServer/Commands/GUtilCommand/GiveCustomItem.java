package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemHandler;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemRegistry;
import org.gsdistance.grimmsServer.Constructable.Item.ItemStats;

import java.util.Set;

public class GiveCustomItem {

    public static boolean subCommand(Player player, String[] args) {
        if (!player.hasPermission("grimmsserver.util.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /gutil givecustomitem <item_id> [material]");
            return false;
        }

        String itemId = args[1].toLowerCase();
        
        // Check if the custom item is registered
        if (!CustomItemRegistry.isCustomItemRegistered(itemId)) {
            player.sendMessage(ChatColor.RED + "Custom item '" + itemId + "' is not registered.");
            player.sendMessage(ChatColor.GRAY + "Available custom items:");
            Set<String> registeredItems = CustomItemRegistry.getRegisteredItemIds();
            for (String registeredItem : registeredItems) {
                player.sendMessage(ChatColor.YELLOW + "- " + registeredItem);
            }
            return false;
        }

        // Determine material (default to iron_hoe for guns, or stone for other items)
        Material material = Material.IRON_HOE; // Default for guns
        if (args.length >= 3) {
            try {
                material = Material.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid material: " + args[2]);
                return false;
            }
        }

        // Create the custom item
        ItemStack itemStack = new ItemStack(material);
        try {
            // Ensure the ItemStack has ItemMeta (required for NBT operations)
            if (itemStack.getItemMeta() == null) {
                itemStack.setItemMeta(org.bukkit.Bukkit.getItemFactory().getItemMeta(material));
            }
            
            // Initialize the item with CustomItemHandler first
            CustomItemHandler.createHandler(itemStack);
            
            CustomItemRegistry.createCustomItem(itemId, itemStack);
            
            // Update item stats to show proper display name and description
            ItemStats.getItemStats(itemStack).UpdateItemStats();
            
            // Give item to player
            player.getInventory().addItem(itemStack);
            player.sendMessage(ChatColor.GREEN + "Given custom item: " + ChatColor.GOLD + itemId);
            return true;
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to create custom item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

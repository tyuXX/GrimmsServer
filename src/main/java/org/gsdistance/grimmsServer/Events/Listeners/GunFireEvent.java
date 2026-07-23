package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gsdistance.grimmsServer.Constructable.Item.CustomItemHandler;
import org.gsdistance.grimmsServer.Constructable.Item.ItemStats;

public class GunFireEvent {
    
    public static void Event(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !CustomItemHandler.isCustomItem(item)) {
            return;
        }
        
        CustomItemHandler handler = CustomItemHandler.getHandler(item);
        String itemId = handler.getCustomItemId();
        
        // Handle ammo reloading
        if (itemId.startsWith("ammo_")) {
            handleReloading(player, handler, itemId);
            return;
        }
        
        // Handle gun firing
        if (!itemId.startsWith("gun_")) {
            return;
        }
        
        // Get gun properties from NBT
        String gunType = handler.getCustomData("gun_type", PersistentDataType.STRING);
        Double damage = handler.getCustomData("damage", PersistentDataType.DOUBLE);
        Integer currentAmmo = handler.getCustomData("current_ammo", PersistentDataType.INTEGER);
        Integer maxAmmo = handler.getCustomData("max_ammo", PersistentDataType.INTEGER);
        String projectileType = handler.getCustomData("projectile_type", PersistentDataType.STRING);
        
        if (currentAmmo == null || currentAmmo <= 0) {
            player.sendMessage(ChatColor.RED + "Out of ammo! Right-click with ammo to reload.");
            return;
        }
        
        // Fire the gun
        fireProjectile(player, damage, projectileType);
        
        // Decrease ammo
        handler.setCustomData("current_ammo", currentAmmo - 1, PersistentDataType.INTEGER);
        
        // Update item stats to show new ammo count
        ItemStats.getItemStats(item).UpdateItemStats();
        
        player.sendMessage(ChatColor.GREEN + "Fired " + ChatColor.GOLD + gunType + ChatColor.GREEN + "! Ammo: " + ChatColor.YELLOW + (currentAmmo - 1) + "/" + maxAmmo);
    }
    
    private static void handleReloading(Player player, CustomItemHandler ammoHandler, String ammoId) {
        // Extract gun type from ammo ID (e.g., "ammo_pistol" -> "pistol")
        String gunType = ammoId.replace("ammo_", "");
        
        // Find gun in player's inventory
        ItemStack gunItem = null;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && CustomItemHandler.isCustomItem(item)) {
                CustomItemHandler gunHandler = CustomItemHandler.getHandler(item);
                String gunItemId = gunHandler.getCustomItemId();
                if (gunItemId.startsWith("gun_")) {
                    String storedGunType = gunHandler.getCustomData("gun_type", PersistentDataType.STRING);
                    if (gunType.equalsIgnoreCase(storedGunType)) {
                        gunItem = item;
                        break;
                    }
                }
            }
        }
        
        if (gunItem == null) {
            player.sendMessage(ChatColor.RED + "No matching gun found for this ammo!");
            return;
        }
        
        CustomItemHandler gunHandler = CustomItemHandler.getHandler(gunItem);
        Integer currentAmmo = gunHandler.getCustomData("current_ammo", PersistentDataType.INTEGER);
        Integer maxAmmo = gunHandler.getCustomData("max_ammo", PersistentDataType.INTEGER);
        Integer ammoAmount = ammoHandler.getCustomData("ammo_amount", PersistentDataType.INTEGER);
        
        if (currentAmmo == null || maxAmmo == null || ammoAmount == null) {
            player.sendMessage(ChatColor.RED + "Failed to reload: Invalid item data.");
            return;
        }
        
        // Calculate how much ammo to add
        int ammoNeeded = maxAmmo - currentAmmo;
        int ammoToAdd = Math.min(ammoNeeded, ammoAmount);
        
        if (ammoToAdd <= 0) {
            player.sendMessage(ChatColor.YELLOW + "Gun is already fully loaded!");
            return;
        }
        
        // Add ammo to gun
        gunHandler.setCustomData("current_ammo", currentAmmo + ammoToAdd, PersistentDataType.INTEGER);
        
        // Consume ammo from ammo item
        int remainingAmmo = ammoAmount - ammoToAdd;
        if (remainingAmmo <= 0) {
            // Remove ammo item if empty
            player.getInventory().setItemInMainHand(null);
            player.sendMessage(ChatColor.GREEN + "Reloaded " + ChatColor.GOLD + gunType + ChatColor.GREEN + " to full! Ammo item consumed.");
        } else {
            // Update ammo item with remaining ammo
            ammoHandler.setCustomData("ammo_amount", remainingAmmo, PersistentDataType.INTEGER);
            player.sendMessage(ChatColor.GREEN + "Reloaded " + ChatColor.GOLD + gunType + ChatColor.GREEN + " with " + ChatColor.YELLOW + ammoToAdd + ChatColor.GREEN + " rounds. Remaining ammo: " + ChatColor.YELLOW + remainingAmmo);
        }
        
        // Update gun item stats
        ItemStats.getItemStats(gunItem).UpdateItemStats();
    }
    
    private static void fireProjectile(Player player, Double damage, String projectileType) {
        // Spawn projectile entity and set its damage
        // Using snowball as a basic projectile for now
        Snowball projectile = player.launchProjectile(Snowball.class);
        
        // Set custom damage on the projectile (you may need a custom entity system for this)
        // For now, we'll store the damage in the projectile's metadata
        projectile.setCustomName("gun_projectile_" + damage);
        
        // You can extend this with different projectile types based on projectileType
        // For example: arrows, fireballs, or custom entities
    }
}

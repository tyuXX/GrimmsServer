package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.Player.PlayerInventoryData;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;

import java.util.Map;

public class PlayerRespawnEvent {
    public PlayerRespawnEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            if (PerSessionDataStorage.dataStore.containsKey("deathLocation-" + event.getPlayer().getUniqueId())) {
                Location deathLocation = (Location) PerSessionDataStorage.dataStore.get("deathLocation-" + event.getPlayer().getUniqueId()).key();
                World deathWorld = deathLocation.getWorld();
                if (deathWorld != null && DynamicDimensionGen.dynamicDimensions.contains(deathWorld.getUID())) {
                    if (deathWorld.isChunkLoaded(deathLocation.getBlockX() >> 4, deathLocation.getBlockZ() >> 4)) {
                        event.setRespawnLocation(deathLocation);
                    } else {
                        event.setRespawnLocation(deathWorld.getSpawnLocation());
                    }
                }
            }
        }

        // Restore soulbound items on respawn
        Player player = event.getPlayer();
        PlayerInventoryData inventoryData = PlayerInventoryData.getPlayerInventoryData(player.getUniqueId());

        // Check if there are soulbound items to restore
        if (!inventoryData.soulboundInventoryContents.isEmpty() || 
            hasNonNullItems(inventoryData.soulboundArmorContents) || 
            hasNonNullItems(inventoryData.soulboundExtraContents)) {
            
            // Restore soulbound inventory items
            Map<Integer, ItemStack> soulboundInventory = inventoryData.getSoulboundInventoryAsItemStacks();
            for (Map.Entry<Integer, ItemStack> entry : soulboundInventory.entrySet()) {
                player.getInventory().setItem(entry.getKey(), entry.getValue());
            }

            // Restore soulbound armor
            ItemStack[] soulboundArmor = inventoryData.getSoulboundArmorAsItemStacks();
            player.getInventory().setArmorContents(soulboundArmor);

            // Restore soulbound extra contents
            ItemStack[] soulboundExtra = inventoryData.getSoulboundExtraAsItemStacks();
            player.getInventory().setExtraContents(soulboundExtra);

            // Clear soulbound items after restoration
            inventoryData.clearSoulboundItems();
            inventoryData.saveToPDS();
        }
    }

    // Helper method to check if array has non-null items
    private static boolean hasNonNullItems(String[] array) {
        if (array == null) return false;
        for (String item : array) {
            if (item != null) return true;
        }
        return false;
    }
}

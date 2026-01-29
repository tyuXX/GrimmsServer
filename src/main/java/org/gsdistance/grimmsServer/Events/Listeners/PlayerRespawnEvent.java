package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;

public class PlayerRespawnEvent {
    public static void Event(org.bukkit.event.player.PlayerRespawnEvent event) {
        // Don't interfere if player has a bed/respawn anchor spawn
        if (event.isBedSpawn()) {
            return; // Let Bukkit handle bed/respawn anchor spawns normally
        }
        
        // Check if player has a last death location stored in session data
        if (PerSessionDataStorage.dataStore.containsKey("deathLocation-" + event.getPlayer().getUniqueId())) {
            Location deathLocation = (Location) PerSessionDataStorage.dataStore
                .get("deathLocation-" + event.getPlayer().getUniqueId()).key();
            
            World deathWorld = deathLocation.getWorld();
            // If the death was in a custom dimension (not overworld), respawn there
            if (deathWorld != null && DynamicDimensionGen.dynamicDimensions.contains(deathWorld.getUID())) {
                // Try to respawn at the death location first
                if (deathWorld.isChunkLoaded(deathLocation.getBlockX() >> 4,
                        deathLocation.getBlockZ() >> 4)) {
                    event.setRespawnLocation(deathLocation);
                } else {
                    // If chunk isn't loaded, respawn at world spawn
                    event.setRespawnLocation(deathWorld.getSpawnLocation());
                }
            }
        }
        // Default behavior for overworld deaths or no stored death location
        // Let Bukkit handle it normally
    }
}

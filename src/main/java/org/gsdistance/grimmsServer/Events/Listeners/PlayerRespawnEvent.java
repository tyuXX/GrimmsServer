package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Indexers.DynamicDimensionGen;

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
    }
}

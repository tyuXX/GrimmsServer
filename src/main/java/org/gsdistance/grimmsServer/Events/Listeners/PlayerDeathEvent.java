package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Constructable.Data;

public class PlayerDeathEvent {
    public static void Event(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Store the death location in session storage
        PerSessionDataStorage.dataStore.put("deathLocation-" + player.getUniqueId(), 
            Data.of(player.getLocation(), org.bukkit.Location.class));
    }
}

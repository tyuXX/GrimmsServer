package org.gsdistance.grimmsServer.Events.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.gsdistance.grimmsServer.Constructable.Data;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

public class PlayerDeathEvent {
   public PlayerDeathEvent() {
   }

   public static void Event(org.bukkit.event.entity.PlayerDeathEvent event) {
      Player player = event.getEntity();
      PerSessionDataStorage.dataStore.put("deathLocation-" + String.valueOf(player.getUniqueId()), Data.of(player.getLocation(), Location.class));
   }
}

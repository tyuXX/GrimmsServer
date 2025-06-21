package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;

public class PlayerQuitEvent {
    public static void Event(org.bukkit.event.player.PlayerQuitEvent event) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        metadata.exitLocation = new Location(event.getPlayer().getLocation());
        metadata.saveToPDS();
    }
}

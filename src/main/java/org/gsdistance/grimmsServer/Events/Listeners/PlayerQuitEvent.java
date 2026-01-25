package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Commands.GAuthCommand.GAuthBaseCommand;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;

public class PlayerQuitEvent {
    public static void Event(org.bukkit.event.player.PlayerQuitEvent event) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        metadata.exitLocation = new Location(event.getPlayer().getLocation());
        metadata.lastExitTime = java.time.LocalDateTime.now().toString();
        metadata.saveToPDS();
        GAuthBaseCommand.login(event.getPlayer(), false);
    }
}

package org.gsdistance.grimmsServer.Events.Listeners;

import org.gsdistance.grimmsServer.Commands.GLevelCommand.PrestigeShop;
import org.gsdistance.grimmsServer.Commands.GUtilCommand.SettingGUI;
import org.gsdistance.grimmsServer.Constructable.Location;
import org.gsdistance.grimmsServer.Constructable.Player.PlayerMetadata;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.time.LocalDateTime;

public class PlayerQuitEvent {
    public PlayerQuitEvent() {
    }

    public static void Event(org.bukkit.event.player.PlayerQuitEvent event) {
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        metadata.exitLocation = new Location(event.getPlayer().getLocation());
        metadata.lastExitTime = LocalDateTime.now().toString();
        metadata.saveToPDS();

        // Clean up GUIs
        PrestigeShop.closeGUI(event.getPlayer());
        SettingGUI.closeGUI(event.getPlayer());

        // Clean up login time tracking
        PlayerTickEvent.onPlayerQuit(event.getPlayer());

        // Record historical stats snapshot
        GrimmsServer.historicalStatsManager.recordQuitSnapshot(event.getPlayer());
    }
}

package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PlayerTitleManager;
import org.gsdistance.grimmsServer.Data.PluginDataStorage;
import org.gsdistance.grimmsServer.GrimmsServer;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.Map;

public class PlayerJoinEvent {
    public static void Event(org.bukkit.event.player.PlayerJoinEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("join_count", 1);
        WorldStats.getWorldStats(event.getPlayer().getWorld()).changeStat("join_count", 1);
        ServerStats.getServerStats().changeStat("join_count", 1);
        PlayerTitleManager.joinedGame(event.getPlayer());

        // Initialize player metadata if not already done
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        metadata.softSave();
    }
}

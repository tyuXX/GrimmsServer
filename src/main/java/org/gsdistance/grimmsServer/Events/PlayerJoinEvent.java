package org.gsdistance.grimmsServer.Events;

import org.gsdistance.grimmsServer.Constructable.PlayerMetadata;
import org.gsdistance.grimmsServer.Data.PerSessionDataStorage;
import org.gsdistance.grimmsServer.Data.PlayerTitleChecker;
import org.gsdistance.grimmsServer.Stats.PlayerStats;
import org.gsdistance.grimmsServer.Stats.ServerStats;
import org.gsdistance.grimmsServer.Stats.WorldStats;

import java.util.ArrayList;
import java.util.Map;

public class PlayerJoinEvent {
    public static void Event(org.bukkit.event.player.PlayerJoinEvent event) {
        PlayerStats.getPlayerStats(event.getPlayer()).changeStat("join_count", 1);
        WorldStats.getWorldStats(event.getPlayer().getWorld()).changeStat("join_count", 1);
        ServerStats.getServerStats().changeStat("join_count", 1);
        PlayerTitleChecker.joinedGame(event.getPlayer());

        // Initialize player metadata if not already done
        PlayerMetadata metadata = PlayerMetadata.getPlayerMetadata(event.getPlayer());
        if(!event.getPlayer().getName().equals(metadata.lastKnownNames.getLast())) {
            metadata.lastKnownNames.add(event.getPlayer().getName());
        }

        // Initialize request data
        PerSessionDataStorage.dataStore.put("requestData-" + event.getPlayer().getName(), Map.of(new ArrayList<Integer>(), ArrayList.class));
    }
}
